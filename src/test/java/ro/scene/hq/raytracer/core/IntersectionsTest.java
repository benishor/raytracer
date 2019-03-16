package ro.scene.hq.raytracer.core;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Computations.prepare_computations;
import static ro.scene.hq.raytracer.core.Intersection.intersection;
import static ro.scene.hq.raytracer.core.Light.lighting;
import static ro.scene.hq.raytracer.core.Light.point_light;
import static ro.scene.hq.raytracer.core.Material.material;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.StripePattern.stripe_pattern;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class IntersectionsTest {
    @Test
    public void precomputingTheReflectionVector() {
        Shape shape = sphere();
        Ray r = ray(point(0, 1, -1), vector(0, -Math.sqrt(2.0)/2.0, Math.sqrt(2.0)/2.0));
        Intersection i = intersection(Math.sqrt(2.0), shape);
        Computations comps = prepare_computations(i, r);
        assertEqualTuples(comps.reflectv, vector(0, Math.sqrt(2.0)/2.0, -Math.sqrt(2.0)/2.0));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }

}
