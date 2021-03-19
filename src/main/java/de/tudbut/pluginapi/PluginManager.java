package de.tudbut.pluginapi;

import tudbut.tools.BetterClassLoader;
import de.tudbut.type.FileFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class PluginManager {

    public static Plugin loadPlugin(String jarPath)
            throws IOException {
        String main = "";

        JarFile file = new JarFile(new File(jarPath));

        if(file.getEntry("main.tplv") != null)
            main = new BufferedReader(new InputStreamReader(file.getInputStream(file.getEntry("main.tplv")))).readLine();

        return loadPlugin(jarPath, main);
    }

    public static Plugin loadPlugin(String jarPath, boolean autoLoad)
            throws IOException {
        String main = "";
    
        JarFile file = new JarFile(new File(jarPath));
    
        if(file.getEntry("main.tplv") != null)
            main = new BufferedReader(new InputStreamReader(file.getInputStream(file.getEntry("main.tplv")))).readLine();
    
    
        return loadPlugin(jarPath, main, autoLoad);
    }

    public static Plugin loadPlugin(String jarPath, String main) {
        return loadPlugin(jarPath, main, true);
    }

    public static Plugin loadPlugin(String jarPath, String main, boolean autoLoad) {
    
        System.out.println("Loading plugin " + jarPath);
        Plugin.loadClass();
        URL[] urls;
        JarFile file = null;
        try {
            file = new JarFile(jarPath);
        }
        catch (IOException e) {
            System.out.println("Couldn't load plugin " + jarPath);
            e.printStackTrace();
            return null;
        }
        try {
            urls = new URL[]{new File(jarPath).toURI().toURL()};
        } catch (MalformedURLException e) {
            System.out.println("Couldn't load plugin " + jarPath);
            e.printStackTrace();
            return null;
        }
        BetterClassLoader cl = new BetterClassLoader(urls);

        Plugin r = null;
        try {
            if(!main.equals("")) {
                Class<?> lc = cl.lc(main);
                if(Plugin.class.isAssignableFrom(lc.getSuperclass()))
                    r = (Plugin) lc.newInstance();
                else {
                    System.out.println(lc.getSuperclass().hashCode());
                    System.out.println(Plugin.class.hashCode());
                }
            } else {
                ArrayList<Class<?>> classes = new ArrayList<>();
                Enumeration<? extends ZipEntry> enumerator = file.entries();
                while (enumerator.hasMoreElements()) {
                    ZipEntry entry = enumerator.nextElement();
                    if(entry.getName().endsWith(".class")) {
                        String s = entry.getName().split("\\.")[0].replaceAll("/", ".");
                        System.out.println("Loading class " + s);
                        Class<?> clazz = cl.lc(s);
                        if(clazz != null)
                            classes.add(clazz);
                    }
                }
    
                for (int i = 0; i < classes.size(); i++) {
                    if(classes.get(i).getSuperclass().getName().equals(Plugin.class.getName())) {
                        try {
                            r = classes.get(i).asSubclass(Plugin.class).newInstance();
                        } catch (InstantiationException ignore) { }
                    }
                }
            }
            if (r != null) {
                r.setLogger(jarPath.split(File.separator)[jarPath.split(File.separator).length - 1]);
                r.setCL(cl, jarPath.split(File.separator)[jarPath.split(File.separator).length - 1]);
                if (autoLoad) {
                    r.onLoad();
                    r.onEvent(new PluginLoadEvent());
                }
            }
            else {
                System.out.println("Couldn't load plugin " + jarPath);
                return null;
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | PluginException e) {
            System.out.println("Couldn't load plugin " + jarPath);
            e.printStackTrace();
            return null;
        }


        return r;
    }

    public static Plugin[] loadPlugins(File dir) throws FileFormatException {
        if (!dir.isDirectory()) {
            throw new FileFormatException();
        }

        Plugin[] plugins = new Plugin[dir.listFiles(file -> file.getAbsolutePath().endsWith(".jar")).length];
        for (int i = 0; i < dir.listFiles(file -> file.getAbsolutePath().endsWith(".jar")).length; i++) {
            try {
                plugins[i] = loadPlugin(dir.listFiles(file -> file.getAbsolutePath().endsWith(".jar"))[i].getAbsolutePath());
            } catch (IOException ignore) { }
        }
        return plugins;
    }

    public static void unloadPlugin(Plugin plugin) throws IOException {
        plugin.onUnload();
        plugin.onEvent(new PluginUnloadEvent());
        plugin.getCL().close();
    }

    public static void unloadPlugins(Plugin[] plugins) throws IOException {
        for (Plugin plugin : plugins)
            unloadPlugin(plugin);
    }
}
