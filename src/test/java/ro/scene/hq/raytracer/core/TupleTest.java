package ro.scene.hq.raytracer.core;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class TupleTest {

    @Test
    public void tuple_is_point() {
        Tuple tuple = new Tuple(4.3, -4.2, 3.1, 1.0);
        assertThat(tuple.isPoint(), is(true));
        assertThat(tuple.isVector(), is(false));
    }

    @Test
    public void tuple_is_vector() {
        Tuple tuple = new Tuple(4.3, -4.2, 3.1, 0.0);
        assertThat(tuple.isPoint(), is(false));
        assertThat(tuple.isVector(), is(true));
    }

    @Test
    public void createPoint() {
        Tuple point = point(4, -4, 3);
        assertThat(point, is(equalTo(new Tuple(4, -4, 3, 1))));
    }

    @Test
    public void createVector() {
        Tuple vector = vector(4, -4, 3);
        assertThat(vector, is(equalTo(new Tuple(4, -4, 3, 0))));
    }

    @Test
    public void addTuples() {
        Tuple a1 = new Tuple(3, -2, 5, 1);
        Tuple a2 = new Tuple(-2, 3, 1, 0);
        Tuple result = a1.add(a2);

        assertThat(result, is(equalTo(new Tuple(1, 1, 6, 1))));
    }

    @Test
    public void subtractPoints() {
        Tuple p1 = point(3, 2, 1);
        Tuple p2 = point(5, 6, 7);
        Tuple result = p1.sub(p2);

        assertThat(result, is(equalTo(vector(-2, -4, -6))));
    }

    @Test
    public void subtractVectorFromPoint() {
        Tuple p = point(3, 2, 1);
        Tuple v = vector(5, 6, 7);
        Tuple result = p.sub(v);

        assertThat(result, is(equalTo(point(-2, -4, -6))));
    }

    @Test
    public void subtractVectors() {
        Tuple v1 = vector(3, 2, 1);
        Tuple v2 = vector(5, 6, 7);
        Tuple result = v1.sub(v2);

        assertThat(result, is(equalTo(vector(-2, -4, -6))));
    }

    @Test
    public void subtractVectorFromZeroVector() {
        Tuple zero = vector(0, 0, 0);
        Tuple v = vector(1, -2, 3);
        Tuple result = zero.sub(v);

        assertThat(result, is(equalTo(vector(-1, 2, -3))));
    }

    @Test
    public void negateTuple() {
        Tuple a = new Tuple(1, -2, 3, -4);
        Tuple result = a.neg();

        assertThat(result, is(equalTo(new Tuple(-1, 2, -3, 4))));
    }

    @Test
    public void multiplyByScalar() {
        Tuple a = new Tuple(1, -2, 3, -4);
        Tuple result = a.mul(3.5);

        assertThat(result, is(equalTo(new Tuple(3.5, -7, 10.5, -14))));
    }

    @Test
    public void multiplyByFraction() {
        Tuple a = new Tuple(1, -2, 3, -4);
        Tuple result = a.mul(0.5);

        assertThat(result, is(equalTo(new Tuple(0.5, -1, 1.5, -2))));
    }

    @Test
    public void divideByScalar() {
        Tuple a = new Tuple(1, -2, 3, -4);
        Tuple result = a.div(2);

        assertThat(result, is(equalTo(new Tuple(0.5, -1, 1.5, -2))));
    }

    @Test
    public void mag() {
        assertThat(Tuple.mag(vector(1, 0, 0)), is(equalTo(1.0)));
        assertThat(Tuple.mag(vector(0, 1, 0)), is(equalTo(1.0)));
        assertThat(Tuple.mag(vector(0, 0, 1)), is(equalTo(1.0)));

        assertTrue(areEqual(Tuple.mag(vector(1, 2, 3)), 3.7416));
    }

    @Test
    public void normalization() {
        Tuple v1 = vector(4, 0, 0);
        assertThat(normalize(v1), is(equalTo(vector(1, 0, 0))));

        Tuple v2 = vector(1, 2, 3);
        double denominator = Math.sqrt(14.0);
        assertThat(normalize(v2), is(equalTo(vector(v2.x / denominator, v2.y / denominator, v2.z / denominator))));
    }

    @Test
    public void normalization_yieldsMagnitudeOf1() {
        Tuple n = normalize(vector(1, 2, 3));
        assertThat(Tuple.mag(n), is(equalTo(1.0)));
    }

    @Test
    public void dotProduct() {
        double dp = dot(vector(1, 2, 3), vector(2, 3, 4));
        assertThat(dp, is(equalTo(20.0)));
    }

    @Test
    public void crossProduct() {
        Tuple a = vector(1, 2, 3);
        Tuple b = vector(2, 3, 4);

        assertThat(cross(a, b), is(equalTo(vector(-1, 2, -1))));
        assertThat(cross(b, a), is(equalTo(vector(1, -2, 1))));
    }

    @Test
    public void color_isATuple() {
        Tuple c = color(1, 2, 3);

        assertThat(c.x, is(equalTo(1.0)));
        assertThat(c.y, is(equalTo(2.0)));
        assertThat(c.z, is(equalTo(3.0)));
    }

    @Test
    public void addingColors() {
        Tuple c1 = color(0.9, 0.6, 0.75);
        Tuple c2 = color(0.7, 0.1, 0.25);

        assertThat(c1.add(c2), is(equalTo(color(1.6, 0.7, 1.0))));
    }

    @Test
    public void subtractingColors() {
        Tuple c1 = color(0.9, 0.6, 0.75);
        Tuple c2 = color(0.7, 0.1, 0.25);

        assertTupleEquals(c1.sub(c2), color(0.2, 0.5, 0.5));
    }

    @Test
    public void multiplyingColorByScalar() {
        Tuple c = color(0.2, 0.3, 0.4).mul(2);
        assertTupleEquals(c, color(0.4, 0.6, 0.8));
    }

    @Test
    public void multiplyingColors() {
        Tuple c = color(1, 0.2, 0.4).mul(color(0.9, 1, 0.1));
        assertTupleEquals(c, color(0.9, 0.2, 0.04));
    }

    private void assertTupleEquals(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }


}