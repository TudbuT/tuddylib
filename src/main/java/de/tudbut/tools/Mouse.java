package de.tudbut.tools;

import tudbut.obj.Vector2i;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class Mouse implements MouseListener, MouseWheelListener, AWTEventListener {
    private static final Map<Integer, Boolean> keys = new HashMap<>();
    private static JFrame frame;
    private static int mouseWheelPosition = 0;
    private static int globListeners = 0;
    private static Mouse glob = null;

    public static boolean isKeyDown(int keyCode) {
        return keys.get(keyCode) != null && keys.get(keyCode);
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
    
    public static void startListeningGlobally() {
        if(globListeners == 0) {
            Mouse listener = new Mouse();
            Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK + AWTEvent.MOUSE_WHEEL_EVENT_MASK);
            glob = listener;
        }
        globListeners++;
    }
    
    public static void stopListeningGlobally() {
        globListeners--;
        if(globListeners == 0) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(glob);
        }
        if(globListeners < 0)
            globListeners = 0;
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
    
    @Override
    public void eventDispatched(AWTEvent awtEvent) {
        if(awtEvent instanceof MouseEvent) {
            MouseEvent mouseEvent = ( (MouseEvent) awtEvent );
            if(mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
                mousePressed(mouseEvent);
            }
            if(mouseEvent.getID() == MouseEvent.MOUSE_RELEASED) {
                mouseReleased(mouseEvent);
            }
        }
        if(awtEvent instanceof MouseWheelEvent) {
            MouseWheelEvent mouseEvent = ( (MouseWheelEvent) awtEvent );
            mouseWheelMoved(mouseEvent);
        }
    }
}
