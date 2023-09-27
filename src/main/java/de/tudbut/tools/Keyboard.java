package de.tudbut.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Keyboard implements KeyListener, AWTEventListener {
    private static final Map<Integer, Boolean> keys = new HashMap<>();
    private static JFrame frame;
    private static int globListeners = 0;
    private static Keyboard glob = null;
    private static final ArrayList<KeyListener> keyListeners = new ArrayList<>();

    public static boolean isKeyDown(int keyCode) {
        return keys.get(keyCode) != null && keys.get(keyCode);
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
    
    public static void startListeningGlobally() {
        if(globListeners == 0) {
            Keyboard listener = new Keyboard();
            Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK);
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
    
    public static void addListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    public static void bindToMouse() {
        Mouse.startListening(frame);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        for (int i = 0 ; i < keyListeners.size() ; i++) {
            keyListeners.get(i).keyTyped(keyEvent);
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        keys.put(ke.getKeyCode(), true);
        for (int i = 0 ; i < keyListeners.size() ; i++) {
            keyListeners.get(i).keyPressed(ke);
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        keys.put(ke.getKeyCode(), false);
        for (int i = 0 ; i < keyListeners.size() ; i++) {
            keyListeners.get(i).keyReleased(ke);
        }
    }
    
    @Override
    public void eventDispatched(AWTEvent awtEvent) {
        System.out.println("yt");
        if(awtEvent instanceof KeyEvent) {
            KeyEvent keyEvent = ( (KeyEvent) awtEvent );
            if(keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                keyPressed(keyEvent);
            }
            if(keyEvent.getID() == KeyEvent.KEY_RELEASED) {
                keyReleased(keyEvent);
            }
        }
    }
}
