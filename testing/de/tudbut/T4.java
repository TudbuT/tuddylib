package de.tudbut;

import tudbut.parsing.TCN;
import tudbut.tools.Time;

import java.io.IOException;

public class T4 {
    
    static int i = 1;
    
    public static void main(String[] args) throws IOException, InterruptedException, TCN.TCNException {
        /*float[] floats = NoiseGenerator.generateRandom(1, 1, 500, 20, 50, new Random())[0][0];
        GraphRenderer graphRenderer = new GraphRenderer();
        graphRenderer.setScaleX(2);
        graphRenderer.setScaleY(2);
        BufferedImage image = graphRenderer.render(x -> floats[(int) x + 250], 500, 500, true);
        
        ImageIO.write(image, "PNG", new FileOutputStream("test5.png"));
        */
    
    
        System.out.println(Time.ydhms(Long.MAX_VALUE / 1000 * 2));
    
    
        //System.out.println(new HTTPRequest(HTTPRequestType.GET, "https://api.mojang.com", 443, "/user/profiles/b8dd8777a0744f3da5b90b19def1b1ac/names").send().parse().getBody());
        //new HTTPRequest(HTTPRequestType.POST, "api.tudbut.de", 80, "/api/track/play?uuid=6dd4a891-f23c-414c-8a02-a2dd5647a6f1").send();
        
        /*String[] uuids = new String[players.length];
        for (int i = 0; i < players.length; i++) {
            uuids[i] = new HTTPRequest(HTTPRequestType.GET, "api.tudbut.de", 80, "/api/getUUID?name=" + players[i]).send().parse().getBody();
        }
        
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
        }
        
         */
    }
}
