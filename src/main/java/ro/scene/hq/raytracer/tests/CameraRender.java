package ro.scene.hq.raytracer.tests;

import ro.scene.hq.raytracer.core.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static ro.scene.hq.raytracer.core.Camera.camera;
import static ro.scene.hq.raytracer.core.Camera.render;
import static ro.scene.hq.raytracer.core.CheckersPattern.checkers_pattern;
import static ro.scene.hq.raytracer.core.Light.point_light;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Plane.plane;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.*;
import static ro.scene.hq.raytracer.core.World.world;

public class CameraRender {
    public static void main(String[] args) throws IOException {

        Plane bottom = plane();
        bottom.material.pattern = checkers_pattern(color(1, 1, 1), color(0, 0, 0));
        bottom.material.specular = 0.5;
        bottom.material.reflective = 0;

        Plane back = plane();
        back.material = bottom.material;
        back.transform = translation(0, 0, 5).mul(rotation_x(Math.PI/2.0));

        Plane front = plane();
        front.material = bottom.material;
        front.transform = translation(0, 0, 5).mul(rotation_x(Math.PI/2.0));

        Plane left = plane();
        left.material = bottom.material;
        left.transform = translation(-5, 0, 2.5).mul(rotation_y(-Math.PI/2.0).mul(rotation_x(Math.PI/2)));

        Plane right = plane();
        right.material = bottom.material;
        right.transform = translation(5, 0, 2.5).mul(rotation_y(Math.PI/2.0).mul(rotation_x(Math.PI/2)));

        Plane top = plane();
        top.material = bottom.material;
        top.transform = translation(0, 10, 0);

        Sphere middle = sphere();
        middle.material.color = color(0, 0, 0.4);
        middle.material.diffuse = 0.3;
        middle.material.specular = 0.2;
        middle.material.shininess = 1;
        middle.material.reflective = 0.5;
        middle.transform = translation(0, 1, 0).mul(scaling(1, 1, 1));

        World w = world();
        w.objects.addAll(Arrays.asList(bottom, top, back, front, left, right, middle));
        w.light = point_light(point(-4, 8, -4.8), color(1, 1, 1));

        Camera c = camera(1024, 768, Math.PI / 2.0);
        c.transform = view_transform(point(0, 1.5, -4.9), point(0, 1, 0), vector(0, 1, 0));

        long startTime = System.currentTimeMillis();
        Canvas canvas = render(c, w);
        System.out.println("Render took " + (System.currentTimeMillis() - startTime) + " ms");

        List<String> ppm = canvas.toPPM();
        OutputStream out = new BufferedOutputStream(new FileOutputStream("camera-plane.ppm"));
        for (String line : ppm) {
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write('\n');
        }
        out.close();

        System.out.println("Canvas dumped to file");
    }
}
