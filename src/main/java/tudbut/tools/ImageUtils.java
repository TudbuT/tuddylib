package tudbut.tools;

import tudbut.rendering.Maths2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.lang.Math.*;

public class ImageUtils {
    
    public static BufferedImage antiAlias(BufferedImage image, int amount) {
        return antiAlias(image, amount, 20);
    }
    
    public static BufferedImage antiAlias(BufferedImage image, int amount, float m) {
        BufferedImage img = smoothen(getContrastColors(image), amount, m);
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x,y), image.getColorModel().hasAlpha());
                Color sc = new Color(img.getRGB(x,y), true);
                out.setRGB(x,y, new Color(
                        max(c.getRed(), sc.getRed()),
                        max(c.getGreen(), sc.getGreen()),
                        max(c.getBlue(), sc.getBlue()),
                        255
                ).getRGB());
            }
        }
        return out;
    }
    
    public static BufferedImage whiteContrast(BufferedImage image, int amount) {
        BufferedImage img = smoothen(getContrast(image), amount, 2);
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x,y), image.getColorModel().hasAlpha());
                Color sc = new Color(img.getRGB(x,y), image.getColorModel().hasAlpha());
                out.setRGB(x,y, new Color(
                        min(abs(c.getRed() + sc.getRed()), 255),
                        min(abs(c.getGreen() + sc.getGreen()), 255),
                        min(abs(c.getBlue() + sc.getBlue()), 255),
                        255
                ).getRGB());
            }
        }
        return out;
    }
    
    public static BufferedImage whiteContrastColored(BufferedImage image, int amount) {
        BufferedImage img = smoothen(getContrastColors(image), amount, 2);
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x,y), image.getColorModel().hasAlpha());
                Color sc = new Color(img.getRGB(x,y), image.getColorModel().hasAlpha());
                out.setRGB(x,y, new Color(
                        min(abs(c.getRed() + sc.getRed()), 255),
                        min(abs(c.getGreen() + sc.getGreen()), 255),
                        min(abs(c.getBlue() + sc.getBlue()), 255),
                        255
                ).getRGB());
            }
        }
        return out;
    }
    
    public static BufferedImage contrast(BufferedImage image, int amount) {
        BufferedImage img = smoothen(getContrast(image), amount);
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x,y), image.getColorModel().hasAlpha());
                Color sc = new Color(img.getRGB(x,y), image.getColorModel().hasAlpha());
                out.setRGB(x,y, new Color(
                        abs(c.getRed() - sc.getRed()),
                        abs(c.getGreen() - sc.getGreen()),
                        abs(c.getBlue() - sc.getBlue()),
                        255
                ).getRGB());
            }
        }
        return out;
    }
    
    public static BufferedImage contrastColored(BufferedImage image, int amount) {
        BufferedImage img = smoothen(getContrastColors(image), amount);
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x,y), image.getColorModel().hasAlpha());
                Color sc = new Color(img.getRGB(x,y), image.getColorModel().hasAlpha());
                out.setRGB(x,y, new Color(
                        abs(c.getRed() - sc.getRed()),
                        abs(c.getGreen() - sc.getGreen()),
                        abs(c.getBlue() - sc.getBlue()),
                        255
                ).getRGB());
            }
        }
        return out;
    }
    
    public static BufferedImage smoothen(BufferedImage image, int amount, float m) {
        float[][][] floats = imageToFloats(image);
        float[][][] floats1 = new float[4][floats.length][floats[0].length];
        for (int x = 0 ; x < floats.length ; x++) {
            for (int y = 0 ; y < floats[0].length ; y++) {
                floats1[0][x][y] = floats[x][y][0];
                floats1[1][x][y] = floats[x][y][1];
                floats1[2][x][y] = floats[x][y][2];
                floats1[3][x][y] = floats[x][y][3];
            }
        }
        NoiseGenerator.smooth(new float[][][]{floats1[0]}, 0, 0, 0, 1, image.getWidth(), image.getHeight(), amount, m);
        NoiseGenerator.smooth(new float[][][]{floats1[1]}, 0, 0, 0, 1, image.getWidth(), image.getHeight(), amount, m);
        NoiseGenerator.smooth(new float[][][]{floats1[2]}, 0, 0, 0, 1, image.getWidth(), image.getHeight(), amount, m);
        NoiseGenerator.smooth(new float[][][]{floats1[3]}, 0, 0, 0, 1, image.getWidth(), image.getHeight(), amount, m);
        for (int x = 0 ; x < floats.length ; x++) {
            for (int y = 0 ; y < floats[0].length ; y++) {
                floats[x][y][0] = floats1[0][x][y];
                floats[x][y][1] = floats1[1][x][y];
                floats[x][y][2] = floats1[2][x][y];
                floats[x][y][3] = floats1[3][x][y];
            }
        }
        return floatsToImage(floats);
    }
    
    public static BufferedImage smoothen(BufferedImage image, int amount) {
        return smoothen(image, amount, 2);
    }
    
    public static BufferedImage smoothenColors(BufferedImage image, int amount) {
        return smoothenColors(image, amount, 2);
    }
    
    public static BufferedImage smoothenColors(BufferedImage image, int amount, float m) {
        float[][][] floats = imageToFloats(image);
        NoiseGenerator.smooth(floats, 0, 0, 0, image.getWidth(), image.getHeight(), 4, amount, m);
        return floatsToImage(floats);
    }
    
    public static float[][][] imageToFloats(BufferedImage image) {
        float[][][] floats = new float[image.getWidth()][image.getHeight()][4];
        for (int x = 0 ; x < floats.length ; x++) {
            for (int y = 0 ; y < floats[0].length ; y++) {
                float[] c = floats[x][y];
                Color color = new Color(image.getRGB(x,y), true);
                c[0] = color.getRed() / 255f;
                c[1] = color.getGreen() / 255f;
                c[2] = color.getBlue() / 255f;
                c[3] = color.getAlpha() / 255f;
            }
        }
        return floats;
    }
    
    public static BufferedImage floatsToImage(float[][][] floats) {
        BufferedImage image = new BufferedImage(floats.length, floats[0].length, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0 ; x < floats.length ; x++) {
            for (int y = 0 ; y < floats[0].length ; y++) {
                float[] c = floats[x][y];
                Color color = new Color(c[0], c[1], c[2], c[3]);
                image.setRGB(x,y, color.getRGB());
            }
        }
        return image;
    }
    
    public static BufferedImage getContrast(BufferedImage image) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0 ; x < image.getWidth() ; x++) {
            for (int y = 0 ; y < image.getHeight() ; y++) {
                Color c0 = new Color(image.getRGB(x, y), image.getColorModel().hasAlpha());
                int i = c0.getRed() + c0.getGreen() + c0.getBlue() + c0.getAlpha();
                for (int ox = -1 ; ox <= 1 ; ox++) {
                    for (int oy = -1 ; oy <= 1 ; oy++) {
                        int tx = x + ox;
                        int ty = y + oy;
                        if(tx >= 0 && ty >= 0) {
                            if(tx < image.getWidth() && ty < image.getHeight()) {
                                Color c1 = new Color(image.getRGB(tx, ty), image.getColorModel().hasAlpha());
                                i += abs(c0.getAlpha() - c1.getAlpha());
                                i += abs(c0.getRed() - c1.getRed());
                                i += abs(c0.getGreen() - c1.getGreen());
                                i += abs(c0.getBlue() - c1.getBlue());
                            }
                        }
                    }
                }
                i /= 4*2;
                i = min(i, 0xff);
                Color color = new Color(i, i, i, 0xff);
                out.setRGB(x, y, color.getRGB());
            }
        }
        return out;
    }
    
    
    public static BufferedImage getContrastColors(BufferedImage image) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0 ; x < image.getWidth() ; x++) {
            for (int y = 0 ; y < image.getHeight() ; y++) {
                Color c0 = new Color(image.getRGB(x, y), image.getColorModel().hasAlpha());
                Color[] c = new Color[9];
                Arrays.fill(c, c0);
                int i = 0;
                for (int ox = -1 ; ox <= 1 ; ox++) {
                    for (int oy = -1 ; oy <= 1 ; oy++) {
                        int tx = x + ox;
                        int ty = y + oy;
                        if(tx >= 0 && ty >= 0) {
                            if(tx < image.getWidth() && ty < image.getHeight()) {
                                Color c1 = new Color(image.getRGB(tx, ty), image.getColorModel().hasAlpha());
                                c[i++] = new Color(
                                        abs(c0.getRed() - c1.getRed()),
                                        abs(c0.getGreen() - c1.getGreen()),
                                        abs(c0.getBlue() - c1.getBlue()),
                                        abs(c0.getAlpha() - c1.getAlpha())
                                );
                            }
                        }
                    }
                }
                Color color = new Color(
                        min((
                                c[0].getRed() + c[1].getRed() + c[2].getRed() +
                                c[3].getRed() + c[4].getRed() + c[5].getRed() +
                                c[6].getRed() + c[7].getRed() + c[8].getRed()
                        ) / 4.5f / 255, 1),
                        min((
                                c[0].getGreen() + c[1].getGreen() + c[2].getGreen() +
                                c[3].getGreen() + c[4].getGreen() + c[5].getGreen() +
                                c[6].getGreen() + c[7].getGreen() + c[8].getGreen()
                        ) / 4.5f / 255, 1),
                        min((
                                c[0].getBlue() + c[1].getBlue() + c[2].getBlue() +
                                c[3].getBlue() + c[4].getBlue() + c[5].getBlue() +
                                c[6].getBlue() + c[7].getBlue() + c[8].getBlue()
                        ) / 4.5f / 255, 1),
                        min((
                                c[0].getAlpha() + c[1].getAlpha() + c[2].getAlpha() +
                                c[3].getAlpha() + c[4].getAlpha() + c[5].getAlpha() +
                                c[6].getAlpha() + c[7].getAlpha() + c[8].getAlpha()
                        ) / 4.5f / 255, 1)
                );
                out.setRGB(x, y, color.getRGB());
            }
        }
        return out;
    }
    
    public static BufferedImage getDifference(BufferedImage image0, BufferedImage image1) {
        if(image0.getWidth() != image1.getWidth() || image0.getHeight() != image1.getHeight())
            throw new IllegalArgumentException("Width and Height don't match!");
        
        BufferedImage out = new BufferedImage(image0.getWidth(), image0.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0 ; x < image0.getWidth() ; x++) {
            for (int y = 0 ; y < image0.getHeight() ; y++) {
                Color c0 = new Color(image0.getRGB(x, y), image0.getColorModel().hasAlpha());
                Color c1 = new Color(image1.getRGB(x, y), image1.getColorModel().hasAlpha());
                Color color = new Color(
                        abs(c0.getRed() - c1.getRed()),
                        abs(c0.getGreen() - c1.getGreen()),
                        abs(c0.getBlue() - c1.getBlue()),
                        abs(c0.getAlpha() - c1.getAlpha())
                );
                out.setRGB(x, y, color.getRGB());
            }
        }
        return out;
    }
    
    public static float getSimilarity(BufferedImage image0, BufferedImage image1) {
        float f;
        int fullDiff = 0;
        BufferedImage diff = getDifference(Maths2D.distortImage(image0, 2, 2, 1), Maths2D.distortImage(image1, 2, 2, 1));
        for (int x = 0 ; x < 2 ; x++) {
            for (int y = 0 ; y < 2 ; y++) {
                Color color = new Color(diff.getRGB(x,y));
                fullDiff += color.getRed();
                fullDiff += color.getGreen();
                fullDiff += color.getBlue();
            }
        }
        fullDiff = max(255 - fullDiff, 0);
        f = fullDiff / 255f;
        
        for (int n = 4 ; n < 100 ; n+=4) {
            fullDiff = 0;
            diff = getDifference(Maths2D.distortImage(image0, n, n, 1), Maths2D.distortImage(image1, n, n, 1));
            for (int x = 0 ; x < n ; x++) {
                for (int y = 0 ; y < n ; y++) {
                    Color color = new Color(diff.getRGB(x,y));
                    int i = 0;
                    i += color.getRed();
                    i += color.getGreen();
                    i += color.getBlue();
                    i /= 3;
                    fullDiff += i;
                }
            }
            fullDiff /= n / 2f * n / 2f;
            fullDiff = max(255 - fullDiff, 0);
            f = f*n/4f + fullDiff / 255f;
            f /= n / 4f + 1;
        }
        return f;
    }
}
