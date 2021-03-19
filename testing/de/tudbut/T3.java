package de.tudbut;

import de.tudbut.tools.Hasher;
import de.tudbut.tools.Mouse;
import de.tudbut.type.Vector3d;
import de.tudbut.ui.windowgui.RenderableWindow;
import tudbut.rendering.Graph;
import tudbut.rendering.GraphRenderer;
import tudbut.rendering.Maths2D;
import tudbut.tools.AudioPlayer;
import tudbut.tools.MappableIO;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class T3 {
    public static void main(String[] ignored) throws NoSuchAlgorithmException {
    
        System.out.println(Hasher.sha512hex(Hasher.sha256hex("justyouraveragepassword")));
    }
}
