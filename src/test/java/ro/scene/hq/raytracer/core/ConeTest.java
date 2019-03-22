package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ro.scene.hq.raytracer.core.Cone.cone;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class ConeTest {

    @Test
    public void intersectingAConeWithARay() {
        Cone cone = cone();
        assertRayHitsCone(point(0, 0, -5), vector(0, 0, 1), cone, 5, 5);
        assertRayHitsCone(point(0, 0, -5), vector(1, 1, 1), cone, 8.66025, 8.66025);
        assertRayHitsCone(point(1, 1, -5), vector(-0.5, -1, 1), cone, 4.55006, 49.44994);
    }

    @Test
    public void intersectingAConeWithARayParallelToOneOfItsHalves() {
        Cone cone = cone();
        Ray ray = ray(point(0, 0, -1), normalize(vector(0, 1, 1)));

        List<Intersection> xs = cone.localIntersect(ray);
        assertThat(xs.size(), is(1));
        assertTrue(areEqual(xs.get(0).t, 0.35355));
    }

    @Test
    public void intersectingAConeEndCaps() {
        Cone cone = cone();
        cone.minimum = -0.5;
        cone.maximum = 0.5;
        cone.closed = true;

        assertRayConeIntersectionCount(point(0, 0, -5), vector(0, 1, 0), cone, 0);
        assertRayConeIntersectionCount(point(0, 0, -0.25), vector(0, 1, 1), cone, 2);
        assertRayConeIntersectionCount(point(0, 0, -0.25), vector(0, 1, 0), cone, 4);
    }

    @Test
    public void computingTheNormalVectorOnACone() {
        Cone cone = cone();
        assertEqualTuples(cone.localNormalAt(point(0, 0, 0)), vector(0, 0, 0));
        assertEqualTuples(cone.localNormalAt(point(1, 1, 1)), vector(1, -Math.sqrt(2.0), 1));
        assertEqualTuples(cone.localNormalAt(point(-1, -1, 0)), vector(-1, 1, 0));
    }

    private void assertRayConeIntersectionCount(Tuple rayOrigin, Tuple rayDirection, Cone cone, int intersectionCount) {
        Ray ray = ray(rayOrigin, normalize(rayDirection));
        List<Intersection> xs = cone.localIntersect(ray);
        assertThat(xs.size(), is(intersectionCount));
    }

    private void assertRayHitsCone(Tuple rayOrigin, Tuple rayDirection, Cone cone, double t0, double t1) {
        Ray ray = ray(rayOrigin, normalize(rayDirection));
        List<Intersection> xs = cone.localIntersect(ray);
        assertThat(xs.size(), is(2));
        assertTrue(areEqual(xs.get(0).t, t0));
        assertTrue(areEqual(xs.get(1).t, t1));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}