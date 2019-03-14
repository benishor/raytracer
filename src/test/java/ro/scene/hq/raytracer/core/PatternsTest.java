package ro.scene.hq.raytracer.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static ro.scene.hq.raytracer.core.Pattern.stripe_pattern;
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

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}
