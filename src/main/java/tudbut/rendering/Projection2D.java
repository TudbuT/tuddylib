package tudbut.rendering;

import de.tudbut.type.Vector2d;
import de.tudbut.ui.windowgui.AdaptedGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Projection2D {
    AtomicInteger x;
    AtomicInteger y;
    RenderOutputType type;
    ArrayList<RenderObject2D> objects = new ArrayList<>();
    ArrayList<Object[]> imageObjects = new ArrayList<>();
    int fgColor = 0x000000;
    int bgColor;
    double multiplier;
    Vector2d offset = new Vector2d(0,0);

    public Projection2D(AtomicInteger xSize, AtomicInteger ySize, RenderOutputType type, int backgroundColor) {
        x = xSize;
        y = ySize;
        this.type = type;
        bgColor = backgroundColor;
        multiplier = 1;
    }

    public Projection2D(AtomicInteger xSize, AtomicInteger ySize, RenderOutputType type, int backgroundColor, double multiplier) {
        x = xSize;
        y = ySize;
        this.type = type;
        bgColor = backgroundColor;
        this.multiplier = multiplier + 1;
    }

    public void setMultiplier(double multiplier) {
        if (multiplier == 0)
            throw new IllegalArgumentException("Multiplier may not be 0");

        this.multiplier = multiplier;
    }

    public void setOffset(Vector2d vector) {
        this.offset = vector;
    }

    public void drawTriangle(Vector2d pos1, Vector2d pos2, Vector2d pos3) {
        if (pos1 == null)
            throw new IllegalArgumentException();
        if (pos2 == null)
            throw new IllegalArgumentException();
        if (pos3 == null)
            throw new IllegalArgumentException();
        objects.add(new RenderObject2D(pos1, pos2, pos3, false, multiplier, fgColor));
    }

    public void drawImage(Vector2d pos, Image image, int newResolutionX, int newResolutionY) {
        if (pos == null)
            throw new IllegalArgumentException();

        imageObjects.add(new Object[]{pos, image, newResolutionX, newResolutionY, true});
    }

    public void drawImage(Vector2d pos, Image image, int newResolutionX, int newResolutionY, boolean applyMultiplier) {
        if (pos == null)
            throw new IllegalArgumentException();

        imageObjects.add(new Object[]{pos, image, newResolutionX, newResolutionY, applyMultiplier});
    }

    public void drawRectangle(Vector2d pos1, Vector2d pos2, Vector2d pos3, Vector2d pos4) {
        if (pos1 == null)
            throw new IllegalArgumentException();
        if (pos2 == null)
            throw new IllegalArgumentException();
        if (pos3 == null)
            throw new IllegalArgumentException();
        if (pos4 == null)
            throw new IllegalArgumentException();
        objects.add(new RenderObject2D(pos1, pos2, pos3, pos4, false, multiplier, fgColor));
    }

    public void fillTriangle(Vector2d pos1, Vector2d pos2, Vector2d pos3) {
        if (pos1 == null)
            throw new IllegalArgumentException();
        if (pos2 == null)
            throw new IllegalArgumentException();
        if (pos3 == null)
            throw new IllegalArgumentException();
        objects.add(new RenderObject2D(pos1, pos2, pos3, true, multiplier, fgColor));
    }

    public void fillRectangle(Vector2d pos1, Vector2d pos2, Vector2d pos3, Vector2d pos4) {
        if (pos1 == null)
            throw new IllegalArgumentException();
        if (pos2 == null)
            throw new IllegalArgumentException();
        if (pos3 == null)
            throw new IllegalArgumentException();
        if (pos4 == null)
            throw new IllegalArgumentException();
        objects.add(new RenderObject2D(pos1, pos2, pos3, pos4, true, multiplier, fgColor));
    }

    public void draw(RenderObject2D object) {
        if (object == null)
            throw new IllegalArgumentException();
        objects.add(object);
    }

    public void draw(Vector2d[] vectors) {
        if (vectors == null)
            throw new IllegalArgumentException();
        if (vectors.length == 3) {
            objects.add(new RenderObject2D(vectors[0], vectors[1], vectors[2], false, multiplier, fgColor));
        }
        else if (vectors.length == 4) {
            objects.add(new RenderObject2D(vectors[0], vectors[1], vectors[2], vectors[3], false, multiplier, fgColor));
        }
        else
            throw new IllegalArgumentException();
    }

    public void setColor(int hexColor) {
        fgColor = hexColor;
    }

    public Object render() {
        BufferedImage image = new BufferedImage(x.get(), y.get(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        g.setColor(new Color(bgColor));
        g.fillRect(0, 0, x.get(), y.get());

        render(g);

        g.dispose();

        objects.clear();
        imageObjects.clear();

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
        for (RenderObject2D o : objects) {
            o = o.clone();
            for (int i = 0; i < o.vectors.length; i++) {
                o.vectors[i].add(offset);
            }
            g.setColor(new Color(o.color));

            if (o.type == RenderObjectType.RECTANGLE) {
                Vector2d pos1 = o.vectors[0];
                Vector2d pos2 = o.vectors[1];
                Vector2d pos3 = o.vectors[2];
                Vector2d pos4 = o.vectors[3];

                g.drawLine((int) pos1.getX(), (int) pos1.getY(), (int) pos2.getX(), (int) pos2.getY());
                g.drawLine((int) pos2.getX(), (int) pos2.getY(), (int) pos3.getX(), (int) pos3.getY());
                g.drawLine((int) pos3.getX(), (int) pos3.getY(), (int) pos4.getX(), (int) pos4.getY());
                g.drawLine((int) pos4.getX(), (int) pos4.getY(), (int) pos1.getX(), (int) pos1.getY());
            }

            if (o.type == RenderObjectType.TRIANGLE) {
                Vector2d pos1 = o.vectors[0];
                Vector2d pos2 = o.vectors[1];
                Vector2d pos3 = o.vectors[2];

                g.drawLine((int) pos1.getX(), (int) pos1.getY(), (int) pos2.getX(), (int) pos2.getY());
                g.drawLine((int) pos2.getX(), (int) pos2.getY(), (int) pos3.getX(), (int) pos3.getY());
                g.drawLine((int) pos3.getX(), (int) pos3.getY(), (int) pos1.getX(), (int) pos1.getY());
            }

            if (o.type == RenderObjectType.FULL_RECTANGLE) {
                int[][] rpos = Maths2D.getFillingCoordinatesForRectangle(o);

                g.fillPolygon(rpos[0], rpos[1], 4);
            }

            if (o.type == RenderObjectType.FULL_TRIANGLE) {
                int[][] rpos = Maths2D.getFillingCoordinatesForTriangle(o);

                g.fillPolygon(rpos[0], rpos[1], 3);
            }
        }

        for (Object[] object : imageObjects) {
            Vector2d pos = (Vector2d) object[0];
            Image image = (Image) object[1];
            int nx = (Integer) object[2];
            int ny = (Integer) object[3];
            boolean applyMultiplier = (Boolean) object[4];
            pos.add(offset);


            new AdaptedGraphics(g).drawImage((int) (pos.getX() * (applyMultiplier ? multiplier : 1)), (int) (pos.getY() * (applyMultiplier ? multiplier : 1)), Maths2D.distortImage(image, nx, ny, applyMultiplier ? multiplier : 1));
        }
    }


}
