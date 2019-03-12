package ro.scene.hq.raytracer.core;

import java.util.List;

import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Ray.transform;
import static ro.scene.hq.raytracer.core.Tuple.normalize;

public abstract class Shape {
    public Matrix transform = identity(4);
    public Material material = Material.material();

    public static void set_transform(Shape s, Matrix transform) {
        s.transform = transform;
    }

    protected abstract List<Intersection> localIntersect(Ray r);

    public static List<Intersection> intersect(Shape s, Ray ray) {
        Ray localRay = transform(ray, inverse(s.transform));
        return s.localIntersect(localRay);
    }

    protected abstract Tuple localNormalAt(Tuple localPoint);

    public static Tuple normal_at(Shape s, Tuple point) {
        Tuple localPoint = inverse(s.transform).mul(point);
        Tuple localNormal = s.localNormalAt(localPoint);
        Tuple worldNormal = transpose(inverse(s.transform)).mul(localNormal);
        worldNormal.w = 0;
        return normalize(worldNormal);
    }


}
