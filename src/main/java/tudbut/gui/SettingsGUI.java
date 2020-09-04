package tudbut.gui;

import de.tudbut.tools.FileRW;
import de.tudbut.tools.Tools;
import de.tudbut.window.Window;
import tudbut.global.DebugStateManager;
import tudbut.gui.settingsgui.Setting;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsGUI {
    public final ArrayList<Setting> settings = new ArrayList<>();
    public final ArrayList<Runnable> changeListeners = new ArrayList<>();
    private final boolean save;
    private final Window window;
    private final String name;

    public SettingsGUI(boolean saveToFile, String name) {
        this.save = saveToFile;
        window = new Window(name, name, false);
        this.name = name;

        if (saveToFile)
            loadFromFile();
    }

    public void open() {
        window.setSize(500, 500);
        window.open();

        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);
            for (int j = 0; j < i; j++) {
                Setting s = settings.get(j);
                if (s.id.equals(setting.id)) {
                    setting = s;
                    settings.remove(i);
                    i = j;
                }
            }
            JButton button = new JButton(
                    setting.name + (
                            setting.type != Setting.Type.BUTTON ? (
                                    ": " + (
                                            setting.value ?
                                            setting.type == Setting.Type.ON_OFF ? "ON" : "YES" :
                                            setting.type == Setting.Type.ON_OFF ? "OFF" : "NO"
                                    )
                            ) : ""
                    )
            );

            Setting finalSetting = setting;
            button.addActionListener(actionEvent -> {
                finalSetting.value = !finalSetting.value;
                button.setText(
                        finalSetting.name + (
                                finalSetting.type != Setting.Type.BUTTON ? (
                                        ": " + (
                                                finalSetting.value ?
                                                finalSetting.type == Setting.Type.ON_OFF ? "ON" : "YES" :
                                                finalSetting.type == Setting.Type.ON_OFF ? "OFF" : "NO"
                                        )
                                ) : ""
                        )
                );
                for (Runnable changeListener : finalSetting.onChange) {
                    changeListener.run();
                }
                for (Runnable changeListener : changeListeners) {
                    changeListener.run();
                }
                if (save) {
                    try {
                        saveSettings();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            button.setLocation(0, i * 25);
            button.setSize(500, 25);
            window.frame.addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent componentEvent) {
                    button.setSize(window.frame.getSize().width, 25);
                }

                @Override
                public void componentMoved(ComponentEvent componentEvent) {
                }

                @Override
                public void componentShown(ComponentEvent componentEvent) {
                }

                @Override
                public void componentHidden(ComponentEvent componentEvent) {
                }
            });
            button.setVisible(true);
            window.frame.add(button);
        }
    }

    public void saveSettings() throws IOException {
        FileRW file = new FileRW("settings_" + name + ".map");
        file.setContent(Tools.mapToString(toMap()).replaceAll(";", ";\n"));
    }

    public void loadFromFile() {
        if (Files.exists(Paths.get("settings_" + name + ".map"))) {
            try {
                fromMap(Tools.stringToMap(new FileRW("settings_" + name + ".map").getContent().join("")));
            }
            catch (Exception e) {
                DebugStateManager.getDebugDLogger("Setting parser").subChannel(name).error("Couldn't read settings! Resetting them.");
                try {
                    new FileRW("settings_" + name + ".map").setContent(":ON_OFF@%I false;");
                }
                catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (Setting setting : settings) {
            map.put(setting.id, setting.type + "@" + setting.name + ": " + setting.value);
        }
        return map;
    }

    public void fromMap(Map<String, String> map) {
        for (String key : map.keySet()) {
            try {
                Setting setting = new Setting();
                setting.id = key;
                String value = map.get(key);

                setting.type = Setting.Type.valueOf(value.split("@")[0]);
                value = value.split("@")[1];
                setting.name = value.split(": ")[0];
                value = value.split(": ")[1];
                setting.value = Boolean.parseBoolean(value);

                settings.add(setting);
            }
            catch (Exception e) {
                DebugStateManager.getDebugDLogger("Setting parser").subChannel(name).warn("Skipping bad setting: " + key);
            }
        }
    }
}
