package tudbut.rendering;

import de.tudbut.type.Vector2d;
import de.tudbut.type.Vector3d;

import java.awt.*;

public class RenderAssistant {
    @Deprecated
    public static void renderToGraphics(Graphics g, Projection3D projection) {
        if (g == null)
            throw new IllegalArgumentException();

        projection.render(g);
    }

    public static void renderToGraphics(Graphics g, Projection2D projection) {
        if (g == null)
            throw new IllegalArgumentException();

        projection.render(g);
    }

    @Deprecated
    public static void drawRectangle(Projection3D projection, double[] doubles) {
        if (doubles.length != 4 * 3)
            throw new IllegalArgumentException("Length must be 4 * 3!");
        if (projection == null)
            throw new IllegalArgumentException("NULL");

        projection.drawRectangle(
                new Vector3d(doubles[0], doubles[1], doubles[2]),
                new Vector3d(doubles[3], doubles[4], doubles[5]),
                new Vector3d(doubles[6], doubles[7], doubles[8]),
                new Vector3d(doubles[9], doubles[10], doubles[11])
        );
    }

    @Deprecated
    public static void fillRectangle(Projection3D projection, double[] doubles) {
        if (doubles.length != 4 * 3)
            throw new IllegalArgumentException("Length must be 4 * 3!");
        if (projection == null)
            throw new IllegalArgumentException("NULL");

        projection.fillRectangle(
                new Vector3d(doubles[0], doubles[1], doubles[2]),
                new Vector3d(doubles[3], doubles[4], doubles[5]),
                new Vector3d(doubles[6], doubles[7], doubles[8]),
                new Vector3d(doubles[9], doubles[10], doubles[11])
        );
    }

    public static void drawRectangle(Projection2D projection, double[] doubles) {
        if (doubles.length != 4 * 2)
            throw new IllegalArgumentException("Length must be 4 * 2!");
        if (projection == null)
            throw new IllegalArgumentException("NULL");

        projection.drawRectangle(
                new Vector2d(doubles[0], doubles[1]),
                new Vector2d(doubles[2], doubles[3]),
                new Vector2d(doubles[4], doubles[5]),
                new Vector2d(doubles[6], doubles[7])
        );
    }

    public static void fillRectangle(Projection2D projection, double[] doubles) {
        if (doubles.length != 4 * 2)
            throw new IllegalArgumentException("Length must be 4 * 2!");
        if (projection == null)
            throw new IllegalArgumentException("NULL");

        projection.fillRectangle(
                new Vector2d(doubles[0], doubles[1]),
                new Vector2d(doubles[2], doubles[3]),
                new Vector2d(doubles[4], doubles[5]),
                new Vector2d(doubles[6], doubles[7])
        );
    }

    public static void drawTriangle(Projection3D projection, double[] doubles) {
        if (doubles.length != 3 * 3)
            throw new IllegalArgumentException("Length must be 3 * 3!");
        if (projection == null)
            throw new IllegalArgumentException("NULL");

        projection.drawTriangle(
                new Vector3d(doubles[0], doubles[1], doubles[2]),
                new Vector3d(doubles[3], doubles[4], doubles[5]),
                new Vector3d(doubles[6], doubles[7], doubles[8])
        );
    }

    public static void fillTriangle(Projection3D projection, double[] doubles) {
        if (doubles.length != 3 * 3)
            throw new IllegalArgumentException("Length must be 3 * 3!");
        if (projection == null)
            throw new IllegalArgumentException("NULL");

        projection.fillTriangle(
                new Vector3d(doubles[0], doubles[1], doubles[2]),
                new Vector3d(doubles[3], doubles[4], doubles[5]),
                new Vector3d(doubles[6], doubles[7], doubles[8])
        );
    }

    public static void drawTriangle(Projection2D projection, double[] doubles) {
        if (doubles.length != 3 * 2)
            throw new IllegalArgumentException("Length must be 3 * 2!");
        if (projection == null)
            throw new IllegalArgumentException("NULL");

        projection.drawTriangle(
                new Vector2d(doubles[0], doubles[1]),
                new Vector2d(doubles[2], doubles[3]),
                new Vector2d(doubles[4], doubles[5])
        );
    }

    public static void fillTriangle(Projection2D projection, double[] doubles) {
        if (doubles.length != 3 * 2)
            throw new IllegalArgumentException("Length must be 3 * 2!");
        if (projection == null)
            throw new IllegalArgumentException("NULL");

        projection.fillTriangle(
                new Vector2d(doubles[0], doubles[1]),
                new Vector2d(doubles[2], doubles[3]),
                new Vector2d(doubles[4], doubles[5])
        );
    }

    public static double[] addOffset(double[] doubles, double xOffset, double yOffset, double zOffset) {
        byte currentCoord = 0;

        for (int i = 0; i < doubles.length; i++) {
            if (currentCoord == 0)
                doubles[i] = doubles[i] + xOffset;
            if (currentCoord == 1)
                doubles[i] = doubles[i] + yOffset;
            if (currentCoord == 2) {
                doubles[i] = doubles[i] + zOffset;
                currentCoord = -1;
            }
            currentCoord++;
        }

        return doubles;
    }

    public static double[] addOffset(double[] doubles, double xOffset, double yOffset) {
        byte currentCoord = 0;

        for (int i = 0; i < doubles.length; i++) {
            if (currentCoord == 0)
                doubles[i] = doubles[i] + xOffset;
            if (currentCoord == 1) {
                doubles[i] = doubles[i] + yOffset;
                currentCoord = -1;
            }
            currentCoord++;
        }

        return doubles;
    }

    public static Vector3d[] addOffset(Vector3d[] vectors, double xOffset, double yOffset, double zOffset) {
        for (Vector3d vector : vectors) {
            vector.set(vector.getX() + xOffset, vector.getY() + yOffset, vector.getZ() + zOffset);
        }

        return vectors;
    }

    public static Vector2d[] addOffset(Vector2d[] vectors, double xOffset, double yOffset) {
        for (Vector2d vector : vectors) {
            vector.set(vector.getX() + xOffset, vector.getY() + yOffset);
        }

        return vectors;
    }
}
