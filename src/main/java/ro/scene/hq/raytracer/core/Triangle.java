package ro.scene.hq.raytracer.core;

import java.util.Collections;
import java.util.List;

import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Intersection.intersections;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class Triangle extends Shape {

    public Tuple p1, p2, p3;
    public Tuple e1, e2;
    public Tuple normal;

    @Override
    protected List<Intersection> localIntersect(Ray r) {
        Tuple dirCrossE2 = cross(r.direction, e2);
        double det = dot(e1, dirCrossE2);
        // test for ray parallel to the triangle
        if (Math.abs(det) < EPSILON) {
            return Collections.emptyList();
        }

        double f = 1.0 / det;
        Tuple p1ToOrigin = r.origin.sub(p1);
        double u = f * dot(p1ToOrigin, dirCrossE2);
        if (u < 0 || u > 1) {
            return Collections.emptyList();
        }

        Tuple originCrossE1 = cross(p1ToOrigin, e1);
        double v = f * dot(r.direction, originCrossE1);
        if (v < 0 || (u + v) > 1) {
            return Collections.emptyList();
        }

        // finally we have an intersection
        double t = f * dot(e2, originCrossE1);
        return intersections(intersection(t, this));
    }

    @Override
    protected Tuple localNormalAt(Tuple localPoint) {
        return normal;
    }

    public Triangle(Tuple p1, Tuple p2, Tuple p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        e1 = p2.sub(p1);
        e2 = p3.sub(p1);
        normal = normalize(cross(e2, e1));
    }

    public static Triangle triangle(Tuple p1, Tuple p2, Tuple p3) {
        return new Triangle(p1, p2, p3);
    }
}
