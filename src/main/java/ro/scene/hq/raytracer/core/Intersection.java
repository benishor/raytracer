package ro.scene.hq.raytracer.core;

import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return Double.compare(that.t, t) == 0 &&
                object.equals(that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, object);
    }
}
