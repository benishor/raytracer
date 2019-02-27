package ro.scene.hq.raytracer.tests;

import ro.scene.hq.raytracer.core.Canvas;
import ro.scene.hq.raytracer.core.Tuple;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ro.scene.hq.raytracer.core.Canvas.canvas;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class Cannon {

    static class Projectile {
        Tuple position;
        Tuple velocity;

        public Projectile(Tuple start, Tuple velocity) {
            this.position = start;
            this.velocity = velocity;
        }
    }

    static class Environment {
        Tuple gravity;
        Tuple wind;

        public Environment(Tuple gravity, Tuple wind) {
            this.gravity = gravity;
            this.wind = wind;
        }
    }

    public static void main(String[] args) throws IOException {
        Tuple start = point(0, 1, 0);
        Tuple velocity = normalize(vector(1, 1.8, 0)).mul(11.25);
        Projectile p = new Projectile(start, velocity);

        Tuple gravity = vector(0, -0.1, 0);
        Tuple wind = vector(-0.01, 0, 0);
        Environment e = new Environment(gravity, wind);

        Canvas c = canvas(900, 550);
        Tuple red = color(1, 0, 0);

        while (p.position.y > 0) {
            int px = (int) p.position.x;
            int py = (int) p.position.y;
            if (px >= 0 && px < c.width && py >= 0 && py < c.height) {
                c.writePixel(px, c.height - py, red);
            }

            Tuple newPosition = p.position.add(p.velocity);
            Tuple newVelocity = p.velocity.add(e.gravity).add(e.wind);
            p = new Projectile(newPosition, newVelocity);
        }

        List<String> ppm = c.toPPM();
        OutputStream out = new BufferedOutputStream(new FileOutputStream("cannon.ppm"));
        for (String line : ppm) {
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write('\n');
        }
        out.close();

        System.out.println("Canvas dumped to file");
    }
}
