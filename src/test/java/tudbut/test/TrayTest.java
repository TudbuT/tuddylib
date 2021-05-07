package tudbut.test;

import tudbut.rendering.Maths2D;
import tudbut.tools.TrayApp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class TrayTest {
    public static void main(String[] args) throws AWTException {
        TrayApp app = new TrayApp(Maths2D.createNoiseImage(new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB), 10, new Random(), 0xffffffff));
        app.onClick(() -> {
            System.out.println("GFEG");
        });
    }
}
