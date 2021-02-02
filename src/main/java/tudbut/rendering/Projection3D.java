package tudbut.rendering;

import de.tudbut.type.Vector3d;
import tudbut.parsing.TudSort;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Projection3D {
    public Vector3d offset = new Vector3d(0, 0, 0);
    AtomicInteger x;
    AtomicInteger y;
    RenderOutputType type;
    ArrayList<RenderObject3D> objects = new ArrayList<>();
    int fgColor = 0x000000;
    int bgColor;
    double var0 = 20;
    int zClip = -1;
    double fovMod = 1;

    public Projection3D(AtomicInteger xSize, AtomicInteger ySize, RenderOutputType type, int backgroundColor) {
        x = xSize;
        y = ySize;
        this.type = type;
        bgColor = backgroundColor;
    }

    public Projection3D(AtomicInteger xSize, AtomicInteger ySize, RenderOutputType type, int backgroundColor, int zClip) {
        x = xSize;
        y = ySize;
        this.type = type;
        bgColor = backgroundColor;
        this.zClip = zClip;
    }

    public void setFOVMod(double fovMod) {
        this.fovMod = fovMod;
    }

    public void drawTriangle(Vector3d pos1, Vector3d pos2, Vector3d pos3) {
        if (pos1 == null)
            throw new IllegalArgumentException();
        if (pos2 == null)
            throw new IllegalArgumentException();
        if (pos3 == null)
            throw new IllegalArgumentException();
        objects.add(new RenderObject3D(pos1, pos2, pos3, false, var0, fgColor));
    }

    public void drawRectangle(Vector3d pos1, Vector3d pos2, Vector3d pos3, Vector3d pos4) {
        if (pos1 == null)
            throw new IllegalArgumentException();
        if (pos2 == null)
            throw new IllegalArgumentException();
        if (pos3 == null)
            throw new IllegalArgumentException();
        if (pos4 == null)
            throw new IllegalArgumentException();
        objects.add(new RenderObject3D(pos1, pos2, pos3, pos4, false, var0, fgColor));
    }

    public void fillTriangle(Vector3d pos1, Vector3d pos2, Vector3d pos3) {
        if (pos1 == null)
            throw new IllegalArgumentException();
        if (pos2 == null)
            throw new IllegalArgumentException();
        if (pos3 == null)
            throw new IllegalArgumentException();
        objects.add(new RenderObject3D(pos1, pos2, pos3, true, var0, fgColor));
    }

    public void fillRectangle(Vector3d pos1, Vector3d pos2, Vector3d pos3, Vector3d pos4) {
        if (pos1 == null)
            throw new IllegalArgumentException();
        if (pos2 == null)
            throw new IllegalArgumentException();
        if (pos3 == null)
            throw new IllegalArgumentException();
        if (pos4 == null)
            throw new IllegalArgumentException();
        objects.add(new RenderObject3D(pos1, pos2, pos3, pos4, true, var0, fgColor));
    }

    public void draw(RenderObject3D object) {
        if (object == null)
            throw new IllegalArgumentException();
        objects.add(object);
    }

    public void draw(Vector3d[] vectors) {
        if (vectors == null)
            throw new IllegalArgumentException();
        if (vectors.length == 3) {
            objects.add(new RenderObject3D(vectors[0], vectors[1], vectors[2], false, var0, fgColor));
        }
        else if (vectors.length == 4) {
            objects.add(new RenderObject3D(vectors[0], vectors[1], vectors[2], vectors[3], false, var0, fgColor));
        }
        else
            throw new IllegalArgumentException();
    }

    public void setColor(int hexColor) {
        fgColor = hexColor;
    }

    public Object render() {
        BufferedImage image = new BufferedImage(x.get(), y.get(), BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.setColor(new Color(bgColor));
        g.fillRect(0, 0, x.get(), y.get());

        render(g);

        g.dispose();

        objects.clear();

        if (type == RenderOutputType.ARRAY2D) {
            int[][] ints = new int[x.get()][y.get()];

            for (int i = 0; i < x.get(); i++) {
                for (int j = 0; j < y.get(); j++) {
                    ints[i][j] = image.getRGB(i, j);
                }
            }

            return ints;
        }
        if (type == RenderOutputType.ARRAY) {
            int[] ints = new int[x.get() * y.get()];

            int c = 0;
            for (int i = 0; i < x.get(); i++) {
                for (int j = 0; j < y.get(); j++) {
                    ints[c] = image.getRGB(i, j);

                    c++; // YEET
                }
            }

            return ints;
        }
        if (type == RenderOutputType.BUFFEREDIMAGE) {
            return image;
        }
        return null;
    }

    void render(Graphics g) {
        /*objects.sort((renderObject0, renderObject1) -> {
            double i = 0;

            for (Vector3d vector : renderObject0.vectors) {
                i = Math.max(vector.getZ(), i);
            }
            double i1 = 0;

            for (Vector3d vector : renderObject1.vectors) {
                i1 = Math.max(vector.getZ(), i1);
            }

            return (int) (i1 - i);
        });*/

        RenderObject3D[] objects = this.objects.toArray(new RenderObject3D[0]);

        try {
            objects = TudSort.sortDouble(objects, t -> {
                double i = 0;
                int j = 0;

                for (Vector3d vector : t.vectors) {
                    i += Math.max(vector.getX(), -vector.getX());
                    j++;
                }

                for (Vector3d vector : t.vectors) {
                    i += Math.max(vector.getY(), -vector.getY());
                    j++;
                }

                for (Vector3d vector : t.vectors) {
                    i += vector.getZ() * 5;
                    j++;
                }

                i = i / j;

                return -i;
            });
        } catch (Throwable ignored) { }
    
        for (int i = 0, objectsLength = objects.length; i < objectsLength; i++) {
            RenderObject3D o = objects[i];
            if (o == null)
                continue;
            o = o.clone();
            for (Vector3d vector : o.vectors) {
                vector.add(offset);
            }
            if (o.color >= 0 && o.color <= 0xffffff) {
                g.setColor(new Color(o.color, false));
            } else
                g.setColor(new Color(o.color, true));
        
            if (o.type == RenderObjectType.RECTANGLE) {
                Maths3D.prepareVectorsForRectangle(o, x.get(), y.get(), var0, fovMod);
            
                if (o.isClipped) {
                    continue;
                }
            
                Vector3d pos1 = o.vectors[0];
                Vector3d pos2 = o.vectors[1];
                Vector3d pos3 = o.vectors[2];
                Vector3d pos4 = o.vectors[3];
            
                g.drawLine((int) (pos1.getX()), (int) (pos1.getY()), (int) (pos2.getX()), (int) (pos2.getY()));
                g.drawLine((int) (pos2.getX()), (int) (pos2.getY()), (int) (pos3.getX()), (int) (pos3.getY()));
                g.drawLine((int) (pos3.getX()), (int) (pos3.getY()), (int) (pos4.getX()), (int) (pos4.getY()));
                g.drawLine((int) (pos4.getX()), (int) (pos4.getY()), (int) (pos1.getX()), (int) (pos1.getY()));
            }
        
            if (o.type == RenderObjectType.TRIANGLE) {
                Maths3D.prepareVectorsForTriangle(o, x.get(), y.get(), var0, fovMod);
            
                if (o.isClipped) {
                    continue;
                }
            
                Vector3d pos1 = o.vectors[0];
                Vector3d pos2 = o.vectors[1];
                Vector3d pos3 = o.vectors[2];
            
                g.drawLine((int) (pos1.getX()), (int) (pos1.getY()), (int) (pos2.getX()), (int) (pos2.getY()));
                g.drawLine((int) (pos2.getX()), (int) (pos2.getY()), (int) (pos3.getX()), (int) (pos3.getY()));
                g.drawLine((int) (pos3.getX()), (int) (pos3.getY()), (int) (pos1.getX()), (int) (pos1.getY()));
            }
        
            if (o.type == RenderObjectType.FULL_RECTANGLE) {
                Maths3D.prepareVectorsForRectangle(o, x.get(), y.get(), var0, fovMod);
            
                if (o.isClipped) {
                    continue;
                }
            
                int[][] rpos = Maths3D.getFillingCoordinatesForRectangle(o);
            
                g.fillPolygon(rpos[0], rpos[1], 4);
            }
        
            if (o.type == RenderObjectType.FULL_TRIANGLE) {
                Maths3D.prepareVectorsForTriangle(o, x.get(), y.get(), var0, fovMod);
            
                if (o.isClipped) {
                    continue;
                }
            
                int[][] rpos = Maths3D.getFillingCoordinatesForTriangle(o);
            
                g.fillPolygon(rpos[0], rpos[1], 3);
            }
        }
    }


}
