package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Material.material;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Shape.*;
import static ro.scene.hq.raytracer.core.ShapesTest.TestShape.test_shape;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class ShapesTest {

    static class TestShape extends Shape {
        public Ray savedRay;

        public static TestShape test_shape() {
            return new TestShape();
        }

        @Override
        protected List<Intersection> localIntersect(Ray r) {
            savedRay = r;
            return null;
        }

        @Override
        protected Tuple localNormalAt(Tuple localPoint) {
            return vector(localPoint.x, localPoint.y, localPoint.z);
        }
    }

    @Test
    public void theDefaultMaterial() {
        Shape s = test_shape();
        assertThat(s.material, is(equalTo(material())));
    }

    @Test
    public void assigningAMaterial() {
        Shape s = test_shape();
        Material m = material();
        m.ambient = 1;
        s.material = m;
        assertThat(s.material, is(equalTo(m)));
    }

    @Test
    public void intersectingAScaledShapeWithARay() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        TestShape s = test_shape();
        set_transform(s, scaling(2, 2, 2));
        List<Intersection> xs = intersect(s, r);

        assertEqualTuples(s.savedRay.origin, point(0, 0, -2.5));
        assertEqualTuples(s.savedRay.direction, vector(0, 0, 0.5));
    }

    @Test
    public void intersectingATranslatedShapeWithARay() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        TestShape s = test_shape();
        set_transform(s, translation(5, 0, 0));
        List<Intersection> xs = intersect(s, r);

        assertEqualTuples(s.savedRay.origin, point(-5, 0, -5));
        assertEqualTuples(s.savedRay.direction, vector(0, 0, 1));
    }

    @Test
    public void computingTheNormalOnATranslatedShape() {
        TestShape s = test_shape();
        set_transform(s, translation(0, 1, 0));
        Tuple n = normal_at(s, point(0, 1.70711, -0.70711));
        assertEqualTuples(n, vector(0, 0.70711, -0.70711));
    }

    @Test
    public void computingTheNormalOnATransformedShape() {
        TestShape s = test_shape();
        set_transform(s, scaling(1, 0.5, 1).mul(rotation_z(Math.PI / 5.0)));
        Tuple n = normal_at(s, point(0, Math.sqrt(2.0) / 2.0, -Math.sqrt(2.0) / 2.0));
        assertEqualTuples(n, vector(0, 0.97014, -0.24254));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }

}