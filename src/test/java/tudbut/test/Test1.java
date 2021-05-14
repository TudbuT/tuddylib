package tudbut.test;

import tudbut.parsing.JSON;
import tudbut.parsing.TCN;
import tudbut.rendering.Maths2D;
import tudbut.tools.ImageUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Test1 {
    
    public static void main(String[] args) throws IOException, JSON.JSONFormatException, TCN.TCNException {
    
        ImageIO.write(ImageUtils.getDifference(Maths2D.distortImage(ImageIO.read(new File("img_1.png")), 8, 8, 1), Maths2D.distortImage(ImageIO.read(new File("img.png")), 8, 8, 1)), "png", new File("img1.png"));
        System.out.println(ImageUtils.getSimilarity(ImageIO.read(new File("img_1.png")), ImageIO.read(new File("img.png"))));
    }
}
