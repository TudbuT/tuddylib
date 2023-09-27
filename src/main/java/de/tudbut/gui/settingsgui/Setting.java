package de.tudbut.gui.settingsgui;

import java.util.ArrayList;

public class Setting {
    public final ArrayList<Runnable> onChange = new ArrayList<>();
    public Type type = Type.ON_OFF;
    public String name = "";
    public String id = "";
    public boolean value = false;

    public enum Type {
        ON_OFF,
        YES_NO,
        BUTTON
    }
}
