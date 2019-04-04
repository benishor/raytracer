package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Triangle.triangle;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class TriangleTest {

    @Test
    public void constructingATriangle() {
        Tuple p1 = point(0, 1, 0);
        Tuple p2 = point(-1, 0, 0);
        Tuple p3 = point(1, 0, 0);
        Triangle t = triangle(p1, p2, p3);

        assertEqualTuples(t.p1, p1);
        assertEqualTuples(t.p2, p2);
        assertEqualTuples(t.p3, p3);
        assertEqualTuples(t.e1, vector(-1, -1, 0));
        assertEqualTuples(t.e2, vector(1, -1, 0));
        assertEqualTuples(t.normal, vector(0, 0, -1));
    }

    @Test
    public void findingTheNormalOnATriangle() {
        Triangle t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0));
        Tuple n1 = t.localNormalAt(point(0, 0.5, 0));
        Tuple n2 = t.localNormalAt(point(-0.5, 0.75, 0));
        Tuple n3 = t.localNormalAt(point(0.5, 0.25, 0));

        assertEqualTuples(n1, t.normal);
        assertEqualTuples(n2, t.normal);
        assertEqualTuples(n3, t.normal);
    }

    @Test
    public void intersectingARayParallelToTheTriangle() {
        Triangle t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0));
        Ray r = ray(point(0, -1, -2), vector(0, 1, 0));

        List<Intersection> xs = t.localIntersect(r);
        assertThat(xs.isEmpty(), is(true));
    }

    @Test
    public void aRayMissesTheP1P3Edge() {
        Triangle t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0));
        Ray r = ray(point(1, 1, -2), vector(0, 0, 1));

        List<Intersection> xs = t.localIntersect(r);
        assertThat(xs.isEmpty(), is(true));
    }

    @Test
    public void aRayMissesTheP1P2Edge() {
        Triangle t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0));
        Ray r = ray(point(-1, 1, -2), vector(0, 0, 1));

        List<Intersection> xs = t.localIntersect(r);
        assertThat(xs.isEmpty(), is(true));
    }

    @Test
    public void aRayMissesTheP2P3Edge() {
        Triangle t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0));
        Ray r = ray(point(0, -1, -2), vector(0, 0, 1));

        List<Intersection> xs = t.localIntersect(r);
        assertThat(xs.isEmpty(), is(true));
    }

    @Test
    public void aRayStrikesATriangle() {
        Triangle t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0));
        Ray r = ray(point(0, 0.5, -2), vector(0, 0, 1));

        List<Intersection> xs = t.localIntersect(r);
        assertThat(xs.size(), is(1));
        assertTrue(areEqual(xs.get(0).t, 2));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}