package ro.scene.hq.raytracer.core;

public class StripePattern extends Pattern {
    public final Tuple a;
    public final Tuple b;

    public StripePattern(Tuple a, Tuple b) {
        this.a = a;
        this.b = b;
    }

    @Override
    Tuple colorAt(Tuple point) {
        long integralValue = (long) Math.floor(point.x);
        return (Math.abs(integralValue) % 2) == 0 ? this.a : this.b;
    }

    public static StripePattern stripe_pattern(Tuple colorA, Tuple colorB) {
        return new StripePattern(colorA, colorB);
    }
}
