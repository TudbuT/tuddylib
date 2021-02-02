package tudbut.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphRenderer {
    
    double scale = 1;
    int offsetX = 0;
    int offsetY = 0;
    
    public GraphRenderer() {
    
    }
    
    public void setScale(double scale) {
        if(scale <= 0)
            throw new IllegalArgumentException();
        
        this.scale = scale;
    }
    
    public void setOffsetX(int offset) {
        this.offsetX = offset;
    }
    
    public void setOffsetY(int offset) {
        this.offsetY = offset;
    }
    
    public BufferedImage render(Graph graph, int pxX, int pxY, boolean markZero) {
        BufferedImage image = new BufferedImage(pxX, pxY, BufferedImage.TYPE_INT_RGB);
    
        for (int y = 0; y < pxY; y++) {
            for (int x = 0; x < pxX; x++) {
                image.setRGB(x,y, 0xffffff);
            }
        }
    
        Graphics graphics = image.getGraphics().create(offsetX, -offsetY, pxX - offsetX, pxY - offsetY);
        graphics.setColor(new Color(0x000000));
        
        int lastPixelY = (int) (-graph.getAsFunction(0 * scale) + pxY);
        if(markZero)
            graphics.drawRect(0, 0, 1, 1);
        for (int i = offsetX; i < pxX; i++) {
            int y = (int) (-graph.getAsFunction(i * scale) + pxY);
            graphics.drawLine(i - 1, lastPixelY, i, y);
            lastPixelY = y;
        }
        
        return image;
    }
}
