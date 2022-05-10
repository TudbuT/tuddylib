import de.tudbut.type.Vector3d;
import tudbut.rendering.Projection3D;
import tudbut.rendering.RenderOutputType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static tudbut.rendering.tph.TPH300.*;
import static tudbut.rendering.tph.TPH301.*;
import static tudbut.rendering.tph.TPH302.*;

public class RenderTest {

    public static void main(String[] args) throws IOException {

        Projection3D d = new Projection3D(new AtomicInteger(100), new AtomicInteger(100), RenderOutputType.BUFFEREDIMAGE, 0xff80a0ff);
        d.setColor(0xff000000);
        d.fillRectangle(new Vector3d(1, 1, 10), new Vector3d(-1, 1, 10), new Vector3d(-1, -1, 10), new Vector3d(1, -1, 10));
        BufferedImage render = (BufferedImage) d.render();
        ImageIO.write(render, "png", new File("output.png"));
    }
}
