package de.tudbut;

import de.tudbut.tools.Hasher;
import de.tudbut.tools.Mouse;
import de.tudbut.tools.Tools;
import de.tudbut.type.Vector3d;
import de.tudbut.ui.windowgui.RenderableWindow;
import tudbut.io.FileBus;
import tudbut.io.TypedInputStream;
import tudbut.net.http.HTTPRequest;
import tudbut.net.http.HTTPRequestType;
import tudbut.parsing.TCN;
import tudbut.rendering.Graph;
import tudbut.rendering.GraphRenderer;
import tudbut.rendering.Maths2D;
import tudbut.tools.AudioPlayer;
import tudbut.tools.Lock;
import tudbut.tools.MappableIO;
import tudbut.tools.Serializing;

import javax.swing.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class T3 {
    public static void main(String[] ignored) throws NoSuchAlgorithmException, IOException, TCN.TCNException {
        FileBus fileBus = new FileBus(new File("test.bus"));
    
        TypedInputStream stream = fileBus.getTypedReader();
        
        while (true) {
            System.out.write(stream.read());
        }
    }
}
