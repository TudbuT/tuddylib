package tudbut.rendering;

import de.tudbut.tools.ExtendedMath;
import de.tudbut.type.FInfo;
import de.tudbut.type.Vector2d;
import de.tudbut.type.Vector3d;

import java.awt.*;
import java.awt.image.BufferedImage;

import static de.tudbut.tools.ExtendedMath.highestMinusLowest;

public class Maths2D {
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

    @FInfo(s="Not usable for multithreaded use of the vectors!! Use rotateMt")
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

    @FInfo(s="Clones the vectors before modifying them so it works with MultiThreading!")
    public static Vector2d rotateMt(Vector2d vec, Vector2d point, double rotation) {
        Vector2d p = vec.clone();
        Vector2d c = point.clone();
        c.negate();
        p.add(c);
        double sinRot = Math.sin(rotation);
        double cosRot = Math.cos(rotation);
        p.set(
                p.getX() * cosRot + p.getY() * -sinRot,
                p.getX() * sinRot + p.getY() * cosRot
        );
        c.negate();
        p.add(c);
        vec.set(p);
        return vec;
    }
}

