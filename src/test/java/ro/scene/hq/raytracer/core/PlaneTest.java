package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Plane.plane;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class PlaneTest {

    @Test
    public void normalOfAPlaneIsConstantEverywhere() {
        Plane p = plane();
        Tuple n1 = p.localNormalAt(point(0, 0, 0));
        Tuple n2 = p.localNormalAt(point(10, 0, -10));
        Tuple n3 = p.localNormalAt(point(-5, 0, 150));

        assertTupleEquals(n1, vector(0, 1, 0));
        assertTupleEquals(n2, vector(0, 1, 0));
        assertTupleEquals(n3, vector(0, 1, 0));
    }

    @Test
    public void intersectWithARayParallelToThePlane() {
        Plane p = plane();
        Ray r = ray(point(0, 10, 0), vector(0, 0, 1));
        List<Intersection> xs = p.localIntersect(r);
        assertThat(xs.isEmpty(), is(true));
    }

    @Test
    public void intersectWithACoplanarRay() {
        Plane p = plane();
        Ray r = ray(point(0, 0, 0), vector(0, 0, 1));
        List<Intersection> xs = p.localIntersect(r);
        assertThat(xs.isEmpty(), is(true));
    }

    @Test
    public void aRayInterestingThePlaneFromAbove() {
        Plane p = plane();
        Ray r = ray(point(0, 1, 0), vector(0, -1, 0));

        List<Intersection> xs = p.localIntersect(r);

        assertThat(xs.size(), is(1));
        Intersection i = xs.get(0);
        assertThat(i.t, is(equalTo(1.0)));
        assertThat(i.object, is(p));
    }

    @Test
    public void aRayInterestingThePlaneFromBelow() {
        Plane p = plane();
        Ray r = ray(point(0, -1, 0), vector(0, 1, 0));

        List<Intersection> xs = p.localIntersect(r);

        assertThat(xs.size(), is(1));
        Intersection i = xs.get(0);
        assertThat(i.t, is(equalTo(1.0)));
        assertThat(i.object, is(p));
    }

    private void assertTupleEquals(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}