package ro.scene.hq.raytracer.core;

import static ro.scene.hq.raytracer.core.Matrix.identity;
import static ro.scene.hq.raytracer.core.Matrix.inverse;

public abstract class Pattern {
    public Matrix transform = identity(4);

    abstract Tuple colorAt(Tuple point);

    public Tuple colorAtObject(Shape object, Tuple point) {
        Tuple pointInObjectSpace = inverse(object.transform).mul(point);
        Tuple pointInPatternSpace = inverse(transform).mul(pointInObjectSpace);
        return colorAt(pointInPatternSpace);
    }
}
