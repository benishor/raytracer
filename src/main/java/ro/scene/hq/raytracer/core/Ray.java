package ro.scene.hq.raytracer.core;

import static ro.scene.hq.raytracer.core.Tuple.point;

public class Ray {
    public final Tuple origin;
    public final Tuple direction;

    public Ray(Tuple origin, Tuple direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public static Ray ray(Tuple origin, Tuple direction) {
        return new Ray(origin, direction);
    }

    public static Tuple position(Ray r, double t) {
        return r.origin.add(r.direction.mul(t));
    }
}
