package ro.scene.hq.raytracer.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static ro.scene.hq.raytracer.core.Matrix.scaling;
import static ro.scene.hq.raytracer.core.Pattern.*;
import static ro.scene.hq.raytracer.core.Shape.set_transform;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class PatternsTest {
    private Tuple black = color(0, 0, 0);
    private Tuple white = color(1, 1, 1);

    @Test
    public void creatingAStripePattern() {
        Pattern pattern = stripe_pattern(white, black);

        assertEqualTuples(pattern.a, white);
        assertEqualTuples(pattern.b, black);
    }

    @Test
    public void aStripePatternIsConstantInY() {
        Pattern pattern = stripe_pattern(white, black);

        assertEqualTuples(pattern.at(point(0, 0, 0)), white);
        assertEqualTuples(pattern.at(point(0, 1, 0)), white);
        assertEqualTuples(pattern.at(point(0, 2, 0)), white);
    }

    @Test
    public void aStripePatternIsConstantInZ() {
        Pattern pattern = stripe_pattern(white, black);

        assertEqualTuples(pattern.at(point(0, 0, 0)), white);
        assertEqualTuples(pattern.at(point(0, 0, 1)), white);
        assertEqualTuples(pattern.at(point(0, 0, 2)), white);
    }

    @Test
    public void aStripePatternAlternatesInX() {
        Pattern pattern = stripe_pattern(white, black);

        assertEqualTuples(pattern.at(point(0, 0, 0)), white);
        assertEqualTuples(pattern.at(point(0.9, 0, 0)), white);
        assertEqualTuples(pattern.at(point(1, 0, 0)), black);
        assertEqualTuples(pattern.at(point(-0.1, 0, 0)), black);
        assertEqualTuples(pattern.at(point(-1, 0, 0)), black);
        assertEqualTuples(pattern.at(point(-1.1, 0, 0)), white);
    }

    @Test
    public void stripesWithAnObjectTransformation() {
        Shape object = sphere();
        set_transform(object, scaling(2, 2, 2));
        Pattern pattern = stripe_pattern(white, black);
        Tuple c = stripe_at_object(pattern, object, point(1.5, 0, 0));
        assertEqualTuples(c, white);
    }

    @Test
    public void stripesWithAPatternTransformation() {
        Shape object = sphere();
        Pattern pattern = stripe_pattern(white, black);
        set_pattern_transform(pattern, scaling(2, 2, 2));
        Tuple c = stripe_at_object(pattern, object, point(1.5, 0, 0));
        assertEqualTuples(c, white);
    }

    @Test
    public void stripesWithBothAnObjectAndAPatternTransformation() {
        Shape object = sphere();
        set_transform(object, scaling(2, 2, 2));
        Pattern pattern = stripe_pattern(white, black);
        set_pattern_transform(pattern, scaling(0.5, 0, 0));
        Tuple c = stripe_at_object(pattern, object, point(2.5, 0, 0));
        assertEqualTuples(c, white);
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}
