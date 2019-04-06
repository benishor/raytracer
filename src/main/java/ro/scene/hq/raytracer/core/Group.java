package ro.scene.hq.raytracer.core;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static ro.scene.hq.raytracer.core.Matrix.inverse;
import static ro.scene.hq.raytracer.core.Ray.transform;

public class Group extends Shape {
    public List<Shape> shapes = new LinkedList<>();
    public String name;

    @Override
    protected List<Intersection> localIntersect(Ray r) {
        List<Intersection> allIntersections = new LinkedList<>();
        for (Shape s : shapes) {
            allIntersections.addAll(s.localIntersect(transform(r, inverse(transform.mul(s.transform)))));
        }
        allIntersections.sort(Comparator.comparingDouble(a -> a.t));
        return allIntersections;
    }

    @Override
    protected Tuple localNormalAt(Tuple localPoint) {
        throw new UnsupportedOperationException("This should not be used! Normals should be computed by using the hit shape's instance!");
    }

    public static Group group() {
        return new Group();
    }

    public void add(Shape s) {
        s.parent = this;
        shapes.add(s);
    }
}
