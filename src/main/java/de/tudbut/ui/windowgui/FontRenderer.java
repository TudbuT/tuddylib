package de.tudbut.ui.windowgui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;

public class FontRenderer {
    Font myFont;
    FontRenderContext context;

    public FontRenderer(int size, Graphics graphics) {
        myFont = new Font("DejaVu Sans", Font.PLAIN, size);
        graphics.setFont(myFont);
        context = graphics.getFontMetrics().getFontRenderContext();
    }

    public int getTextWidth(String text) {
        int r = 0;

        for (int i = 0; i < text.split("\n").length; i++)
            r += ((int) myFont.getStringBounds(text.split("\n")[i], context).getWidth()) + 4;

        return r;
    }

    public int getTextHeight(String text) {
        int r = 0;

        for (int i = 0; i < text.split("\n").length; i++)
            r += ((int) myFont.getStringBounds(text.split("\n")[i], context).getHeight()) + 4;

        return r;
    }

    public Image renderText(String text, int color) {
        BufferedImage image = new BufferedImage(getTextWidth(text), getTextHeight(text), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(new Color(color));
        graphics.setFont(myFont);
        for (int i = 0; i < text.split("\n").length; i++)
            graphics.drawString(text.split("\n")[i], 0, (myFont.getSize() + 4) * (i + 1));

        return image;
    }
}
