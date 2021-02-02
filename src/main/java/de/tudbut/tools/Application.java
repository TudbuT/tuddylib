package de.tudbut.tools;

import tudbut.logger.Logger;
import de.tudbut.window.Window;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.atomic.AtomicBoolean;


public class Application {
    protected Window window = null;
    private boolean hasConfig = false;
    private Config config;
    private boolean main = false;
    private String label = "";
    private Logger logger = null;


    protected void activate(String name, boolean hasCfg, boolean hasWindow, boolean isMain) throws Exception {
        this.hasConfig = hasCfg;
        this.label = name;
        this.main = isMain;
        this.logger = new Logger(name);

        if (hasWindow) this.window = new Window(name, name, isMain).setSize(100, 100).open();

        if (hasCfg) this.config = new Config(name.replaceAll(" ", "_") + ".config");
    }

    public String toString() {
        return label;
    }

    protected Config getConfig() {
        if (this.hasConfig) return this.config;
        else return null;
    }

    protected void setConfig(Config c) {
        this.config = c;
        this.hasConfig = true;
    }

    public Logger getLogger() {
        return this.logger;
    }

    protected String input(String name) {
        return JOptionPane.showInputDialog(name);
    }

    protected void output(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    @Deprecated
    protected boolean askD(String question) {
        this.logger.info("Showing question dialog");

        AtomicBoolean received = new AtomicBoolean(false);
        AtomicBoolean yes = new AtomicBoolean(false);

        Window askWindow = new Window(this.label, this.label + " - " + question, false).setText(question).setSize(400, 150).open();


        JButton askButton_yes = new JButton("Yes");
        JButton askButton_no = new JButton("No");
        askWindow.label.add(askButton_yes);
        askWindow.label.add(askButton_no);

        askButton_yes.setLocation(50, 70);
        askButton_no.setLocation(250, 70);
        askButton_yes.setSize(100, 25);
        askButton_no.setSize(100, 25);

        askWindow.frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {
                received.set(true);
                yes.set(false);
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

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {
            }
        });

        askButton_yes.addActionListener((a) -> {
            yes.set(true);
            received.set(true);
        });

        askButton_no.addActionListener((a) -> {
            yes.set(false);
            received.set(true);
        });

        askWindow.label.updateUI();

        while (true) {
            if (received.get()) break;
        }

        this.logger.info("Answer received");

        if (yes.get()) {
            askWindow.delete();
            return true;
        }
        else {
            askWindow.delete();
            return false;
        }
    }

    protected boolean ask(String question) {
        return JOptionPane.showConfirmDialog(null,
                                             question, label, JOptionPane.YES_NO_OPTION
        ) ==
               JOptionPane.YES_OPTION;
    }
}
