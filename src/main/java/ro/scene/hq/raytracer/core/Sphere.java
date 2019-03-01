package ro.scene.hq.raytracer.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Matrix.identity;
import static ro.scene.hq.raytracer.core.Matrix.inverse;
import static ro.scene.hq.raytracer.core.Ray.transform;
import static ro.scene.hq.raytracer.core.Tuple.dot;
import static ro.scene.hq.raytracer.core.Tuple.point;

public class Sphere {
    public double radius = 1.0;
    public Tuple origin = point(0, 0, 0);
    public Matrix transform = identity(4);

    public static Sphere sphere() {
        return new Sphere();
    }

    public static List<Intersection> intersect(Sphere sphere, Ray ray) {
        Ray r2 = transform(ray, inverse(sphere.transform));

        Tuple sphereToRay = r2.origin.sub(sphere.origin);
        double a = dot(r2.direction, r2.direction);
        double b = 2.0 * dot(r2.direction, sphereToRay);
        double c = dot(sphereToRay, sphereToRay) - 1.0;

        double discriminant = b * b - 4.0 * a * c;
        if (discriminant < 0.0) {
            return Collections.emptyList();
        }

        double t1 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2.0 * a);

        return Arrays.asList(intersection(t1, sphere), intersection(t2, sphere));
    }

    public static void set_transform(Sphere s, Matrix transform) {
        s.transform = transform;
    }
}
