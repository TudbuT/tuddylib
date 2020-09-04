package de.tudbut.pluginapi;

import tudbut.tools.BetterClassLoader;
import de.tudbut.type.FileFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarFile;

public class PluginManager {

    public static Plugin loadPlugin(String jarPath)
            throws IOException {
        String main;

        JarFile file = new JarFile(new File(jarPath));

        main = new BufferedReader(new InputStreamReader(file.getInputStream(file.getEntry("main.tplv")))).readLine();

        return loadPlugin(jarPath, main);
    }

    public static Plugin loadPlugin(String jarPath, boolean autoLoad)
            throws IOException {
        String main;

        JarFile file = new JarFile(new File(jarPath));

        main = file.getEntry("main.tplv").toString();

        return loadPlugin(jarPath, main, autoLoad);
    }

    public static Plugin loadPlugin(String jarPath, String main) {
        return loadPlugin(jarPath, main, true);
    }

    public static Plugin loadPlugin(String jarPath, String main, boolean autoLoad) {


        URL[] urls;
        try {
            urls = new URL[]{new File(jarPath).toURI().toURL()};
        } catch (MalformedURLException e) {
            System.out.println("Couldn't load plugin " + jarPath);
            e.printStackTrace();
            return null;
        }
        BetterClassLoader cl = new BetterClassLoader(urls);

        Plugin r;
        try {
            r = cl.lc(main).asSubclass(Plugin.class).newInstance();
            r.setLogger(jarPath.split(File.separator)[jarPath.split(File.separator).length - 1]);
            r.setCL(cl, jarPath.split(File.separator)[jarPath.split(File.separator).length - 1]);
            if (autoLoad) {
                r.onLoad();
                r.onEvent(new PluginLoadEvent());
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
