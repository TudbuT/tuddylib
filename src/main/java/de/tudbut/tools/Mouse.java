package de.tudbut.tools;

import de.tudbut.type.Vector2d;
import tudbut.obj.Vector2i;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

public class Mouse implements MouseListener, MouseWheelListener {
    private static final Map<Integer, Boolean> keys = new HashMap<>();
    private static JFrame frame;
    private static int mouseWheelPosition = 0;


    public static boolean isKeyDown(int keyCode) {
        return keys.get(keyCode) == null ? false : keys.get(keyCode);
    }

    public static int getMouseWheelPos() {
        return mouseWheelPosition;
    }

    public static void startListening(boolean trap) {
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(0, 0);
        frame.setTitle("MouseListener");
        new Thread(() -> {
            while (true) {
                frame.setSize(100, 100);
                frame.setLocation(0, 0);
                if (trap) {
                    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    frame.requestFocus();
                }
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        frame.addMouseListener(new Mouse());
        frame.addMouseWheelListener(new Mouse());
    }

    public static void startListening(JFrame theFrame) {
        theFrame.addMouseListener(new Mouse());
        theFrame.addMouseWheelListener(new Mouse());
    }

    public static void bindToKeyboard() {
        Keyboard.startListening(frame);
    }
    
    public static Point getMousePoint() {
        return MouseInfo.getPointerInfo().getLocation();
    }
    
    public static Vector2i getMousePos() {
        Point p = getMousePoint();
        return new Vector2i((int) p.getX(), (int) p.getY());
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        keys.put(mouseEvent.getButton(), true);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        keys.put(mouseEvent.getButton(), false);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        mouseWheelPosition -= mouseWheelEvent.getWheelRotation();
    }
}
