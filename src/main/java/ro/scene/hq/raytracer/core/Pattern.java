package ro.scene.hq.raytracer.core;

import static ro.scene.hq.raytracer.core.Matrix.identity;
import static ro.scene.hq.raytracer.core.Matrix.inverse;

public class Pattern {
    public final Tuple a;
    public final Tuple b;
    public Matrix transform = identity(4);

    public Pattern(Tuple a, Tuple b) {
        this.a = a;
        this.b = b;
    }

    Tuple at(Tuple point) {
        long integralValue = (long)Math.floor(point.x);
        return (Math.abs(integralValue) % 2) == 0 ? this.a : this.b;
    }

    public static void set_pattern_transform(Pattern pattern, Matrix transform) {
        pattern.transform = transform;
    }

    public static Pattern stripe_pattern(Tuple colorA, Tuple colorB) {
        return new Pattern(colorA, colorB);
    }

    public static Tuple stripe_at_object(Pattern pattern, Shape object, Tuple point) {
        Tuple pointInObjectSpace = inverse(object.transform).mul(point);
        Tuple pointInPatternSpace = inverse(pattern.transform).mul(pointInObjectSpace);
        return pattern.at(pointInPatternSpace);
    }
}
