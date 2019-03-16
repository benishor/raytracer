package ro.scene.hq.raytracer.core;

import java.util.stream.IntStream;

import static ro.scene.hq.raytracer.core.Canvas.canvas;
import static ro.scene.hq.raytracer.core.Matrix.inverse;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Tuple.normalize;
import static ro.scene.hq.raytracer.core.Tuple.point;
import static ro.scene.hq.raytracer.core.World.color_at;

public class Camera {

    public final int hsize;
    public final int vsize;
    public final double fov;
    public Matrix transform;
    public double pixelSize;
    public double halfWidth;
    public double halfHeight;

    public Camera(int hsize, int vsize, double fov) {
        this.hsize = hsize;
        this.vsize = vsize;
        this.fov = fov;
        this.transform = Matrix.identity(4);

        double halfView = Math.tan(fov / 2.0);
        double aspect = hsize / (double) vsize;
        if (aspect >= 1) {
            halfWidth = halfView;
            halfHeight = halfView / aspect;
        } else {
            halfWidth = halfView * aspect;
            halfHeight = halfView;
        }
        pixelSize = (2.0 * halfWidth) / (double) hsize;
    }

    public static Camera camera(int hsize, int vsize, double fov) {
        return new Camera(hsize, vsize, fov);
    }

    public static Ray ray_for_pixel(Camera c, int px, int py) {
        // the offset from the edge of the canvas to the pixel's center
        double xoffset = (px + 0.5) * c.pixelSize;
        double yoffset = (py + 0.5) * c.pixelSize;

        // the untransformed coordinates of the pixel in world space.
        // (remember that the camera looks toward -z, so +x is to the *left*.)
        double world_x = c.halfWidth - xoffset;
        double world_y = c.halfHeight - yoffset;

        // using the camera matrix, transform the canvas point and the origin,
        // and then compute the ray's direction vector.
        // (remember that the canvas is at z=-1)
        Tuple pixel = inverse(c.transform).mul(point(world_x, world_y, -1));
        Tuple origin = inverse(c.transform).mul(point(0, 0, 0));
        Tuple direction = normalize(pixel.sub(origin));
        return ray(origin, direction);
    }

    public static Canvas render(Camera c, World w) {
        Canvas image = canvas(c.hsize, c.vsize);
        for (int y = 0; y < c.vsize; y++) {
            final int yy = y;
            IntStream.range(0, c.hsize).parallel().forEach(x -> {
                Ray ray = ray_for_pixel(c, x, yy);
                Tuple color = color_at(w, ray);
                image.writePixel(x, yy, color);
            });
//            for (int x = 0; x < c.hsize; x++) {
//                Ray ray = ray_for_pixel(c, x, y);
//                Tuple color = color_at(w, ray);
//                image.writePixel(x, y, color);
//            }
        }
        return image;
    }
}
