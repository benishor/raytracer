package ro.scene.hq.raytracer.core;


import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Intersection.*;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Sphere.*;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class SpheresTest {
    @Test
    public void aRayIntersectsASphereAtTwoPoints() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Sphere s = sphere();
        List<Intersection> xs = intersect(s, r);
        assertThat(xs.size(), is(2));
        assertThat(xs.get(0).t, is(equalTo(4.0)));
        assertThat(xs.get(1).t, is(equalTo(6.0)));
    }

    @Test
    public void aRayIntersectsASpehereAtTangent() {
        Ray r = ray(point(0, 1, -5), vector(0, 0, 1));
        Sphere s = sphere();
        List<Intersection> xs = intersect(s, r);
        assertThat(xs.size(), is(2));
        assertThat(xs.get(0).t, is(equalTo(5.0)));
        assertThat(xs.get(1).t, is(equalTo(5.0)));
    }

    @Test
    public void aRayMissesASphere() {
        Ray r = ray(point(0, 2, -5), vector(0, 0, 1));
        Sphere s = sphere();
        List<Intersection> xs = intersect(s, r);
        assertThat(xs.isEmpty(), is(true));
    }

    @Test
    public void aRayOriginatesInsideASphere() {
        Ray r = ray(point(0, 0, -0), vector(0, 0, 1));
        Sphere s = sphere();
        List<Intersection> xs = intersect(s, r);
        assertThat(xs.size(), is(2));
        assertThat(xs.get(0).t, is(equalTo(-1.0)));
        assertThat(xs.get(1).t, is(equalTo(1.0)));
    }

    @Test
    public void aSphereIsBehindARay() {
        Ray r = ray(point(0, 0, 5), vector(0, 0, 1));
        Sphere s = sphere();
        List<Intersection> xs = intersect(s, r);
        assertThat(xs.size(), is(2));
        assertThat(xs.get(0).t, is(equalTo(-6.0)));
        assertThat(xs.get(1).t, is(equalTo(-4.0)));
    }

    @Test
    public void anIntersectionEncapsulatesTAndObject() {
        Sphere s = sphere();
        Intersection i = intersection(3.5, s);
        assertThat(i.t, is(equalTo(3.5)));
        assertThat(i.object, is(s));
    }

    @Test
    public void aggregatingIntersections() {
        Sphere s = sphere();
        Intersection i1 = intersection(1, s);
        Intersection i2 = intersection(2, s);

        List<Intersection> xs = intersections(i1, i2);

        assertThat(xs.size(), is(2));
        assertThat(xs.get(0).t, is(equalTo(1.0)));
        assertThat(xs.get(1).t, is(equalTo(2.0)));
    }

    @Test
    public void intersectSetsTheObjectOnTheIntersection() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Sphere s = sphere();
        List<Intersection> xs = intersect(s, r);
        assertThat(xs.size(), is(2));
        assertThat(xs.get(0).object, is(equalTo(s)));
        assertThat(xs.get(1).object, is(equalTo(s)));
    }

    @Test
    public void theHitWhenAllIntersectionsHavePositiveT() {
        Sphere s = sphere();
        Intersection i1 = intersection(1, s);
        Intersection i2 = intersection(2, s);
        List<Intersection> xs = intersections(i1, i2);

        Optional<Intersection> h = hit(xs);

        assertThat(h.isPresent(), is(true));
        assertThat(h.get(), is(i1));
    }

    @Test
    public void theHitWhenSomeIntersectionsHaveNegativeT() {
        Sphere s = sphere();
        Intersection i1 = intersection(-1, s);
        Intersection i2 = intersection(1, s);
        List<Intersection> xs = intersections(i1, i2);

        Optional<Intersection> h = hit(xs);

        assertThat(h.isPresent(), is(true));
        assertThat(h.get(), is(i2));
    }

    @Test
    public void theHitWhenAllIntersectionsHaveNegativeT() {
        Sphere s = sphere();
        Intersection i1 = intersection(-1, s);
        Intersection i2 = intersection(-2, s);
        List<Intersection> xs = intersections(i1, i2);

        Optional<Intersection> h = hit(xs);

        assertThat(h.isPresent(), is(false));
    }

    @Test
    public void theHitIsAlwaysTheLowestNonNegativeIntersection() {
        Sphere s = sphere();
        Intersection i1 = intersection(5, s);
        Intersection i2 = intersection(7, s);
        Intersection i3 = intersection(-3, s);
        Intersection i4 = intersection(2, s);
        List<Intersection> xs = intersections(i1, i2, i3, i4);

        Optional<Intersection> h = hit(xs);

        assertThat(h.isPresent(), is(true));
        assertThat(h.get(), is(i4));
    }

    @Test
    public void normalOnASphereAtAPointOnXAxis() {
        Sphere s = sphere();
        Tuple n = normal_at(s, point(1, 0, 0));
        assertThat(n, is(equalTo(vector(1, 0, 0))));
    }

    @Test
    public void normalOnASphereAtAPointOnYAxis() {
        Sphere s = sphere();
        Tuple n = normal_at(s, point(0, 1, 0));
        assertThat(n, is(equalTo(vector(0, 1, 0))));
    }

    @Test
    public void normalOnASphereAtAPointOnZAxis() {
        Sphere s = sphere();
        Tuple n = normal_at(s, point(0, 0, 1));
        assertThat(n, is(equalTo(vector(0, 0, 1))));
    }

    @Test
    public void normalOnASphereAtANonAxialPoint() {
        Sphere s = sphere();
        Tuple n = normal_at(s, point(Math.sqrt(3) / 3.0, Math.sqrt(3) / 3.0, Math.sqrt(3) / 3.0));
        assertThat(n, is(equalTo(vector(Math.sqrt(3) / 3.0, Math.sqrt(3) / 3.0, Math.sqrt(3) / 3.0))));
    }

    @Test
    public void normalIsANormalizedVector() {
        Sphere s = sphere();
        Tuple n = normal_at(s, point(Math.sqrt(3) / 3.0, Math.sqrt(3) / 3.0, Math.sqrt(3) / 3.0));

        assertTupleEquals(n, normalize(n));
    }

    @Test
    public void computingNormalOnATranslatedSphere() {
        Sphere s = sphere();
        Shape.set_transform(s, translation(0, 1, 0));

        Tuple n = normal_at(s, point(0, 1.70711, -0.70711));

        assertTupleEquals(n, vector(0, 0.70711, -0.70711));
    }

    @Test
    public void computingNormalOnATransformedSphere() {
        Sphere s = sphere();
        Shape.set_transform(s, scaling(1, 0.5, 1).mul(rotation_z(Math.PI / 5.0)));

        Tuple n = normal_at(s, point(0, Math.sqrt(2.0) / 2.0, -Math.sqrt(2.0) / 2.0));

        assertTupleEquals(n, vector(0, 0.97014, -0.24254));
    }

    private void assertTupleEquals(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}
