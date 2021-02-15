package de.tudbut.ui.windowgui;

import de.tudbut.tools.Keyboard;
import de.tudbut.tools.Mouse;
import de.tudbut.type.*;
import tudbut.obj.Vector2i;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RenderableWindow {
    public BufferedImage image;
    public AtomicInteger xSize = new AtomicInteger(1);
    public AtomicInteger ySize = new AtomicInteger(1);
    protected BufferedImage buffer;
    protected BufferedImage bufferTMP;
    private final AtomicInteger fps = new AtomicInteger();
    private final JFrame window;
    private final JLabel label;
    private RenderRunnable[] toRender = new RenderRunnable[1024];
    private RenderRunnable[] toRenderTMP = new RenderRunnable[1024];
    private final AtomicLong frameAt = new AtomicLong(new Date().getTime());
    private final AtomicLong lastFrameAt = new AtomicLong(new Date().getTime());
    private short nextRenderRunnable = 0;
    private final int tryFps;
    
    public RenderableWindow(int x, int y, String name, int tryFps, boolean showFPS) {
        this(x,y,name,tryFps,showFPS,true);
    }
    
    public RenderableWindow(int x, int y, String name, int tryFps, boolean showFPS, boolean decorated) {
        this.tryFps = tryFps;
        window = new JFrame(name);
        image = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
        window.setUndecorated(!decorated);
        window.setVisible(true);
        window.setSize(x, y);
        label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics g2 = g.create();
                g2.drawImage(buffer, 0, 0, window);
                g2.drawImage(image, 0, 0, window);
                g2.dispose();
            }
        };
        window.add(label);
        Keyboard.startListening(window);
        Mouse.startListening(window);
        Timer timer = new Timer(1000 / tryFps, e -> {
            label.repaint();
            xSize.set(window.getSize().width);
            ySize.set(window.getSize().height);
            if (showFPS)
                window.setTitle(name + " | " + fps.get() + " FPS");
        });
        timer.start();
        label.repaint();
        window.repaint();
    }
    
    public void calcInsets(int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            Vector2i sizeOnScreen = getSize().add(getInsets().multiply(2));
            window.setSize(
                    sizeOnScreen.getX(),
                    sizeOnScreen.getY()
            );
        }).start();
    }
    
    public Vector2i getMousePos() {
        Vector2i pos = Mouse.getMousePos();
        Insets insets = window.getInsets();
        return new Vector2i(
                (int) - window.getLocation().getX() - insets.left,
                (int) - window.getLocation().getY() - insets.top
        ).add(pos);
    }
    
    public boolean mouseIsBetween(Vector2i pos0, Vector2i pos1) {
        Vector2i mousePos = getMousePos();
        return
                mousePos.getX() > pos0.getX() &&
                mousePos.getY() > pos0.getY() &&
                mousePos.getX() < pos1.getX() &&
                mousePos.getY() < pos1.getY();
    }
    
    public boolean mouseInRangeFrom(Vector2i pos, Vector2i range) {
        Vector2i mousePos = getMousePos();
        return
                mousePos.getX() > pos.getX() &&
                mousePos.getY() > pos.getY() &&
                mousePos.getX() < pos.getX() + range.getX() &&
                mousePos.getY() < pos.getY() + range.getY();
    }
    
    public Vector2i getSizeOnScreen() {
        return
                new Vector2i(xSize.get(), ySize.get())
                        .negate()
                        .add(
                                new Vector2i(
                                        window.getInsets().left + window.getInsets().right,
                                        window.getInsets().top + window.getInsets().bottom
                                )
                        )
                        .negate();
    }
    
    public Vector2i getInsets() {
        return new Vector2i(
                window.getInsets().left + window.getInsets().right,
                window.getInsets().top + window.getInsets().bottom
        );
    }
    
    public Vector2i getSize() {
        return
                new Vector2i(xSize.get(), ySize.get());
    }
    
    public int getWindowBarHeight() {
        return window.getInsets().top;
    }
    
    public int getFps() {
        return fps.get();
    }
    
    public JFrame getWindow() {
        return window;
    }
    
    public JLabel getLabel() {
        return label;
    }
    
    public void render(RenderRunnable renderRunnable) {
        toRenderTMP[nextRenderRunnable] = renderRunnable;
        nextRenderRunnable++;
    }
    
    public void prepareRender() {
        nextRenderRunnable = 0;
        if (toRenderTMP[0] != null)
            toRender = toRenderTMP;
        toRenderTMP = new RenderRunnable[1024];
    }
    
    public void doRender() {
        BufferedImage i = new BufferedImage(xSize.get(), ySize.get(), BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = i.getGraphics();
        AdaptedGraphics ag = new AdaptedGraphics(g);
        
        for (RenderRunnable r : toRender) {
            if (r == null)
                break;
            
            r.render(ag, g, image);
        }
        
        bufferTMP = i;
    }
    
    @FInfo(s = "aka. finishRender")
    public void swapBuffers() {
        frameAt.set(new Date().getTime());
        try {
            fps.set((int) (1000 / (frameAt.get() - lastFrameAt.get())));
        }
        catch (ArithmeticException ignore) {
            fps.set(tryFps);
        }
        lastFrameAt.set(new Date().getTime());
        BufferedImage var1 = buffer;
        buffer = bufferTMP;
        bufferTMP = var1;
    }
    
    public interface RenderRunnable {
        void render(AdaptedGraphics ag, Graphics gr, BufferedImage img);
    }
}
