package ro.scene.hq.raytracer.core;

import java.util.Collections;
import java.util.List;

import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Intersection.intersections;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class Cube extends Shape {

    public static Cube cube() {
        return new Cube();
    }

    static class CheckAxisResult {
        public final double mint;
        public final double maxt;

        public CheckAxisResult(double mint, double maxt) {
            this.mint = mint;
            this.maxt = maxt;
        }
    }

    @Override
    protected List<Intersection> localIntersect(Ray r) {
        CheckAxisResult xCheck = checkAxis(r.origin.x, r.direction.x);
        CheckAxisResult yCheck = checkAxis(r.origin.y, r.direction.y);
        CheckAxisResult zCheck = checkAxis(r.origin.z, r.direction.z);
        double tmin = Math.max(Math.max(xCheck.mint, yCheck.mint), zCheck.mint);
        double tmax = Math.min(Math.min(xCheck.maxt, yCheck.maxt), zCheck.maxt);
        if (tmin > tmax) {
            return Collections.emptyList();
        } else {
            return intersections(
                    intersection(tmin, this),
                    intersection(tmax, this)
            );
        }
    }

    private CheckAxisResult checkAxis(double origin, double direction) {
        double tmin, tmax;
        double tMinNumerator = (-1 - origin);
        double tMaxNumerator = (1 - origin);
        if (Math.abs(direction) >= EPSILON) {
            tmin = tMinNumerator / direction;
            tmax = tMaxNumerator / direction;
        } else {
            tmin = tMinNumerator * INFINITY;
            tmax = tMaxNumerator * INFINITY;
        }

        if (tmin > tmax) {
            return new CheckAxisResult(tmax, tmin);
        } else {
            return new CheckAxisResult(tmin, tmax);
        }
    }

    @Override
    protected Tuple localNormalAt(Tuple localPoint) {
        double maxc = Math.max(Math.max(Math.abs(localPoint.x), Math.abs(localPoint.y)), Math.abs(localPoint.z));
        if (areEqual(maxc, Math.abs(localPoint.x))) {
            return normalize(vector(localPoint.x, 0, 0));
        } else if (areEqual(maxc, Math.abs(localPoint.y))) {
            return normalize(vector(0, localPoint.y, 0));
        } else {
            return normalize(vector(0, 0, localPoint.z));
        }
    }
}
