package ro.scene.hq.raytracer.core;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ro.scene.hq.raytracer.core.Computations.prepare_computations;
import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Intersection.intersections;
import static ro.scene.hq.raytracer.core.Light.lighting;
import static ro.scene.hq.raytracer.core.Light.point_light;
import static ro.scene.hq.raytracer.core.Material.material;
import static ro.scene.hq.raytracer.core.Matrix.scaling;
import static ro.scene.hq.raytracer.core.Matrix.translation;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Sphere.glass_sphere;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.StripePattern.stripe_pattern;
import static ro.scene.hq.raytracer.core.Tuple.*;
import static ro.scene.hq.raytracer.core.World.schlick;

public class IntersectionsTest {
    @Test
    public void precomputingTheReflectionVector() {
        Shape shape = sphere();
        Ray r = ray(point(0, 1, -1), vector(0, -Math.sqrt(2.0)/2.0, Math.sqrt(2.0)/2.0));
        Intersection i = intersection(Math.sqrt(2.0), shape);
        Computations comps = prepare_computations(i, r, Collections.emptyList());
        assertEqualTuples(comps.reflectv, vector(0, Math.sqrt(2.0)/2.0, -Math.sqrt(2.0)/2.0));
    }

    @Test
    public void findingN1AndN2AtVariousIntersections() {
        Sphere A = glass_sphere();
        A.transform = scaling(2, 2, 2);
        A.material.refractiveIndex = 1.5;

        Sphere B = glass_sphere();
        B.transform = translation(0, 0, -0.25);
        B.material.refractiveIndex = 2.0;

        Sphere C = glass_sphere();
        C.transform = translation(0, 0, 0.25);
        C.material.refractiveIndex = 2.5;

        Ray r = ray(point(0, 0, -4), vector(0, 0, 1));
        List<Intersection> xs = intersections(
                intersection(2, A),
                intersection(2.75, B),
                intersection(3.25, C),
                intersection(4.75, B),
                intersection(5.25, C),
                intersection(6, A)
        );

        Computations comps;

        comps = prepare_computations(xs.get(0), r, xs);
        assertTrue(areEqual(comps.n1, 1.0) && areEqual(comps.n2, 1.5));

        comps = prepare_computations(xs.get(1), r, xs);
        assertTrue(areEqual(comps.n1, 1.5) && areEqual(comps.n2, 2.0));

        comps = prepare_computations(xs.get(2), r, xs);
        assertTrue(areEqual(comps.n1, 2.0) && areEqual(comps.n2, 2.5));

        comps = prepare_computations(xs.get(3), r, xs);
        assertTrue(areEqual(comps.n1, 2.5) && areEqual(comps.n2, 2.5));

        comps = prepare_computations(xs.get(4), r, xs);
        assertTrue(areEqual(comps.n1, 2.5) && areEqual(comps.n2, 1.5));

        comps = prepare_computations(xs.get(5), r, xs);
        assertTrue(areEqual(comps.n1, 1.5) && areEqual(comps.n2, 1.0));
    }

    @Test
    public void theUnderPointIsOffsetBelowTheSurface() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Sphere shape = glass_sphere();
        shape.transform = translation(0, 0, 1);

        Intersection i = intersection(5, shape);
        List<Intersection> xs = intersections(i);
        Computations comps = prepare_computations(i, r, xs);

        assertTrue(comps.under_point.z > EPSILON/2.0);
        assertTrue(comps.point.z < comps.under_point.z);
    }

    @Test
    public void schlickApproximationUnderTotalInternalReflection() {
        Shape shape = glass_sphere();
        Ray r = ray(point(0, 0, Math.sqrt(2.0)/2.0), vector(0, 1, 0));
        List<Intersection> xs = intersections(
                intersection(-Math.sqrt(2.0)/2.0, shape),
                intersection(Math.sqrt(2.0)/2.0, shape)
        );
        Computations comps = prepare_computations(xs.get(1), r, xs);
        double reflectance = schlick(comps);

        assertThat(reflectance, is(equalTo(1.0)));
    }

    @Test
    public void schlickApproximationWithAPerpendicularViewingAngle() {
        Shape shape = glass_sphere();
        Ray r = ray(point(0, 0, 0), vector(0, 1, 0));
        List<Intersection> xs = intersections(
                intersection(-1, shape),
                intersection(1, shape)
        );

        Computations comps = prepare_computations(xs.get(1), r, xs);
        double reflectance = schlick(comps);

        assertTrue(areEqual(reflectance, 0.04));
    }

    @Test
    public void schlickApproximationWithSmallAngleAndN2LargerThanN1() {
        Shape shape = glass_sphere();
        Ray r = ray(point(0, 0.99, -2), vector(0, 0, 1));
        List<Intersection> xs = intersections(intersection(1.8589, shape));

        Computations comps = prepare_computations(xs.get(0), r, xs);
        double reflectance = schlick(comps);

        assertTrue(areEqual(reflectance, 0.48873));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}
