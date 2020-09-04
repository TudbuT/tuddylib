package de.tudbut;

import de.tudbut.ui.windowgui.RenderableWindow;

public class T3 {
    public static void main(String[] ignored) throws InterruptedException {
        RenderableWindow window = new RenderableWindow(500, 500, "hi", 20, true);
    
        Thread.sleep(1000);
        for (int i = 0; i < 1; i++) {
            window.render((ag, g, img) -> g.fillRect(0,0,50,50));
            window.prepareRender();
            window.doRender();
            window.swapBuffers();
        }
        System.out.println("x");
    }
}
