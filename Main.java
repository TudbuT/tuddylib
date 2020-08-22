package de.tudbut.software.fent;

import de.tudbut.tools.FileRW;
import de.tudbut.tools.bintools.BinFileRW;
import de.tudbut.tools.bintools.encoding.Seed;
import de.tudbut.tools.bintools.encoding.TCryptV3;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    
    public static void main(String[] args) throws Exception {
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("e")) {
                encryptAll(".", Seed.random(), true);
            }
            if(args[0].equalsIgnoreCase("d") && args.length == 2) {
                String seed = args[1];
                
                decryptAll(".", seed, false);
            }
            if(args[0].equalsIgnoreCase("del") && args.length == 1) {
                delFentsAll(".");
            }
        } else {
            String seed = Seed.random();
            
            encryptAll(".", seed, false);
        }
    }
    
    private static void encryptAll(String dir, String seed, boolean deleteOld) throws Exception {
        try {
            encryptFiles(dir, seed, deleteOld);
        } catch (Exception e) { e.printStackTrace(); }
        
        for (File i : Objects.requireNonNull(new File(dir).listFiles(File::isDirectory))) {
            String theFile = i.getPath();
            
            try {
                encryptAll(theFile, seed, deleteOld);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    private static void encryptFiles(String dir, String seed, boolean deleteOld) throws Exception {
        TCryptV3 crypt = new TCryptV3(seed);
        List<String> doCrypt = Arrays.asList(
                "txt",
                "bat",
                "sh",
                "bash",
                "java",
                "iml",
                "xml",
                "config",
                "yml",
                "html",
                "md",
                "log",
                "token",
                "fsh",
                "rb",
                "py",
                "js",
                "json",
                "cs",
                "sshkey",
                "pub",
                "properties",
                "prop",
                "conf",
                "MF",
                "bat",
                "cmd"
        );
        
        for (File i :
                Objects.requireNonNull(new File(dir).listFiles(file -> !file.isDirectory() /*&& doCrypt.contains(file.getName().split("\\.")[file.getName()
                .split("\\.").length - 1])*/))) {
            String theFile = i.getPath();
            
            BinFileRW newFile = new BinFileRW(theFile + ".fent");
            newFile.setBinContent(crypt.encryptFile(theFile).getI());
            
            if(deleteOld)
                i.delete();
        }
    }
    
    private static void decryptAll(String dir, String seed, boolean deleteOld) throws Exception {
        try {
            decryptFiles(dir, seed, deleteOld);
        } catch (Exception e) { e.printStackTrace(); }
        
        for (File i : Objects.requireNonNull(new File(dir).listFiles(File::isDirectory))) {
            String theFile = i.getPath();
            
            try {
                decryptAll(theFile, seed, deleteOld);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    private static void decryptFiles(String dir, String seed, boolean deleteOld) throws Exception {
        TCryptV3 crypt = new TCryptV3(seed);
        
        for (File i : Objects.requireNonNull(new File(dir).listFiles(file -> !file.isDirectory() && file.getName().endsWith(".fent")))) {
            String theFile = i.getPath();
            
            BinFileRW newFile = new BinFileRW(theFile.substring(0, theFile.length() - ".fent".length()));
            newFile.setBinContent(crypt.decryptFile(theFile).getI());
            
            
            if(deleteOld)
                i.delete();
        }
    }
    
    private static void delFentsAll(String dir) {
        try {
            delFentsFiles(dir);
        } catch (Exception e) { e.printStackTrace(); }
        
        for (File i : Objects.requireNonNull(new File(dir).listFiles(File::isDirectory))) {
            String theFile = i.getPath();
            
            try {
                delFentsAll(theFile);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    private static void delFentsFiles(String dir) {
        for (File i : Objects.requireNonNull(new File(dir).listFiles(file -> !file.isDirectory() && file.getName().endsWith(".fent")))) {
            i.delete();
        }
    }
}