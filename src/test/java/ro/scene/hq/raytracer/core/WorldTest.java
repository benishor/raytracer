package ro.scene.hq.raytracer.core;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ro.scene.hq.raytracer.core.Computations.prepare_computations;
import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Light.point_light;
import static ro.scene.hq.raytracer.core.Matrix.scaling;
import static ro.scene.hq.raytracer.core.Matrix.translation;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.*;
import static ro.scene.hq.raytracer.core.World.*;


public class WorldTest {

//    @Before
//    public void setUp() throws Exception {
//        Shape.NEXT_ID = 1L;
//    }

    @Test
    public void creatingAWorld() {
        World w = world();

        assertThat(w.objects.isEmpty(), is(true));
        assertThat(w.light, is(nullValue()));
    }

    @Test
    public void theDefaultWorld() {
        Light light = point_light(point(-10, 10, -10), color(1, 1, 1));

        Sphere s1 = sphere();
        s1.material.color = color(0.8, 1.0, 0.6);
        s1.material.diffuse = 0.7;
        s1.material.specular = 0.2;

        Sphere s2 = sphere();
        s2.transform = scaling(0.5, 0.5, 0.5);

        World w = default_world();
        assertThat(w.light, is(equalTo(light)));

        assertThat(w.contains(s1), is(true));
        assertThat(w.contains(s2), is(true));
    }

    @Test
    public void intersectAWorldWithARay() {
        World w = default_world();
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));

        List<Intersection> xs = intersect_world(w, r);

        assertThat(xs.size(), is(4));
        assertThat(xs.get(0).t, is(equalTo(4.0)));
        assertThat(xs.get(1).t, is(equalTo(4.5)));
        assertThat(xs.get(2).t, is(equalTo(5.5)));
        assertThat(xs.get(3).t, is(equalTo(6.0)));
    }

    @Test
    public void precomputingTheStateOfAnIntersection() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Sphere shape = sphere();
        Intersection i = intersection(4, shape);

        Computations comps = prepare_computations(i, r);
        assertThat(comps.t, is(equalTo(i.t)));
        assertThat(comps.object, is(i.object));
        assertEqualTuples(comps.point, point(0, 0, -1));
        assertEqualTuples(comps.eyev, vector(0, 0, -1));
        assertEqualTuples(comps.normalv, vector(0, 0, -1));
    }

    @Test
    public void theHitWhenAnIntersectionOccursOnTheOutside() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Sphere shape = sphere();
        Intersection i = intersection(4, shape);
        Computations comps = prepare_computations(i, r);
        assertThat(comps.inside, is(false));
    }

    @Test
    public void theHitWhenAnIntersectionOccursOnTheInside() {
        Ray r = ray(point(0, 0, 0), vector(0, 0, 1));
        Sphere shape = sphere();
        Intersection i = intersection(1, shape);
        Computations comps = prepare_computations(i, r);
        assertEqualTuples(comps.point, point(0, 0, 1));
        assertEqualTuples(comps.eyev, vector(0, 0, -1));
        assertThat(comps.inside, is(true));
        // normal would have been 0, 0, 1 but it is inverted due to being inside.
        // this ensures we get proper lighting
        assertEqualTuples(comps.normalv, vector(0, 0, -1));
    }

    @Test
    public void shadingAnIntersection() {
        World w = default_world();
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Shape shape = w.objects.get(0);
        Intersection i = intersection(4, shape);

        Computations comps = prepare_computations(i, r);

        Tuple c = shade_hit(w, comps);
        assertEqualTuples(c, color(0.38066, 0.47583, 0.2855));
    }

    @Test
    public void shadingAnIntersectionFromInside() {
        World w = default_world();
        w.light = point_light(point(0, 0.25, 0), color(1, 1, 1));
        Ray r = ray(point(0, 0, 0), vector(0, 0, 1));
        Shape shape = w.objects.get(1);
        Intersection i = intersection(0.5, shape);

        Computations comps = prepare_computations(i, r);

        Tuple c = shade_hit(w, comps);
        assertEqualTuples(c, color(0.90498, 0.90498, 0.90498));
    }

    @Test
    public void theColorWhenARayMisses() {
        World w = default_world();
        Ray r = ray(point(0, 0, -5), vector(0, 1, 0));
        Tuple c = color_at(w, r);
        assertEqualTuples(c, color(0, 0, 0));
    }

    @Test
    public void theColorWhenARayHits() {
        World w = default_world();
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Tuple c = color_at(w, r);
        assertEqualTuples(c, color(0.38066, 0.47583, 0.2855));
    }

    @Test
    public void noShadowWhenNothingIsCollinearWithPointAndLight() {
        World w = default_world();
        Tuple p = point(0, 10, 0);
        assertThat(is_shadowed(w, p), is(false));
    }

    @Test
    public void shadowWhenAnObjectIsBetweenThePointAndLight() {
        World w = default_world();
        Tuple p = point(10, -10, 10);
        assertThat(is_shadowed(w, p), is(true));
    }

    @Test
    public void noShadowWhenObjectIsBehindTheLight() {
        World w = default_world();
        Tuple p = point(-20, 20, -20);
        assertThat(is_shadowed(w, p), is(false));
    }

    @Test
    public void noShadowWhenObjectIsBehindThePoint() {
        World w = default_world();
        Tuple p = point(-2, 2, -2);
        assertThat(is_shadowed(w, p), is(false));
    }

    @Test
    public void theColorWithAnIntersectionBehindTheRay() {
        World w = default_world();
        Shape outer = w.objects.get(0);
        outer.material.ambient = 1;
        Shape inner = w.objects.get(1);
        inner.material.ambient = 1;
        Ray r = ray(point(0, 0, 0.75), vector(0, 0, -1));
        Tuple c = color_at(w, r);
        assertEqualTuples(c, inner.material.color);
    }

    @Test
    public void theHitShouldOffsetThePoint() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Sphere shape = sphere();
        shape.transform = translation(0, 0, 1);
        Intersection i = intersection(5, shape);
        Computations comps = prepare_computations(i, r);
        assertTrue(comps.over_point.z < -EPSILON/2.0);
        assertTrue(comps.point.z > comps.over_point.z);
    }

    @Test
    public void shadeHitIsGivenAnIntersectionInShadow() {
        World w = world();
        w.light = point_light(point(0, 0, -10), color(1, 1, 1));

        Sphere s1 = sphere();
        w.objects.add(s1);

        Sphere s2 = sphere();
        s2.transform = translation(0, 0, 10);
        w.objects.add(s2);

        Ray r = ray(point(0, 0, 5), vector(0, 0, 1));
        Intersection i = intersection(4, s2);
        Computations comps = prepare_computations(i, r);
        Tuple c = shade_hit(w, comps);
        assertEqualTuples(c, color(0.1, 0.1, 0.1));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}
