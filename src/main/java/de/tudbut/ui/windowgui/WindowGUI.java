package de.tudbut.ui.windowgui;

import de.tudbut.logger.Logger;
import de.tudbut.tools.Config;
import de.tudbut.window.Window;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;

@Deprecated
public class WindowGUI {
    public static String PATH = "tmp/gui";
    private static long nextWindowID = 0;
    private int nextActionListenerID = 0;
    private final int nextDesignID = 0;
    private final ActionListener[] listeners = new ActionListener[1023];
    private final Logger logger = new Logger("WindowGUI");
    //private static Design[] designs = new Design[1024];

    protected Config create(String name) throws Exception {
        if (!Files.exists(Paths.get(PATH))) {
            Files.createDirectory(Paths.get("tmp"));
            Files.createDirectory(Paths.get(PATH));
        }

        if (!Files.exists(Paths.get(PATH + "/" + nextWindowID))) {
            Files.createDirectory(Paths.get(PATH + "/" + nextWindowID));
            Files.createDirectory(Paths.get(PATH + "/" + nextWindowID + "/button"));
        }
        Config r = new Config(PATH + "/" + nextWindowID + ".window");
        r.set("name", name);
        r.set("id", String.valueOf(nextWindowID));
        //r.set("design", "null");
        r.set("nextButtonY", "0");
        r.set("nextButtonID", "0");
        r.set("buttons", "0");
        nextWindowID++;
        return r;
    }

    protected Config addButton(
            Config window, String text, int sizeX,
            int sizeY
    ) throws Exception {
        int nextButtonY = Integer.parseInt(window.get("nextButtonY"));
        int nextButtonID = Integer.parseInt(window.get("nextButtonID"));
        Config r = new Config(PATH + "/" + window.get("id") + "/button/" + nextButtonID + ".button");
        r.set("text", text);
        r.set("id", nextButtonID + "");
        r.set("posX", "0");
        r.set("posY", nextButtonY + "");
        r.set("sizeX", sizeX + "");
        r.set("sizeY", sizeY + "");
        r.set("windowID", window.get("id"));
        r.set("listenerID", "x");
        r.set("invisible", "0");
        window.set("buttons", (Integer.parseInt(window.get("buttons")) + 1) + "");
        nextButtonY += sizeY + 2;
        nextButtonID++;
        window.set("nextButtonY", nextButtonY + "");
        window.set("nextButtonID", nextButtonID + "");
        return r;
    }

    protected void delButton(Config button, Config window) throws Exception {
        int nextButtonY = Integer.parseInt(window.get("nextButtonY"));

        nextButtonY -= Integer.parseInt(button.get("sizeY")) + 2;

        button.set("invisible", "1");

        window.set("nextButtonY", nextButtonY + "");
    }

    protected void setButtonVisibility(Config button, boolean visible)
            throws Exception {
        button.set("invisible", !visible ? "1" : "0");
    }


    protected void addButtonListener(Config button, ActionListener listener) throws Exception {
        listeners[nextActionListenerID] = listener;
        button.set("listenerID", nextActionListenerID + "");
        nextActionListenerID++;
    }

    /*protected void addDesign(Config window, Design design) throws Exception {
        window.set("design", nextDesignID + "");
        designs[nextDesignID] = design;
        nextDesignID++;
    }

    public void setBackground(Window window, WindowBackgroundColor background) {
        window.addText(background.toHTML());
    }*/

    protected void render(Config window, Window w) throws Exception {
        logger.info("Preparing render...");
        w.label.removeAll();
        w.frame.repaint();
        logger.info("Started render...");

        JButton[] buttons = new JButton[Integer.parseInt(window.get("buttons"))];
        for (int i = 0; i < buttons.length; i++) {
            Config config = new Config(PATH + "/" + window.get("id") + "/button/" + i + ".button");
            JButton button = new JButton(config.get("text"));
            button.setLocation(Integer.parseInt(config.get("posX")), Integer.parseInt(config.get("posY")));
            button.setSize(Integer.parseInt(config.get("sizeX")), Integer.parseInt(config.get("sizeY")));
            if (!config.get("listenerID").equals("x"))
                if (listeners[Integer.parseInt(config.get("listenerID"))] != null)
                    button.addActionListener(listeners[Integer.parseInt(config.get("listenerID"))]);

            if (config.get("invisible").equals("1")) {
                button.setVisible(false);
                button.setSize(0, 0);
                button.setEnabled(false);
            }
            buttons[i] = button;
        }

        logger.info("Finishing render...");

        for (JButton button : buttons) {
            w.label.add(button);
        }

        w.frame.setTitle(window.get("name"));

        logger.info("Finished! Updating...");

        w.label.updateUI();
        w.frame.repaint();

        logger.info("Rendered!");

        /*if(!window.get("design").equals("null")) {
            w.addText(designs[Integer.parseInt(window.get("design"))].toString());
            w.frame.setBackground(new Color(designs[Integer.parseInt(window.get("design"))].getBg().Icolor));
        }*/
    }
}
