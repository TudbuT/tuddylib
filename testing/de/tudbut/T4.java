package de.tudbut;

import de.tudbut.io.StreamReader;
import de.tudbut.tools.FileRW;
import tudbut.debug.Debug;
import tudbut.debug.DebugProfiler;
import tudbut.net.http.HTTPContentType;
import tudbut.net.http.HTTPRequest;
import tudbut.net.http.HTTPRequestType;
import tudbut.obj.Save;
import tudbut.parsing.TCN;
import tudbut.tools.*;
import tudbut.tools.encryption.Key;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class T4 {
    
        @Save
        public static HTTPContentType contentType = HTTPContentType.AAC;
        @Save
        public static HTTPContentType contentTypeT = HTTPContentType.AAC;
        @Save
        public static HTTPContentType contentTypeTE = HTTPContentType.AAC;
        @Save
        public static HTTPContentType contentTypeTEF = HTTPContentType.AAC;
        @Save
        public static HTTPContentType contentTypeTEFR = HTTPContentType.AAC;
        /*
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
            new FileRW("mtt").setContent(ConfigSaverTCN.saveConfig(new de.tudbut.T4()).toString());
            contentType = HTTPContentType.TXT;
            System.out.println(contentType);
            ConfigSaverTCN.loadConfig(new de.tudbut.T4(), TCN.read(new FileRW("mtt").getContent().join("\n")));
            System.out.println(contentType);
    
        */
    public static void main(String[] args) throws IOException, InterruptedException, TCN.TCNException, IllegalAccessException, ClassNotFoundException {
        /*float[] floats = NoiseGenerator.generateRandom(1, 1, 500, 20, 50, new Random())[0][0];
        GraphRenderer graphRenderer = new GraphRenderer();
        graphRenderer.setScaleX(2);
        graphRenderer.setScaleY(2);
        BufferedImage image = graphRenderer.render(x -> floats[(int) x + 250], 500, 500, true);
        
        ImageIO.write(image, "PNG", new FileOutputStream("test5.png"));
        */
    
        Key key = new Key("a");
        System.out.println(key.encryptString("Test"));
        System.out.println(key.decryptString(key.encryptString("Test")));
        /*
        Lock lock = new Lock();
        System.out.println("Start " + new Date().getTime());
        lock.lock(1000);
        Thread.sleep(1200);
        lock.waitHere();
        System.out.println("Stop " + new Date().getTime());
    */
        
        //System.out.println(new StreamReader(new URL("https://www.tudbut.de").openConnection().getInputStream()).readAllAsString());
        //System.out.println(new HTTPRequest(HTTPRequestType.GET, "https://www.tudbut.de", 443, "/").send().parse().getBody());
        //new HTTPRequest(HTTPRequestType.POST, "api.tudbut.de", 80, "/api/track/play?uuid=6dd4a891-f23c-414c-8a02-a2dd5647a6f1").send();
        
        /*String[] uuids = new String[players.length];
        for (int i = 0; i < players.length; i++) {
            uuids[i] = new HTTPRequest(HTTPRequestType.GET, "api.tudbut.de", 80, "/api/getUUID?name=" + players[i]).send().parse().getBody();
        }
        */
        
        /*
        String s = "";
        while (true) {
            long sa = new Date().getTime();
            for (int i = 0; i < players.length; i++) {
                String uuid = uuids[i];
                new HTTPRequest(HTTPRequestType.POST, "api.tudbut.de", 80, "/api/track/play?uuid=" + uuid).send();
                s += players[i] + ": " + TCN.read(new HTTPRequest(HTTPRequestType.GET, "api.tudbut.de", 80, "/api/getUserRecord?uuid=" + uuid).send().parse().getBody()).getInteger("playTime") + "\n";
            }
            System.out.println(s);
            s = "";
            try {
                Thread.sleep(1000 - (new Date().getTime() - sa));
            } catch (Exception ignore) { }
        }*/
        
        
    }
}
