package de.tudbut;

import tudbut.io.FileBus;
import tudbut.io.StreamRedirect;
import tudbut.io.TypedOutputStream;
import tudbut.net.http.HTTPRequest;
import tudbut.net.http.HTTPRequestType;
import tudbut.obj.Save;
import tudbut.obj.TLMap;
import tudbut.parsing.TCN;
import tudbut.tools.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ee {
    
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IOException, TCN.TCNException, NoSuchAlgorithmException, InterruptedException {
    
        FileBus fileBus = new FileBus(new File("test.bus"));
    
        TypedOutputStream stream = fileBus.getTypedWriter();
        
        StreamRedirect.redirect(System.in, fileBus.getWriter(), new HashMap<>());
    }
    
}
