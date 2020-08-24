package de.tudbut;

import de.tudbut.tools.Keyboard;
import de.tudbut.type.Vector2d;
import de.tudbut.type.Vector3d;
import de.tudbut.ui.windowgui.RenderableWindow;
import tudbut.rendering.Maths2D;
import tudbut.rendering.Projection3D;
import tudbut.rendering.RenderOutputType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import static tudbut.rendering.tph.TPH300.*;
import static tudbut.rendering.tph.TPH301.*;
import static tudbut.rendering.tph.TPH302.*;

public class Main {
    private static final AtomicInteger lastMousePosX = new AtomicInteger();
    private static final AtomicInteger lastMousePosY = new AtomicInteger();
    private static final AtomicInteger rotX = new AtomicInteger();
    private static final AtomicInteger rotY = new AtomicInteger();

    private static final Vector3d startPoint = new Vector3d(0, 0, 5);
    private static final Vector3d zero = new Vector3d(0, 0, 0);

    private static final Vector3d offset = startPoint.clone();
    private static final Vector3d position = startPoint.clone();

    private static boolean tab = false;

    private static final long cam = createCamera(startPoint, 0, 0);


    private static final RenderableWindow window = new RenderableWindow(16 * 70, 9 * 70, "TEST", 20, true);
    private static final Projection3D projection = new Projection3D(window.xSize, window.ySize, RenderOutputType.BUFFEREDIMAGE, 0x002200);


    public static void main(String[] args) throws AWTException, InterruptedException {

        System.out.println(0xffffff);
        System.out.println(0x01000000);
        window.getWindow().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        projection.setFOVMod(1);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        window.getWindow().setLocation(screenSize.width / 2 - (16 * 70) / 2, screenSize.height / 2 - (9 * 70) / 2);

        offset.set(startPoint.clone().multiply(20));

        Robot robot = new Robot();
        BufferedImage n = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        Cursor nc;
        robot.mouseMove(window.getWindow().getLocationOnScreen().x + window.xSize.get() / 2, window.getWindow().getLocationOnScreen().y + window.ySize.get() / 2);
        window.getWindow().setCursor(nc = Toolkit.getDefaultToolkit().createCustomCursor(n, new Point(0,0), "empty"));
        lastMousePosX.set(MouseInfo.getPointerInfo().getLocation().x);
        lastMousePosY.set(MouseInfo.getPointerInfo().getLocation().y);

        new Thread(() ->{
            while(true) {
                if (Keyboard.isKeyDown(KeyEvent.VK_Q)) {
                    tab = !tab;
                    window.getWindow().setCursor(nc);
                    while (Keyboard.isKeyDown(KeyEvent.VK_Q)) ;
                }

                if (tab) {
                    window.getWindow().setCursor(Cursor.getDefaultCursor());
                    continue;
                }
                if (Keyboard.isKeyDown(KeyEvent.VK_A)) {
                    Vector3d rot = applyRotation(new Vector3d(1, 0, 0), zero, Y, rotX.get() / 5d);
                    offset.add(new Vector3d(rot.getX(), rot.getY(), rot.getZ()));
                }
                if (Keyboard.isKeyDown(KeyEvent.VK_D)) {
                    Vector3d rot = applyRotation(new Vector3d(-1, 0, 0), zero, Y, rotX.get() / 5d);
                    offset.add(new Vector3d(rot.getX(), rot.getY(), rot.getZ()));
                }
                if (Keyboard.isKeyDown(KeyEvent.VK_SHIFT)) {
                    offset.add(new Vector3d(0, -1, 0));
                }
                if (Keyboard.isKeyDown(KeyEvent.VK_SPACE)) {
                    offset.add(new Vector3d(0, 1, 0));
                }
                if (Keyboard.isKeyDown(KeyEvent.VK_W)) {
                    Vector3d rot = applyRotation(new Vector3d(0, 0, -1), zero, Y, rotX.get() / 5d);
                    offset.add(new Vector3d(rot.getX(), rot.getY(), rot.getZ()));
                }
                if (Keyboard.isKeyDown(KeyEvent.VK_S)) {
                    Vector3d rot = applyRotation(new Vector3d(0, 0, 1), zero, Y, rotX.get() / 5d);
                    offset.add(new Vector3d(rot.getX(), rot.getY(), rot.getZ()));
                }
                offset.multiply(1 / 20d);

                if (Keyboard.isKeyDown(KeyEvent.VK_ESCAPE))
                    System.exit(0);


                rotX.getAndAdd(MouseInfo.getPointerInfo().getLocation().x - lastMousePosX.get());
                rotY.getAndAdd(MouseInfo.getPointerInfo().getLocation().y - lastMousePosY.get());

                robot.mouseMove(window.getWindow().getLocationOnScreen().x + window.xSize.get() / 2, window.getWindow().getLocationOnScreen().y + window.ySize.get() / 2);

                lastMousePosX.set(MouseInfo.getPointerInfo().getLocation().x);
                lastMousePosY.set(MouseInfo.getPointerInfo().getLocation().y);

                setCameraPosition(offset, cam);
                position.set(offset);
                if (rotY.get() / 5 < -90)
                    rotY.set(-90 * 5);
                if (rotY.get() / 5 > 90)
                    rotY.set(90 * 5);
                setCameraRotation(new Vector2d((double) rotX.get() / 5, (double) rotY.get() / 5), cam);
                offset.multiply(20);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        int j = 0;

        while(true) {
            j++;
            projection.setFOVMod(1);

            int finalJ = j;
            window.render((ag, g, i) -> {


                BufferedImage img = new BufferedImage(2,2,BufferedImage.TYPE_INT_RGB);
                img.setRGB(0,0, 0xffff0000); img.setRGB(1,0, 0xff00ff00);
                img.setRGB(0,1, 0xffffffff); img.setRGB(1,1, 0x0000ff);

                img = Maths2D.distortImage(img, 2,2, 1);

                img = Maths2D.addImageBorder(img, new Vector2d(2, 2), new Vector2d(5, 5), 0x8000ff00);

                renderTexturedRectangle(
                        new Vector3d(-2,1,0),
                        new Vector2d(4,4),
                        new Vector3d(0, finalJ,0),
                        new Vector3d(0,0,0),
                        cam,
                        projection,
                        img
                );

                img = Maths2D.cropImage(img, new Vector2d(1,1), new Vector2d(3,3));

                Vector3d finalPosition = position.clone();
                renderTexturedRectangle(
                        finalPosition.clone().negate().add(new Vector3d(0,0,500)),
                        new Vector2d(40,40),
                        new Vector3d(finalJ / 50d, 0,0),
                        finalPosition.clone().negate(),
                        cam,
                        projection,
                        img
                );
                renderTexturedRectangle(
                        finalPosition.clone().negate().add(new Vector3d(0,0,-500)),
                        new Vector2d(40,40),
                        new Vector3d(finalJ / 50d, 0,0),
                        finalPosition.clone().negate(),
                        cam,
                        projection,
                        img
                );

                projection.setColor(0xffff0000);
                projection.fillRectangle(
                        applyRot(new Vector3d(-1, -1, 0)),
                        applyRot(new Vector3d(-1, 1, 0)),
                        applyRot(new Vector3d(1, 1, 0)),
                        applyRot(new Vector3d(1, -1, 0))
                );

                ag.drawImage(0, 0, (BufferedImage) projection.render());
            });

            window.prepareRender();
            window.doRender();
            window.swapBuffers();
            Thread.sleep(10);
        }
    }

    private static Vector3d applyRot(Vector3d vec) {
        return translate(vec, cam);
    }
}
