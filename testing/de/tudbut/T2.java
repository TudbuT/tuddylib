package de.tudbut;

import de.tudbut.io.StreamReader;
import tudbut.global.DebugStateManager;
import tudbut.obj.Atomic;
import tudbut.obj.IgnoreThrowRunnable;
import tudbut.obj.PrintThrowRunnable;
import tudbut.tools.ThreadPool;
import tudbut.tools.Tools2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class T2 {
    

    public static void main(String[] args) throws Throwable {
        final int frameCount = 500;
        
        StreamReader.BUFFER_SIZE = 8192 * 2 * 2 * 2 * 2;
        ThreadPool pool = new ThreadPool(50, "main", false);
        pool.run(() -> {
            System.out.println("started");
        });
        Thread.sleep(0);
        Thread.sleep(0);
    
        System.out.println(new Date().getTime());
        try {
            Tools2.deleteDir(new File("frames/cache"));
        } catch (Exception ignore) {}
        new File("frames/cache/").mkdirs();
        new File("frames.t2f").delete();
        new File("frames.t2f").createNewFile();
        
        final boolean[] done = new boolean[frameCount];
        final Atomic<BufferedImage> screenshot = new Atomic<>();
        final Atomic<Boolean> allDone = new Atomic<>(false);
        pool.run((PrintThrowRunnable) () -> {
            File zip;
            zip = new File("frames.t2f");
    
            File[] files = new File[frameCount];
            for (int i = 0; i < frameCount; i++) {
                files[i] = new File("frames/cache/" + i + ".frame");
                while (!done[i]) Thread.sleep(1);
            }
    
            
            Tools2.addFilesToZIP(zip, file -> {
                System.out.println("Compressed: " + file.getName().split("\\.")[0]);
            }, files);
            
    
            System.out.println(new Date().getTime());
            try {
                Tools2.deleteDir(new File("frames/cache"));
            } catch (Exception ignore) {}
            System.exit(0);
        });
        
        new Thread(() -> {
            while (!allDone.get()) {
                screenshot.set(Tools2.screenshot());
            }
        }).start();
        
        for (int i = 0; i < frameCount; i++) {
            long sa = new Date().getTime();
    
            int finalI = i;
            pool.run((PrintThrowRunnable) () -> {
                while (screenshot.get() == null) {
                    Thread.sleep(10);
                }
                ImageIO.write(screenshot.get(), "jpg", new File("frames/cache/" + finalI + ".frame"));
                System.out.println("Cached: " + finalI);
                done[finalI] = true;
            });
            
            try {
                Thread.sleep(200 - (new Date().getTime() - sa));
            } catch (Exception ignored) {
                System.out.println("TLF");
            }
        }
        allDone.set(true);
    }
    
    
}
