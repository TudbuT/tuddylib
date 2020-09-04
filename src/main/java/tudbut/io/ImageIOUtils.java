package tudbut.io;

import de.tudbut.tools.Tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageIOUtils {

    public static int[] imageToBytes(BufferedImage image) {
        DataBuffer buffer = image.getData().getDataBuffer();

        if(buffer.getClass() == DataBufferInt.class) {
            return ((DataBufferInt) buffer).getData();
        }
        if(buffer.getClass() == DataBufferByte.class) {
            return Tools.byteArrayToIntArray(((DataBufferByte) buffer).getData());
        }
        throw new IllegalStateException();
    }

    public static BufferedImage imageFromBytes(int[] ints) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(Tools.charArrayToByteArray(Tools.intArrayToCharArray(ints))));
    }
}
