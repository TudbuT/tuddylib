package de.tudbut.window;

import tudbut.logger.LoggerSink;
import tudbut.global.DebugStateManager;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.atomic.AtomicReference;

public class Window {
    private final AtomicReference<LoggerSink> logger;
    public JFrame frame = new JFrame();
    public JLabel label = new JLabel();
    private String text;
    private final boolean isMain;
    private int exitValue = 0;
    private String name;
    private String title;
    private boolean isClosed = true;
    private final boolean messages = true;

    public Window(String name, String label, boolean isMain) {
        this.logger = DebugStateManager.debugLoggerReference();
        this.frame.setName(name);
        this.label.setName(name);
        this.frame.setTitle(label);
        if (isMain) {
            this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.isMain = true;
        }
        else {
            this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.isMain = false;
        }
        this.label.setVerticalAlignment(1);
        this.frame.getContentPane().add(this.label);
        this.name = name;
        this.title = label;
        this.frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {
                Window.this.isClosed = true;
                Window.this.logger.get().info("Window was closed");
                if (Window.this.isMain) Window.this.delete();
                else Window.this.close();
            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {
            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {
            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {
            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {
            }

        });
        this.logger.get().info("Created window");

        new Timer(100, (ignore) -> {
            frame.repaint();
            this.label.repaint();
            this.label.updateUI();
            frame.repaint();
            this.label.repaint();
        });
    }

    public Window setText(String string) {
        text = string;
        this.label.setText("<html>" + string.replaceAll("[\\\\\\n]", "<br />") + "</html>");
        this.logger.get().info("Set text to '" + string + "'");
        return this;
    }

    public Window setExitValue(int v) {
        this.exitValue = v;
        this.logger.get().info("Set exit value to " + v);
        return this;
    }

    public Window addText(String string) {
        text += string;
        this.label.setText("<html>" +
                           text.replaceAll("[\\\\\\n]", "<br />") + "</html>");
        this.logger.get().info("Added text '" + string + "'");
        return this;
    }

    public Window open() {
        this.frame.setVisible(true);
        this.logger.get().info("Opened window");
        this.isClosed = false;
        return this;
    }

    public Window close() {
        this.frame.setVisible(false);
        this.logger.get().info("Closed window");
        this.isClosed = true;
        return this;
    }

    public Window setSize(int x, int y) {
        this.frame.setSize(x, y);
        this.logger.get().info("Resized window");
        return this;
    }

    public Window delete() {
        this.frame.dispose();
        this.isClosed = true;
        if (this.isMain) {
            this.logger.get().info("Deleted window");
            logger.get().warn("EXITING - Window '" + this.name + "' was deleted");
            System.exit(this.exitValue);
        }
        return this;
    }

    public Window restore() {
        Window restoredWindow = new Window(this.name, this.title, this.isMain);
        restoredWindow.setText(this.label.getText()).setSize(this.frame.getWidth(), this.frame.getHeight());
        return restoredWindow;
    }

    public boolean isClosed() {
        return this.isClosed;
    }
}
