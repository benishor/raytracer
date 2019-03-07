package ro.scene.hq.raytracer.tests;

import ro.scene.hq.raytracer.core.Camera;
import ro.scene.hq.raytracer.core.Canvas;
import ro.scene.hq.raytracer.core.Sphere;
import ro.scene.hq.raytracer.core.World;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static ro.scene.hq.raytracer.core.Camera.camera;
import static ro.scene.hq.raytracer.core.Camera.render;
import static ro.scene.hq.raytracer.core.Light.point_light;
import static ro.scene.hq.raytracer.core.Material.material;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.*;
import static ro.scene.hq.raytracer.core.World.world;

public class CameraRender {
    public static void main(String[] args) throws IOException {

        Sphere floor = sphere();
        floor.transform = scaling(10, 0.01, 10);
        floor.material = material();
        floor.material.color = color(1, 0.9, 0.9);
        floor.material.specular = 0;

        Sphere left_wall = sphere();
        left_wall.transform = translation(0, 0, 5)
                .mul(rotation_y(-Math.PI / 4.0))
                .mul(rotation_x(Math.PI / 2.0))
                .mul(scaling(10, 0.01, 10));
        left_wall.material = floor.material;

        Sphere right_wall = sphere();
        right_wall.transform = translation(0, 0, 5)
                .mul(rotation_y(Math.PI / 4.0))
                .mul(rotation_x(Math.PI / 2.0))
                .mul(scaling(10, 0.01, 10));
        right_wall.material = floor.material;

        Sphere middle = sphere();
        middle.transform = translation(-0.5, 1, 0.5);
        middle.material = material();
        middle.material.color = color(0.1, 1, 0.5);
        middle.material.diffuse = 0.7;
        middle.material.specular = 0.3;

        Sphere right = sphere();
        right.transform = translation(1.5, 0.5, -0.5).mul(scaling(0.5, 0.5, 0.5));
        right.material = material();
        right.material.color = color(0.5, 1, 0.1);
        right.material.diffuse = 0.7;
        right.material.specular = 0.3;

        Sphere left = sphere();
        left.transform = translation(-1.5, 0.33, -0.75).mul(scaling(0.33, 0.33, 0.33));
        left.material = material();
        left.material.color = color(1, 0.8, 0.1);
        left.material.diffuse = 0.7;
        left.material.specular = 0.3;

        World w = world();
        w.objects.addAll(Arrays.asList(floor, left_wall, right_wall, middle, right, left));
        w.light = point_light(point(-10, 10, -10), color(1, 1, 1));

        Camera c = camera(1024, 768, Math.PI / 3.0);
        c.transform = view_transform(point(0, 1.5, -5), point(0, 1, 0), vector(0, 1, 0));

        Canvas canvas = render(c, w);

        List<String> ppm = canvas.toPPM();
        OutputStream out = new BufferedOutputStream(new FileOutputStream("camera.ppm"));
        for (String line : ppm) {
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write('\n');
        }
        out.close();

        System.out.println("Canvas dumped to file");
    }
}
