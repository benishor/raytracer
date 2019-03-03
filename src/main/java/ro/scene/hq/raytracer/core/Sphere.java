package ro.scene.hq.raytracer.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Ray.transform;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class Sphere {
    public double radius = 1.0;
    public Tuple origin = point(0, 0, 0);
    public Matrix transform = identity(4);
    public Material material = Material.material();

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

    public static Tuple normal_at(Sphere s, Tuple point) {
        Tuple objectPoint = inverse(s.transform).mul(point);
        Tuple objectNormal = objectPoint.sub(s.origin);
        Tuple worldNormal = transpose(inverse(s.transform)).mul(objectNormal);
        worldNormal.w = 0;
        return normalize(worldNormal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sphere sphere = (Sphere) o;
        return Double.compare(sphere.radius, radius) == 0 &&
                origin.equals(sphere.origin) &&
                transform.equals(sphere.transform) &&
                material.equals(sphere.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(radius, origin, transform, material);
    }
}
