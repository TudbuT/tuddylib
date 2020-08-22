package de.tudbut.ui.windowgui;

import de.tudbut.logger.GlobalLogger;

@Deprecated
public class WindowBackgroundColor {

    public String Scolor;
    public int Icolor;

    public WindowBackgroundColor(String scolor, int icolor) {
        if (!checkColor(scolor)) {
            GlobalLogger.warn("Color not valid!");
            return;
        }
        this.Scolor = scolor;
        this.Icolor = icolor;
    }

    public String toHTML() {
        return "<html><style>html { background-color: " + Scolor + "; }</style></html>";
    }

    private boolean checkColor(String hex) {
        if (hex.startsWith("#")) {
            return hex.length() == "#000000".length();
        }
        if (hex.startsWith("0x")) {
            return hex.length() == "0x000000".length();
        }
        return false;
    }
}
