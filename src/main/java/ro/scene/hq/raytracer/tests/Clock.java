package ro.scene.hq.raytracer.tests;

import ro.scene.hq.raytracer.core.Canvas;
import ro.scene.hq.raytracer.core.Matrix;
import ro.scene.hq.raytracer.core.Tuple;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ro.scene.hq.raytracer.core.Canvas.canvas;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Tuple.color;
import static ro.scene.hq.raytracer.core.Tuple.point;

public class Clock {
    public static void main(String[] args) throws IOException {
        Canvas c = canvas(1024, 768);

        double rotationIncrement = 2 * Math.PI / 12.0;

        Tuple white = color(1, 1, 1);

        double angle = 0.0;
        for (int i = 0; i < 12; i++) {
            Tuple p = point(0, 1, 0);
            Matrix rotation = rotation_z(angle);
            Matrix scale = scaling(300, 300, 0);
            Matrix translate = translation(c.width / 2, c.height / 2, 0);

            Matrix transform = translate.mul(scale).mul(rotation);
            Tuple p2 = transform.mul(p);

            c.writePixel((int)p2.x, (int)p2.y, white);

            angle += rotationIncrement;
        }


        List<String> ppm = c.toPPM();
        OutputStream out = new BufferedOutputStream(new FileOutputStream("clock.ppm"));
        for (String line : ppm) {
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write('\n');
        }
        out.close();

        System.out.println("Canvas dumped to file");
    }
}
