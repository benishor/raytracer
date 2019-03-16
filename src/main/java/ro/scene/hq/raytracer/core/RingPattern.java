package ro.scene.hq.raytracer.core;

public class RingPattern extends Pattern {
    public final Tuple a;
    public final Tuple b;

    public RingPattern(Tuple a, Tuple b) {
        this.a = a;
        this.b = b;
    }

    @Override
    Tuple colorAt(Tuple point) {
        double distance = Math.sqrt(point.x * point.x + point.z * point.z);
        if (((long) Math.floor(distance) % 2) == 0) {
            return a;
        } else {
            return b;
        }
    }

    public static RingPattern ring_pattern(Tuple a, Tuple b) {
        return new RingPattern(a, b);
    }
}
