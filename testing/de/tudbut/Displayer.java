package de.tudbut;

import de.tudbut.io.StreamReader;
import de.tudbut.ui.windowgui.RenderableWindow;
import tudbut.rendering.Maths2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Displayer {
    
    public static void main(String[] args) throws IOException {
        
        final int LENGTH = 500;
        
        ZipInputStream stream = new ZipInputStream(new FileInputStream("frames.t2f"));
    
        StreamReader.BUFFER_SIZE = 8192 * 2 * 2;
        
        BufferedImage[] images = new BufferedImage[LENGTH];
        Map<Integer, InputStream> streams = new HashMap<>();
        for (int i = 0; i < LENGTH; i++) {
            ZipEntry entry = stream.getNextEntry();
            streams.put(Integer.parseInt(entry.getName().split("\\.")[0]), new ByteArrayInputStream(new StreamReader(stream).readAllAsBytes()));
            stream.closeEntry();
        }
        
        for (int i = 0; i < LENGTH; i++) {
            images[i] = ImageIO.read(streams.get(i));
        }
        
        RenderableWindow window = new RenderableWindow(1000,500,"hi", 20, true);
        
        while (true)
            for (int i = 0; i < LENGTH; i++) {
                long sa = new Date().getTime();
                int finalI = i;
                window.render((ag, gr, img) -> {
                    ag.drawImage(0,0, Maths2D.distortImage(images[finalI], window.xSize.get(), window.ySize.get() - window.getWindowBarHeight(), 1));
                });
                window.prepareRender();
                window.doRender();
                window.swapBuffers();
                
                try {
                    Thread.sleep(200 - (new Date().getTime() - sa));
                } catch (Exception ignored) { }
            }
    }
}
