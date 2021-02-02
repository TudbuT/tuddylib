package de.tudbut.ui.windowgui;

import de.tudbut.type.Vector2d;
import tudbut.obj.Vector2i;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;

public class FontRenderer {
    Font myFont;
    FontRenderContext context;

    public FontRenderer(int size) {
        myFont = new Font("serif", Font.PLAIN, size);
        context = new FontRenderContext(null, true, false);
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
    
    private static Rectangle getStringBounds(String s, Font font, FontRenderContext context) {
        GlyphVector gv = font.createGlyphVector(context, s);
        return gv.getPixelBounds(context, 0, 0);
    }

    public Image renderText(String text, int color) {
        BufferedImage image = new BufferedImage(getTextWidth(text), getTextHeight(text), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = ((Graphics2D) image.getGraphics());
        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics.setColor(new Color(color));
        graphics.setFont(myFont);
        for (int i = 0; i < text.split("\n").length; i++)
            graphics.drawString(text.split("\n")[i], 0, (myFont.getSize() + 4) * (i + 1));

        return image;
    }
    
    public Vector2i getCoordsForCentered(Vector2i coords, String text) {
        return
                new Vector2i(
                        coords.getX() - getTextWidth(text) / 2,
                        coords.getY() - getTextHeight(text) / 2
                )
        ;
    }
}
