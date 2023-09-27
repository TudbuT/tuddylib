package de.tudbut.rendering.tph;

import de.tudbut.rendering.Projection3D;
import de.tudbut.type.Vector2d;
import de.tudbut.type.FInfo;
import de.tudbut.type.Vector3d;

import java.awt.*;
import java.awt.image.BufferedImage;

import static de.tudbut.rendering.tph.TPH301.*;
import static de.tudbut.rendering.tph.TPH300.*;

@FInfo(s = "TuddyProjection Helper 3D 02: Textures")
public class TPH302 {
    public static final int ID = 303;

    public static void renderTexturedRectangle(Vector3d offset, Vector2d size, Vector3d rot, Vector3d rotPoint, long camera, Projection3D projection3D, Image image) {
        BufferedImage img = (BufferedImage) image;

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                    projection3D.setColor(img.getRGB(x,y));
                    projection3D.fillRectangle(
                            translate(applyRotations(new Vector3d(offset.getX() + (size.getX() / img.getWidth()) * x, offset.getY() + (size.getY() / img.getHeight()) * y, offset.getZ()), rot, rotPoint), camera),
                            translate(applyRotations(new Vector3d(offset.getX() + (size.getX() / img.getWidth()) * (x + 1), offset.getY() + (size.getY() / img.getHeight()) * (y), offset.getZ()), rot, rotPoint), camera),
                            translate(applyRotations(new Vector3d(offset.getX() + (size.getX() / img.getWidth()) * (x + 1), offset.getY() + (size.getY() / img.getHeight()) * (y + 1), offset.getZ()), rot, rotPoint), camera),
                            translate(applyRotations(new Vector3d(offset.getX() + (size.getX() / img.getWidth()) * (x), offset.getY() + (size.getY() / img.getHeight()) * (y + 1), offset.getZ()), rot, rotPoint), camera)
                    );
            }
        }
    }

    private static Vector3d applyRotations(Vector3d vec, Vector3d rot, Vector3d rotPoint) {
        return applyRotation(applyRotation(applyRotation(vec, rotPoint, Z, rot.getZ()), rotPoint, Y, rot.getY()), rotPoint, X, rot.getX());
    }
}
