package ro.scene.hq.raytracer.core;

public class GradientPattern extends Pattern {
    public final Tuple a;
    public final Tuple b;
    private final Tuple distance;

    public GradientPattern(Tuple a, Tuple b) {
        this.a = a;
        this.b = b;
        distance = b.sub(a);
    }

    @Override
    Tuple colorAt(Tuple point) {
        double fraction = point.x - Math.floor(point.x);
        return a.add(distance.mul(fraction));
    }

    public static GradientPattern gradient_pattern(Tuple a, Tuple b) {
        return new GradientPattern(a, b);
    }
}
