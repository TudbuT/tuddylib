package de.tudbut.tools;

import de.tudbut.obj.TLClassLoader;

public class ClassLoaderAdapter implements TLClassLoader {
    private final ClassLoader cl;
    
    public ClassLoaderAdapter(ClassLoader classLoader) {
        cl = classLoader;
    }
    
    public Class<?> lc(String s) throws ClassNotFoundException {
        cl.setClassAssertionStatus(s, true);
        return cl.loadClass(s);
    }
    
    public Class<?> get(String s) throws ClassNotFoundException {
        return cl.loadClass(s);
    }
    
    public Class<?> friendlyGet(String s) {
        try {
            return cl.loadClass(s);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
