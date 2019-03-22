package ro.scene.hq.raytracer.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Tuple.*;
import static ro.scene.hq.raytracer.core.Tuple.vector;

public class Cone extends Shape {
    public double minimum = -INFINITY;
    public double maximum = INFINITY;
    public boolean closed = false;

    @Override
    protected List<Intersection> localIntersect(Ray r) {
        double a = r.direction.x * r.direction.x - r.direction.y * r.direction.y + r.direction.z * r.direction.z;
        double b = 2 * r.origin.x * r.direction.x - 2 * r.origin.y * r.direction.y + 2 * r.origin.z * r.direction.z;
        double c = r.origin.x * r.origin.x - r.origin.y * r.origin.y + r.origin.z * r.origin.z;

        List<Intersection> xs = new ArrayList<>(2);
        if (areEqual(a, 0)) {
            if (areEqual(b, 0)) {
                return Collections.emptyList();
            } else {
                double t = -c / (2 * b);
                xs.add(intersection(t, this));
            }
        }

        double disc = b * b - 4 * a * c;
        if (disc < 0) {
            return Collections.emptyList();
        }

        double t0 = (-b - Math.sqrt(disc)) / (2 * a);
        double t1 = (-b + Math.sqrt(disc)) / (2 * a);

        if (t0 > t1) {
            double tmp = t0;
            t0 = t1;
            t1 = tmp;
        }

        double y0 = r.origin.y + t0 * r.direction.y;
        if (minimum < y0 && y0 < maximum) {
            xs.add(intersection(t0, this));
        }

        double y1 = r.origin.y + t1 * r.direction.y;
        if (minimum < y1 && y1 < maximum) {
            xs.add(intersection(t1, this));
        }

        intersectCaps(r, xs);

        return xs;
    }

    @Override
    protected Tuple localNormalAt(Tuple localPoint) {
        double dist = localPoint.x * localPoint.x + localPoint.z * localPoint.z;
        if (dist < 1 && localPoint.y >= (maximum - EPSILON)) {
            return vector(0, 1, 0);
        } else if (dist < 1 && localPoint.y <= (minimum + EPSILON)) {
            return vector(0, -1, 0);
        } else {
            double y = Math.sqrt(dist);
            if (localPoint.y > 0) {
                y = -y;
            }
            return vector(localPoint.x, y, localPoint.z);
        }
    }

    public static Cone cone() {
        return new Cone();
    }

    private void intersectCaps(Ray ray, List<Intersection> xs) {
        // caps only matter if the cone is closed, and might possibly be
        // intersected by the ray.
        if (!closed) return;

        // check for an intersection with the lower end cap by intersecting
        // the ray with the plane at y=cyl.minimum
        double t = (minimum - ray.origin.y) / ray.direction.y;
        if (checkCap(ray, t)) {
            xs.add(intersection(t, this));
        }

        // check for an intersection with the upper end cap by intersecting
        // the ray with the plane at y=cyl.maximum
        t = (maximum - ray.origin.y) / ray.direction.y;
        if (checkCap(ray, t)) {
            xs.add(intersection(t, this));
        }
    }

    // checks to see if the intersection at `t` is within a radius
    // of 1 (the radius of your cylinders) from the y axis.
    private boolean checkCap(Ray ray, double t) {
        double x = ray.origin.x + t * ray.direction.x;
        double z = ray.origin.z + t * ray.direction.z;
        double y = ray.origin.y + t * ray.direction.y;
        return (x * x + z * z) <= y * y;
    }

}
