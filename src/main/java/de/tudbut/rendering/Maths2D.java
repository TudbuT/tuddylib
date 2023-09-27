package de.tudbut.rendering;

import de.tudbut.type.Vector2d;
import de.tudbut.tools.NoiseGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Maths2D {
    public static int camera(int i, int camera) {
        return i - camera;
    }
    
    public static int center(int i, int size) {
        return i + size / 2;
    }
    
    public static boolean[] getRelation(Rectangle2D measuringObject, Rectangle2D toMeasure) {
        boolean[] b = new boolean[4];
        Vector2d a0 = toMeasure.getPos();
        Vector2d a1 = toMeasure.getEndPoint();
        Vector2d b0 = measuringObject.getPos();
        Vector2d b1 = measuringObject.getEndPoint();
        
        b[0] = a0.getX() < b1.getX();
        b[1] = a0.getY() < b1.getY();
        b[2] = a1.getX() > b0.getX();
        b[3] = a1.getY() > b0.getY();
    
        for (int i = 0; i < b.length; i++) {
            b[i] = !b[i];
        }
    
        return b;
    }
    
    public static BufferedImage createNoiseImage(BufferedImage base, int smoothness, Random random, int color) {
        int x = base.getWidth(), y = base.getHeight();
        BufferedImage image = Maths2D.distortImage(base, x, y, 1);
        //noinspection SuspiciousNameCombination
        float[][] floats = NoiseGenerator.generateRandom(1, x, y, smoothness, 1, random)[0];
    
        Graphics graphics = image.getGraphics();
        for (int i = 0 ; i < y ; i++) {
            for (int j = 0 ; j < x ; j++) {
                Color c = new Color(color, true);
                c = new Color(c.getRed() * floats[j][i] / 255, c.getGreen() * floats[j][i] / 255, c.getBlue() * floats[j][i] / 255, 1);
                graphics.setColor(c);
                graphics.drawRect(j, i, 1, 1);
            }
        }
        
        return image;
    }
    
    public static BufferedImage createMultiNoiseImage(BufferedImage base, int smoothness, Random random, int color) {
        int x = base.getWidth(), y = base.getHeight();
        BufferedImage image = Maths2D.distortImage(base, x, y, 1);
        //noinspection SuspiciousNameCombination
        float[][][] floats = new float[][][] {
                NoiseGenerator.generateRandom(1, x, y, smoothness, 1, random)[0],
                NoiseGenerator.generateRandom(1, x, y, smoothness, 1, random)[0],
                NoiseGenerator.generateRandom(1, x, y, smoothness, 1, random)[0],
                NoiseGenerator.generateRandom(1, x, y, smoothness, 1, random)[0]
        };
        
        Graphics graphics = image.getGraphics();
        for (int i = 0 ; i < y ; i++) {
            for (int j = 0 ; j < x ; j++) {
                Color c = new Color(color, true);
                c = new Color((int) (c.getRed() * floats[1][j][i]), (int) (c.getGreen() * floats[2][j][i]), (int) (c.getBlue() * floats[3][j][i]), (int) (c.getAlpha() * floats[0][j][i]));
                graphics.setColor(c);
                graphics.drawRect(j, i, 1, 1);
            }
        }
        
        return image;
    }
    
    public static boolean collides(Rectangle2D rectangle0, Rectangle2D rectangle1) {
        boolean b = true;
        boolean[] rel = getRelation(rectangle0, rectangle1);
        for (int i = 0; i < rel.length; i++) {
            if (rel[i]) {
                b = false;
                break;
            }
        }
        return b;
    }
    
    public static int[][] getFillingCoordinatesForRectangle(RenderObject2D o) {
        int[][] r = new int[2][4];

        r[0][0] = (int) o.vectors[0].getX();
        r[0][1] = (int) o.vectors[1].getX();
        r[0][2] = (int) o.vectors[2].getX();
        r[0][3] = (int) o.vectors[3].getX();

        r[1][0] = (int) o.vectors[0].getY();
        r[1][1] = (int) o.vectors[1].getY();
        r[1][2] = (int) o.vectors[2].getY();
        r[1][3] = (int) o.vectors[3].getY();

        return r;
    }

    public static int[][] getFillingCoordinatesForTriangle(RenderObject2D o) {
        int[][] r = new int[2][3];

        r[0][0] = (int) o.vectors[0].getX();
        r[0][1] = (int) o.vectors[1].getX();
        r[0][2] = (int) o.vectors[2].getX();

        r[1][0] = (int) o.vectors[0].getY();
        r[1][1] = (int) o.vectors[1].getY();
        r[1][2] = (int) o.vectors[2].getY();

        return r;
    }

    public static BufferedImage distortImage(Image image, int newResolutionX, int newResolutionY, double multiplier) {
        newResolutionX = (int) (newResolutionX * multiplier);
        newResolutionY = (int) (newResolutionY * multiplier);

        BufferedImage r = new BufferedImage(newResolutionX, newResolutionY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img = (BufferedImage) image;

        for (int x = 0; x < newResolutionX; x++) {
            for (int y = 0; y < newResolutionY; y++) {
                r.setRGB(x, y, img.getRGB((int) ((double) x / ((double) newResolutionX / (double) img.getWidth())), (int) ((double) y / ((double) newResolutionY / (double) img.getHeight()))));
            }
        }

        return r;
    }
    
    public static BufferedImage prepareImage(BufferedImage image, Vector2d pos, Vector2d endPos) {
        endPos = endPos.clone().add(pos.clone().negate());
        return distortImage(image, (int) endPos.getX(), (int) endPos.getY(), 1);
    }

    public static BufferedImage rotateImage(Image image, double rot) {
        rot = Math.PI * (rot / 360 * 2);
        BufferedImage img = (BufferedImage) image;
        BufferedImage r = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Vector2d p = new Vector2d(img.getWidth() / 2d, img.getHeight() / 2d);

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Vector2d rotated = rotate(new Vector2d(x,y), p, rot);
                try {
                    r.setRGB(x, y, img.getRGB((int) rotated.getX(), (int) rotated.getY()));
                } catch (Exception ignore) { }
            }
        }

        return r;
    }

    public static BufferedImage rotateImage(Image image, double rot, boolean clockwise) {
        return clockwise ? rotateImage(image, -rot) : rotateImage(image, rot);
    }

    public static BufferedImage cropImage(Image image, Vector2d start, Vector2d end) {
        BufferedImage img = (BufferedImage) image;
        BufferedImage r = new BufferedImage((int) (end.getY() - start.getY()), (int) (end.getY() - start.getY()), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < r.getWidth(); x++) {
            for (int y = 0; y < r.getHeight(); y++) {
                r.setRGB(x, y, img.getRGB((int) (x + start.getX()), (int) (y + start.getY())));
            }
        }

        return r;
    }

    public static BufferedImage addImageBorder(Image image, Vector2d posOnResult, Vector2d resultSize, int color) {
        BufferedImage img = (BufferedImage) image;
        BufferedImage r = new BufferedImage((int) resultSize.getX(), (int) resultSize.getY(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = r.getGraphics().create();

        if(color >= 0 && color <= 0xffffff)
            g.setColor(new Color(color, false));
        else
            g.setColor(new Color(color, true));

        g.fillRect(0,(int) posOnResult.getY(), (int) posOnResult.getX(), (int) resultSize.getY());

        g.fillRect(0,0, (int) resultSize.getX(), (int) posOnResult.getY());

        g.fillRect((int) posOnResult.getX() + img.getWidth(), (int) (posOnResult.getY()), (int) (resultSize.getX() - (posOnResult.getX() + img.getWidth())), (int) (resultSize.getY() - (resultSize.getY() - img.getHeight())));

        g.fillRect((int) posOnResult.getX(), (int) posOnResult.getY() + img.getHeight(), (int) resultSize.getX(), (int) (resultSize.getY() - (posOnResult.getY() + img.getHeight())));


        g.drawImage(img, (int) posOnResult.getX(), (int) posOnResult.getY(), null);

        g.dispose();
        return r;
    }

    public static Vector2d rotate(Vector2d vec, Vector2d point, double rotation) {
        point.negate();
        vec.add(point);
        double sinRot = Math.sin(rotation);
        double cosRot = Math.cos(rotation);
        vec.set(
                vec.getX() * cosRot + vec.getY() * -sinRot,
                vec.getX() * sinRot + vec.getY() * cosRot
        );
        point.negate();
        vec.add(point);
        return vec;
    }
}

