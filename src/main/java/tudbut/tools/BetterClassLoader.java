package tudbut.tools;

import tudbut.obj.TLClassLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class BetterClassLoader extends URLClassLoader implements TLClassLoader {
    public BetterClassLoader(URL[] urls, ClassLoader classLoader) {
        super(urls, classLoader);
    }

    public BetterClassLoader(URL[] urls) {
        super(urls);
    }

    public BetterClassLoader(URL[] urls, ClassLoader classLoader, URLStreamHandlerFactory urlStreamHandlerFactory) {
        super(urls, classLoader, urlStreamHandlerFactory);
    }
    
    public BetterClassLoader(URLClassLoader loader) {
        super(loader.getURLs());
    }

    public Class<?> lc(String s) throws ClassNotFoundException {
        setClassAssertionStatus(s, true);
        return loadClass(s);
    }

    public Class<?> get(String s) throws ClassNotFoundException {
        Class<?> c;
        if((c = findLoadedClass(s)) != null) {
            return c;
        }
        else
            return findClass(s);
    }

    public Class<?> friendlyGet(String s) {
        try {
            Class<?> c;
            if ((c = findLoadedClass(s)) != null) {
                return c;
            } else
                return findClass(s);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
