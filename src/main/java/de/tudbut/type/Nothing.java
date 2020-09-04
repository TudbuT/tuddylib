package de.tudbut.type;

public class Nothing {
    public static void voidObject(Object toVoid) {
        // literally does nothing with it expect killing it
        Runtime.getRuntime().gc();
    }
}
