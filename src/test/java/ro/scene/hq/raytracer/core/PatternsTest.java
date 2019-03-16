package ro.scene.hq.raytracer.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Pattern.*;
import static ro.scene.hq.raytracer.core.PatternsTest.TestPattern.test_pattern;
import static ro.scene.hq.raytracer.core.Shape.set_transform;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.StripePattern.stripe_pattern;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class PatternsTest {
    private Tuple black = color(0, 0, 0);
    private Tuple white = color(1, 1, 1);

    static class TestPattern extends Pattern {
        @Override
        Tuple colorAt(Tuple point) {
            return color(point.x, point.y, point.z);
        }

        public static TestPattern test_pattern() {
            return new TestPattern();
        }
    }

    @Test
    public void creatingAStripePattern() {
        StripePattern pattern = stripe_pattern(white, black);

        assertEqualTuples(pattern.a, white);
        assertEqualTuples(pattern.b, black);
    }

    @Test
    public void aStripePatternIsConstantInY() {
        StripePattern pattern = stripe_pattern(white, black);

        assertEqualTuples(pattern.colorAt(point(0, 0, 0)), white);
        assertEqualTuples(pattern.colorAt(point(0, 1, 0)), white);
        assertEqualTuples(pattern.colorAt(point(0, 2, 0)), white);
    }

    @Test
    public void aStripePatternIsConstantInZ() {
        Pattern pattern = stripe_pattern(white, black);

        assertEqualTuples(pattern.colorAt(point(0, 0, 0)), white);
        assertEqualTuples(pattern.colorAt(point(0, 0, 1)), white);
        assertEqualTuples(pattern.colorAt(point(0, 0, 2)), white);
    }

    @Test
    public void aStripePatternAlternatesInX() {
        StripePattern pattern = stripe_pattern(white, black);

        assertEqualTuples(pattern.colorAt(point(0, 0, 0)), white);
        assertEqualTuples(pattern.colorAt(point(0.9, 0, 0)), white);
        assertEqualTuples(pattern.colorAt(point(1, 0, 0)), black);
        assertEqualTuples(pattern.colorAt(point(-0.1, 0, 0)), black);
        assertEqualTuples(pattern.colorAt(point(-1, 0, 0)), black);
        assertEqualTuples(pattern.colorAt(point(-1.1, 0, 0)), white);
    }

    @Test
    public void defaultPatternTransformation() {
        Pattern pattern = test_pattern();
        assertMatricesEqual(pattern.transform, identity(4));
    }

    @Test
    public void assigningATransformation() {
        Pattern pattern = test_pattern();
        pattern.transform = translation(1, 2, 3);
        assertMatricesEqual(pattern.transform, translation(1, 2, 3));
    }

    private void assertMatricesEqual(Matrix a, Matrix b) {
        assertThat(a.size, is(equalTo(b.size)));
        for (int row = 0; row < a.size; row++) {
            for (int col = 0; col < a.size; col++) {
                assertThat(areEqual(a.data[row][col], b.data[row][col]), is(true));
            }
        }
    }

    @Test
    public void aPatternWithAnObjectTransformation() {
        Shape shape = sphere();
        shape.transform = scaling(2, 2, 2);
        Pattern pattern = test_pattern();
        Tuple c = pattern.colorAtObject(shape, point(2, 3, 4));
        assertEqualTuples(c, color(1, 1.5, 2));
    }

    @Test
    public void aPatternWithAPatternTransformation() {
        Shape shape = sphere();
        Pattern pattern = test_pattern();
        pattern.transform = scaling(2, 2, 2);
        Tuple c = pattern.colorAtObject(shape, point(2, 3, 4));
        assertEqualTuples(c, color(1, 1.5, 2));
    }

    @Test
    public void aPatternWithBothAnObjectAndAPatternTransformation() {
        Shape shape = sphere();
        shape.transform = scaling(2, 2, 2);
        Pattern pattern = test_pattern();
        pattern.transform = translation(0.5, 1, 1.5);
        Tuple c = pattern.colorAtObject(shape, point(2.5, 3, 3.5));
        assertEqualTuples(c, color(0.75, 0.5, 0.25));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}
