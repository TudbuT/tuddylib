package de.tudbut.obj;

public interface TLClassLoader {
    Class<?> lc(String s) throws ClassNotFoundException;
    
    Class<?> get(String s) throws ClassNotFoundException;
    
    Class<?> friendlyGet(String s);
}
