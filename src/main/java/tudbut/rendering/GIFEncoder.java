package tudbut.rendering;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.RenderedImage;
import java.io.IOException;

public class GIFEncoder {
    
    protected final ImageWriter writer;
    protected final ImageWriteParam params;
    protected final IIOMetadata metadata;
    protected ImageOutputStream stream;
    
    public GIFEncoder(ImageOutputStream out, int imageType, int delay, boolean loop) throws IOException {
        writer = ImageIO.getImageWritersBySuffix("gif").next();
        params = writer.getDefaultWriteParam();
        
        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
        metadata = writer.getDefaultImageMetadata(imageTypeSpecifier, params);
        
        configureRootMetadata(delay, loop);
        
        writer.setOutput(out);
        writer.prepareWriteSequence(null);
    }
    
    private void configureRootMetadata(int delay, boolean loop) throws IIOInvalidTreeException {
        String metaFormatName = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);
        
        IIOMetadataNode meta = getNode(root, "GraphicControlExtension");
        meta.setAttribute("disposalMethod", "none");
        meta.setAttribute("userInputFlag", "FALSE");
        meta.setAttribute("transparentColorFlag", "FALSE");
        meta.setAttribute("delayTime", Integer.toString(delay / 10));
        meta.setAttribute("transparentColorIndex", "0");
        
        meta = getNode(root, "ApplicationExtensions");
        
        int loopContinuously = loop ? 0 : 1;
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        child.setAttribute("applicationID", "TuddyLIB");
        child.setAttribute("authenticationCode", "1.0");
        child.setUserObject(new byte[]{ 0x1, (byte) (loopContinuously & 0xFF), 0});
        meta.appendChild(child);
        metadata.setFromTree(metaFormatName, root);
    }
    
    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++){
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
                return (IIOMetadataNode) rootNode.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }
    
    public void addFrame(RenderedImage img) throws IOException {
        writer.writeToSequence(new IIOImage(img, null, metadata), params);
    }
    
    public void close() throws IOException {
        writer.endWriteSequence();
    }
}
