package ro.scene.hq.raytracer.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Tuple.point;
import static ro.scene.hq.raytracer.core.Tuple.vector;

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
        Tuple result = a.negate();

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
}