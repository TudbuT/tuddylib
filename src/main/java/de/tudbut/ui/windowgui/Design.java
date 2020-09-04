package de.tudbut.ui.windowgui;

@Deprecated
public class Design {

    private WindowBackgroundColor bg;
    private String css = "";


    public WindowBackgroundColor getBg() {
        return bg;
    }

    public void setBGColor(WindowBackgroundColor bg) {
        this.bg = bg;
    }

    public void addCSS(String css) {
        this.css += "<html><style>" + css + "</style></html>";
    }

    public String toString() {
        return this.css + bg.toHTML();
    }
}
