package ro.scene.hq.raytracer.core;

import java.util.Collections;
import java.util.List;

import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Tuple.EPSILON;
import static ro.scene.hq.raytracer.core.Tuple.vector;

public class Plane extends Shape {

    public static Plane plane() {
        return new Plane();
    }

    @Override
    protected List<Intersection> localIntersect(Ray r) {
        if (Math.abs(r.direction.y) < EPSILON) {
            return Collections.emptyList();
        }

        double t = -r.origin.y / r.direction.y;
        return Collections.singletonList(intersection(t, this));
    }

    @Override
    protected Tuple localNormalAt(Tuple localPoint) {
        return vector(0, 1, 0);
    }
}
