package de.tudbut;

import de.tudbut.tools.Mouse;
import de.tudbut.type.Vector3d;
import de.tudbut.ui.windowgui.RenderableWindow;
import tudbut.rendering.Graph;
import tudbut.rendering.GraphRenderer;
import tudbut.rendering.Maths2D;
import tudbut.tools.MappableIO;

import javax.swing.*;

public class T3 {
    public static void main(String[] ignored) throws InterruptedException {
        GraphRenderer graphRenderer = new GraphRenderer();
    
        RenderableWindow window = new RenderableWindow(500,500,"t", 20, true);
        window.getWindow().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.calcInsets(500);
        Graph graph = x -> x;
        graphRenderer.setOffsetX(10);
        graphRenderer.setOffsetY(10);
        
        double scale = 1;
        while (true) {
            double newScale = Mouse.getMouseWheelPos() / 10d + 1;
            if(newScale > 0) {
                scale = newScale;
            }
            else {
                newScale = Mouse.getMouseWheelPos() / 20d + 0.5;
                if(newScale > 0) {
                    scale = newScale;
                }
            }
            graphRenderer.setScale(scale);
            
            window.render(((ag, gr, img) -> {
                gr.drawImage(Maths2D.distortImage(graphRenderer.render(graph, 250, 250, true), 500,500,1), 0, 0, null);
            }));
            window.prepareRender();
            window.doRender();
            window.swapBuffers();
            Thread.sleep(100);
        }
    }
}
