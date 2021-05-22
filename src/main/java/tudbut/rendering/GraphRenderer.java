package tudbut.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphRenderer {
    
    double scale = 1;
    int offsetX = 0;
    int offsetY = 0;
    double scaleY;
    
    public GraphRenderer() {
    
    }
    
    public void setScaleX(double scale) {
        if(scale <= 0)
            throw new IllegalArgumentException();
        
        this.scale = 1 / scale;
    }
    
    public void setScaleY(double scale) {
        if(scale <= 0)
            throw new IllegalArgumentException();
    
        this.scaleY = scale;
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
    
        Graphics graphics = image.getGraphics().create();
        graphics.setColor(new Color(0x000000));
        
        int lastPixelY = Maths2D.center(Maths2D.camera((int) Math.round(-graph.getAsFunction(-Maths2D.center(Maths2D.camera(0, offsetX), pxX) * scale) * scaleY), offsetY), pxY);
        if(markZero)
            graphics.drawRect(Maths2D.center(Maths2D.camera(0, offsetX), pxX) - 1, Maths2D.center(Maths2D.camera(0, offsetY), pxY) - 1, 2, 2);
        for (int i = -Maths2D.center(Maths2D.camera(0, offsetX), pxX) ; i < Maths2D.center(Maths2D.camera(0, offsetX), pxX) * 2; i++) {
            int y = Maths2D.center(Maths2D.camera((int) Math.round(-graph.getAsFunction(i * scale) * scaleY), offsetY), pxY);
            graphics.drawLine(Maths2D.center(Maths2D.camera(i, offsetX), pxX) - 1, lastPixelY, Maths2D.center(Maths2D.camera(i, offsetX), pxX), y);
            lastPixelY = y;
        }
        
        return image;
    }
}
