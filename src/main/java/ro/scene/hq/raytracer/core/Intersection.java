package ro.scene.hq.raytracer.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Intersection {
    public final double t;
    public final Shape object;

    public Intersection(double t, Shape object) {
        this.t = t;
        this.object = object;
    }

    public static Intersection intersection(double t, Shape object) {
        return new Intersection(t, object);
    }

    public static List<Intersection> intersections(Intersection... entries) {
        return Arrays.asList(entries);
    }

    public static Optional<Intersection> hit(List<Intersection> xs) {
        xs.sort(Comparator.comparingDouble(a -> a.t));
        for (Intersection i : xs) {
            if (i.t > 0.0) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}
