package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static ro.scene.hq.raytracer.core.Cylinder.cylinder;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class CylinderTest {

    @Test
    public void aRayMissesACylinder() {
        Cylinder cyl = cylinder();
        assertRayMissesCylinder(point(1, 0, 0), vector(0, 1, 0), cyl);
        assertRayMissesCylinder(point(0, 0, 0), vector(0, 1, 0), cyl);
        assertRayMissesCylinder(point(0, 0, -5), vector(1, 1, 1), cyl);
    }

    @Test
    public void aRayStrikesACylinder() {
        Cylinder cyl = cylinder();
        assertRayHitsCylinder(point(1, 0, -5), vector(0, 0, 1), cyl, 5, 5);
        assertRayHitsCylinder(point(0, 0, -5), vector(0, 0, 1), cyl, 4, 6);
        assertRayHitsCylinder(point(0.5, 0, -5), vector(0.1, 1, 1), cyl, 6.80798, 7.08872);
    }

    @Test
    public void normalVectorOnACylinder() {
        Cylinder cyl = cylinder();
        assertEqualTuples(cyl.localNormalAt(point(1, 0, 0)), vector(1, 0, 0));
        assertEqualTuples(cyl.localNormalAt(point(0, 5, -1)), vector(0, 0, -1));
        assertEqualTuples(cyl.localNormalAt(point(0, -2, 1)), vector(0, 0, 1));
        assertEqualTuples(cyl.localNormalAt(point(-1, 1, 0)), vector(-1, 0, 0));
    }

    @Test
    public void theDefaultMinimumAndMaximumForACylinder() {
        Cylinder cyl = cylinder();
        assertTrue(areEqual(cyl.minimum, -INFINITY));
        assertTrue(areEqual(cyl.maximum, INFINITY));
    }

    @Test
    public void intersectingAConstrainedCylinder() {
        Cylinder cyl = cylinder();
        cyl.minimum = 1;
        cyl.maximum = 2;
        assertRayCylinderIntersectionCount(point(0, 1.5, 0), vector(0.1, 1, 0), cyl, 0);
        assertRayCylinderIntersectionCount(point(0, 3, -5), vector(0, 0, 1), cyl, 0);
        assertRayCylinderIntersectionCount(point(0, 0, -5), vector(0, 0, 1), cyl, 0);
        assertRayCylinderIntersectionCount(point(0, 2, -5), vector(0, 0, 1), cyl, 0);
        assertRayCylinderIntersectionCount(point(0, 1, -5), vector(0, 0, 1), cyl, 0);
        assertRayCylinderIntersectionCount(point(0, 1.5, -2), vector(0, 0, 1), cyl, 2);
    }

    @Test
    public void theDefaultClosedValueForACylinder() {
        Cylinder cyl = cylinder();
        assertThat(cyl.closed, is(false));
    }

    @Test
    public void intersectingTheCapsOfAClosedCylinder() {
        Cylinder cyl = cylinder();
        cyl.minimum = 1;
        cyl.maximum = 2;
        cyl.closed = true;
        assertRayCylinderIntersectionCount(point(0, 3, 0), vector(0, -1, 0), cyl, 2);
        assertRayCylinderIntersectionCount(point(0, 3, -2), vector(0, -1, 2), cyl, 2);
        assertRayCylinderIntersectionCount(point(0, 4, -2), vector(0, -1, 1), cyl, 2);
        assertRayCylinderIntersectionCount(point(0, 0, -2), vector(0, 1, 2), cyl, 2);
        assertRayCylinderIntersectionCount(point(0, -1, -2), vector(0, 1, 1), cyl, 2);
    }

    @Test
    public void theNormalVectorOnACylinderEndCaps() {
        Cylinder cyl = cylinder();
        cyl.minimum = 1;
        cyl.maximum = 2;
        cyl.closed = true;
        assertEqualTuples(cyl.localNormalAt(point(0, 1, 0)), vector(0, -1, 0));
        assertEqualTuples(cyl.localNormalAt(point(0.5, 1, 0)), vector(0, -1, 0));
        assertEqualTuples(cyl.localNormalAt(point(0, 1, 0.5)), vector(0, -1, 0));
        assertEqualTuples(cyl.localNormalAt(point(0, 2, 0)), vector(0, 1, 0));
        assertEqualTuples(cyl.localNormalAt(point(0.5, 2, 0)), vector(0, 1, 0));
        assertEqualTuples(cyl.localNormalAt(point(0, 2, 0.5)), vector(0, 1, 0));
    }

    private void assertRayCylinderIntersectionCount(Tuple rayOrigin, Tuple rayDirection, Cylinder cylinder, int intersectionCount) {
        Ray ray = ray(rayOrigin, rayDirection);
        assertThat(cylinder.localIntersect(ray).size(), is(equalTo(intersectionCount)));
    }

    private void assertRayMissesCylinder(Tuple rayOrigin, Tuple rayDirection, Cylinder cylinder) {
        Ray ray = ray(rayOrigin, normalize(rayDirection));
        assertThat(cylinder.localIntersect(ray).isEmpty(), is(true));
    }

    private void assertRayHitsCylinder(Tuple rayOrigin, Tuple rayDirection, Cylinder cylinder, double t0, double t1) {
        Ray ray = ray(rayOrigin, normalize(rayDirection));
        List<Intersection> xs = cylinder.localIntersect(ray);
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