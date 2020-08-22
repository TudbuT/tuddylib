package de.tudbut.tools;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Keyboard implements KeyListener {
    private static final Map<Integer, Boolean> keys = new HashMap<>();
    private static JFrame frame;

    public static boolean isKeyDown(int keyCode) {
        return keys.get(keyCode) == null ? false : keys.get(keyCode);
    }

    public static void startListening(boolean trap) {
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(0, 0);
        frame.setTitle("KeyboardListener");
        new Thread(() -> {
            while (true) {
                frame.setSize(0, 0);
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
        frame.addKeyListener(new Keyboard());
    }

    public static void startListening(JFrame frame) {
        frame.addKeyListener(new Keyboard());
    }

    public static void bindToMouse() {
        Mouse.startListening(frame);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        keys.put(ke.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        keys.put(ke.getKeyCode(), false);
    }
}
