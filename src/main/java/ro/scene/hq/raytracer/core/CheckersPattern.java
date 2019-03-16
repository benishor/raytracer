package ro.scene.hq.raytracer.core;

public class CheckersPattern extends Pattern {
    public final Tuple a;
    public final Tuple b;

    public CheckersPattern(Tuple a, Tuple b) {
        this.a = a;
        this.b = b;
    }

    @Override
    Tuple colorAt(Tuple point) {
        long x = (long) Math.abs(Math.floor(point.x));
        long y = (long) Math.abs(Math.floor(point.y));
        long z = (long) Math.abs(Math.floor(point.z));
        if ((x + y + z) % 2 == 0) {
            return a;
        } else {
            return b;
        }
    }

    public static CheckersPattern checkers_pattern(Tuple a, Tuple b) {
        return new CheckersPattern(a, b);
    }
}
