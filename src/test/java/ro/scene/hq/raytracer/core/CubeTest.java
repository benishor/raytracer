package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static ro.scene.hq.raytracer.core.Cube.cube;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class CubeTest {

    @Test
    public void aRayIntersectsACube() {
        Cube c = cube();
        // facets
        assertCubeIntersection(c, point(5, 0.5, 0), vector(-1, 0, 0), 4, 6);
        assertCubeIntersection(c, point(-5, 0.5, 0), vector(1, 0, 0), 4, 6);
        assertCubeIntersection(c, point(0.5, 5, 0), vector(0, -1, 0), 4, 6);
        assertCubeIntersection(c, point(0.5, -5, 0), vector(0, 1, 0), 4, 6);
        assertCubeIntersection(c, point(0.5, 0, 5), vector(0, 0, -1), 4, 6);
        assertCubeIntersection(c, point(0.5, 0, -5), vector(0, 0, 1), 4, 6);
        // inside
        assertCubeIntersection(c, point(0, 0.5, 0), vector(0, 0, 1), -1, 1);
    }

    @Test
    public void aRayMissesACube() {
        Cube c = cube();
        assertCubeNoIntersection(c, point(-2, 0, 0), vector(0.2673, 0.5345, 0.8018));
        assertCubeNoIntersection(c, point(0, -2, 0), vector(0.8018, 0.2673, 0.5345));
        assertCubeNoIntersection(c, point(0, 0, -2), vector(0.5345, 0.8018, 0.2673));
        assertCubeNoIntersection(c, point(2, 0, 2), vector(0, 0, -1));
        assertCubeNoIntersection(c, point(0, 2, 2), vector(0, -1, 0));
        assertCubeNoIntersection(c, point(2, 2, 0), vector(-1, 0, 0));
    }

    @Test
    public void theNormalOnTheSurfaceOfACube() {
        Cube c = cube();
        assertCubeNormalAtPosition(c, point(1, 0.5, -0.8), vector(1, 0, 0));
        assertCubeNormalAtPosition(c, point(-1, -0.2, 0.9), vector(-1, 0, 0));
        assertCubeNormalAtPosition(c, point(-0.4, 1, -0.1), vector(0, 1, 0));
        assertCubeNormalAtPosition(c, point(0.3, -1, -0.7), vector(0, -1, 0));
        assertCubeNormalAtPosition(c, point(-0.6, 0.3, 1), vector(0, 0, 1));
        assertCubeNormalAtPosition(c, point(0.4, 0.4, -1), vector(0, 0, -1));
        assertCubeNormalAtPosition(c, point(1, 1, 1), vector(1, 0, 0));
        assertCubeNormalAtPosition(c, point(-1, -1, -1), vector(-1, 0, 0));
    }

    private void assertCubeIntersection(Cube c, Tuple rayOrigin, Tuple rayDirection, double t1, double t2) {
        Ray r = ray(rayOrigin, rayDirection);
        List<Intersection> xs = c.localIntersect(r);
        assertThat(xs.size(), is(equalTo(2)));
        assertTrue(areEqual(xs.get(0).t, t1));
        assertTrue(areEqual(xs.get(1).t, t2));
    }

    private void assertCubeNoIntersection(Cube c, Tuple rayOrigin, Tuple rayDirection) {
        Ray r = ray(rayOrigin, rayDirection);
        List<Intersection> xs = c.localIntersect(r);
        assertThat(xs.isEmpty(), is(true));
    }

    private void assertCubeNormalAtPosition(Cube c, Tuple point, Tuple expectedNormal) {
        Tuple actualNormal = c.localNormalAt(point);
        assertEqualTuples(actualNormal, expectedNormal);
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}