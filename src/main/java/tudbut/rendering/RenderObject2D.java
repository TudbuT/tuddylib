package tudbut.rendering;

import de.tudbut.tools.Tools;
import de.tudbut.type.Vector2d;

import java.awt.image.BufferedImage;

public class RenderObject2D {
    private final double multiplier;
    final Vector2d[] vectors;
    final RenderObjectType type;
    int color;
    BufferedImage image;

    public RenderObject2D(Vector2d pos1, Vector2d pos2, Vector2d pos3, boolean fill, double multiplier, int color) {
        type = fill ? RenderObjectType.FULL_TRIANGLE : RenderObjectType.TRIANGLE;
        vectors = new Vector2d[]{pos1, pos2, pos3};
        this.multiplier = multiplier;
        this.color = color;

        updateMultiplier();
    }

    public RenderObject2D(Vector2d pos1, Vector2d pos2, Vector2d pos3, Vector2d pos4, boolean fill, double multiplier, int color) {
        type = fill ? RenderObjectType.FULL_RECTANGLE : RenderObjectType.RECTANGLE;
        vectors = new Vector2d[]{pos1, pos2, pos3, pos4};
        this.multiplier = multiplier;
        this.color = color;

        updateMultiplier();
    }
    
    public RenderObject2D(BufferedImage image, Vector2d pos, Vector2d size, double multiplier) {
        type = RenderObjectType.IMAGE;
        vectors = new Vector2d[]{pos, size};
        this.multiplier = multiplier;
        this.image = image;
        
        updateMultiplier();
    }

    private RenderObject2D(Vector2d[] vectors, RenderObjectType type, double multiplier, int color, BufferedImage image) {
        this.vectors = vectors;
        this.type = type;
        this.multiplier = multiplier;
        this.color = color;
        this.image = image;
    }

    void updateMultiplier() {
        for (Vector2d vec : vectors) {
            vec.multiply(multiplier);
        }
    }

    @Override
    public RenderObject2D clone() {
        Vector2d[] lvectors = new Vector2d[vectors.length];
        Tools.copyArray(vectors, lvectors, vectors.length);

        for (int i = 0; i < lvectors.length; i++) {
            lvectors[i] = lvectors[i].clone();
        }

        return new RenderObject2D(lvectors, type, multiplier, color, image);
    }
}
