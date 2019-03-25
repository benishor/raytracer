package ro.scene.hq.raytracer.core;

import java.util.List;
import java.util.Objects;

import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Ray.transform;
import static ro.scene.hq.raytracer.core.Tuple.*;

public abstract class Shape {
    public Matrix transform = identity(4);
    public Material material = Material.material();
    public Shape parent;

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
        Tuple localPoint = s.worldToObject(point);
        Tuple localNormal = s.localNormalAt(localPoint);
        Tuple worldNormal = normalize(s.normalToWorld(localNormal));
//        worldNormal.w = 0;
        return worldNormal;
    }

    public Tuple worldToObject(Tuple worldPoint) {
        Matrix allTransforms = identity(4);
        Shape currentShape = this;
        while (currentShape != null) {
            allTransforms = currentShape.transform.mul(allTransforms);
            currentShape = currentShape.parent;
        }
        return inverse(allTransforms).mul(worldPoint);
    }

    public Tuple normalToWorld(Tuple localNormal) {
        Tuple currentNormal = normalize(vector(localNormal.x, localNormal.y, localNormal.z));
        Shape currentShape = this;
        while (currentShape != null) {
            currentNormal = normalize(transpose(inverse(currentShape.transform)).mul(currentNormal));
            currentShape = currentShape.parent;
        }
        return currentNormal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape)) return false;
        Shape shape = (Shape) o;
        return transform.equals(shape.transform) &&
                material.equals(shape.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transform, material);
    }
}
