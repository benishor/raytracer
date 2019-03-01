package ro.scene.hq.raytracer.tests;

import ro.scene.hq.raytracer.core.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ro.scene.hq.raytracer.core.Canvas.canvas;
import static ro.scene.hq.raytracer.core.Matrix.scaling;
import static ro.scene.hq.raytracer.core.Matrix.shearing;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Sphere.intersect;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class Raycasting {
    public static void main(String[] args) throws IOException {

        Tuple red = color(1, 0, 0);
        Tuple rayOrigin = point(0, 0, -5.0);
        double wallZ = 10.0;
        double wallSize = 7.0;
        int canvasPixels = 100;
        double pixelSize = wallSize / canvasPixels;
        double half = wallSize / 2.0;

        Sphere s = sphere();
        s.transform = shearing(1, 0, 0, 0, 0, 0).mul(scaling(0.5, 1, 1));

        Canvas c = canvas(canvasPixels, canvasPixels);

        for (int y = 0; y < c.height; y++) {
            double worldY = half - pixelSize * y;
            for (int x = 0; x < c.width; x++) {
                double worldX = -half + pixelSize * x;

                Tuple position = point(worldX, worldY, wallZ);
                Ray r = ray(rayOrigin, normalize(position.sub(rayOrigin)));
                List<Intersection> xs = intersect(s, r);
                if (!xs.isEmpty()) {
                    c.writePixel(x, y, red);
                }
            }
        }

        List<String> ppm = c.toPPM();
        OutputStream out = new BufferedOutputStream(new FileOutputStream("raycasting.ppm"));
        for (String line : ppm) {
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write('\n');
        }
        out.close();

        System.out.println("Canvas dumped to file");
    }
}
