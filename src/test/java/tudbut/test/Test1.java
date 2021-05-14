package tudbut.test;

import tudbut.parsing.JSON;
import tudbut.parsing.TCN;
import tudbut.rendering.Maths2D;
import tudbut.tools.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Test1 {
    
    public static void main(String[] args) throws IOException, JSON.JSONFormatException, TCN.TCNException {
    
        boolean b = true;
        
        BufferedImage img0 = ImageIO.read(new File("img.png"));
        BufferedImage img1 = ImageIO.read(new File("img_1.png"));
        img1 = Maths2D.distortImage(img1, img0.getWidth(), img0.getHeight(), 1);
        
        if(b)
            img0 = ImageUtils.invert(img0);
        ImageIO.write(img0, "png", new File("img2.png"));
        
        long l = new Date().getTime();
        ImageIO.write(ImageUtils.getDifference(img0,img1), "png", new File("img1.png"));
        //System.out.println(ImageUtils.getSimilarityV2(ImageIO.read(new File("img_1.png")), ImageIO.read(new File("img.png"))));
        System.out.println(System.currentTimeMillis() - l);
    }
}
