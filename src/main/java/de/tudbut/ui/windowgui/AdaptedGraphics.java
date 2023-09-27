package de.tudbut.ui.windowgui;

import java.awt.*;

public class AdaptedGraphics {
    private final Graphics graphics;

    public AdaptedGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public void setColor(int r, int g, int b) {
        this.graphics.setColor(new Color(r, g, b));
    }

    public void setColor(int rgb) {
        if(rgb >= 0 && rgb <= 0xffffff)
            this.graphics.setColor(new Color(rgb, false));
        else
            this.graphics.setColor(new Color(rgb, true));
    }

    public Graphics g() {
        return graphics;
    }

    public void drawImage(int x, int y, Image image) {
        graphics.drawImage(image, x, y, null);
    }
}
