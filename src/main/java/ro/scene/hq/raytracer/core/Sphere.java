package ro.scene.hq.raytracer.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class Sphere extends Shape {
    public double radius = 1.0;
    public Tuple origin = point(0, 0, 0);

    public static Sphere sphere() {
        return new Sphere();
    }

    public static Sphere glass_sphere() {
        Sphere sphere = new Sphere();
        sphere.material.transparency = 1.0;
        sphere.material.refractiveIndex = 1.5;
        return sphere;
    }

    @Override
    protected List<Intersection> localIntersect(Ray r) {
        Tuple sphereToRay = r.origin.sub(origin);
        double a = dot(r.direction, r.direction);
        double b = 2.0 * dot(r.direction, sphereToRay);
        double c = dot(sphereToRay, sphereToRay) - 1.0;

        double discriminant = b * b - 4.0 * a * c;
        if (discriminant < 0.0) {
            return Collections.emptyList();
        }

        double t1 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2.0 * a);

        return Arrays.asList(intersection(t1, this), intersection(t2, this));
    }

    @Override
    protected Tuple localNormalAt(Tuple localPoint) {
        return vector(localPoint.x, localPoint.y, localPoint.z);
    }
}
