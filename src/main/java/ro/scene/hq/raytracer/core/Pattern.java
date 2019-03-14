package ro.scene.hq.raytracer.core;

public class Pattern {
    public final Tuple a;
    public final Tuple b;

    public Pattern(Tuple a, Tuple b) {
        this.a = a;
        this.b = b;
    }

    Tuple at(Tuple point) {
        long integralValue = (long)Math.floor(point.x);
        return (Math.abs(integralValue) % 2) == 0 ? this.a : this.b;
    }

    public static Pattern stripe_pattern(Tuple colorA, Tuple colorB) {
        return new Pattern(colorA, colorB);
    }
}
