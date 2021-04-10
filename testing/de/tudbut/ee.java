package de.tudbut;

import tudbut.net.http.HTTPRequest;
import tudbut.net.http.HTTPRequestType;
import tudbut.obj.Save;
import tudbut.obj.TLMap;
import tudbut.parsing.TCN;
import tudbut.tools.*;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class ee {
    
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IOException, TCN.TCNException, NoSuchAlgorithmException, InterruptedException {
    
        /*Lock lock = new Lock();
        URL url = new URL("https://camo.githubusercontent.com/a3b51f1cea0775cc854b93d7e24a7eb0a97f7cbc416e37ac92d619ff2ab657f3/68747470733a2f2f6b6f6d617265762e636f6d2f67687076632f3f757365726e616d653d547564627554267374796c653d666c61742d73717561726526636f6c6f723d626c756576696f6c6574");
        while (true) {
            new Thread(() -> {
                try {
                    url.openConnection().getContent();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            System.out.println("Done!");
            Thread.sleep(200);
        }
        
         */
    


        ThreadPool pool = new ThreadPool(5, " ", false);
        
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        pool.run(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X");
        });
        
    }
    
}
