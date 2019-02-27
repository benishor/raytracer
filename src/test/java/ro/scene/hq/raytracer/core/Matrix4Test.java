package ro.scene.hq.raytracer.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static ro.scene.hq.raytracer.core.Matrix4.transpose;

public class Matrix4Test {
    @Test
    public void mat4CanBeRepresented() {
        Matrix4 m = new Matrix4(new double[][]{
                {1, 2, 3, 4},
                {5.5, 6.5, 7.5, 8.5},
                {9, 10, 11, 12},
                {13.5, 14.5, 15.5, 16.5}});

        assertThat(m.get(0, 0), is(equalTo(1.0)));
        assertThat(m.get(0, 3), is(equalTo(4.0)));
        assertThat(m.get(1, 0), is(equalTo(5.5)));
        assertThat(m.get(1, 2), is(equalTo(7.5)));
        assertThat(m.get(2, 2), is(equalTo(11.0)));
        assertThat(m.get(3, 0), is(equalTo(13.5)));
        assertThat(m.get(3, 2), is(equalTo(15.5)));
    }

    @Test
    public void matrixEquality_sameMatrix() {
        Matrix4 a = new Matrix4(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}});
        Matrix4 b = new Matrix4(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}});

        assertThat(a, is(equalTo(b)));
    }

    @Test
    public void matrixEquality_differentMatrix() {
        Matrix4 a = new Matrix4(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}});
        Matrix4 b = new Matrix4(new double[][]{
                {2, 3, 4, 5},
                {6, 7, 8, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}});

        assertThat(a, is(not(equalTo(b))));
    }

    @Test
    public void matrixMultiplication() {
        Matrix4 a = new Matrix4(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}});
        Matrix4 b = new Matrix4(new double[][]{
                {-2, 1, 2, 3},
                {3, 2, 1, -1},
                {4, 3, 6, 5},
                {1, 2, 7, 8}});

        Matrix4 expected = new Matrix4(new double[][]{
                {20, 22, 50, 48},
                {44, 54, 114, 108},
                {40, 58, 110, 102},
                {16, 26, 46, 42}});

        assertThat(a.mul(b), is(equalTo(expected)));
    }

    @Test
    public void matrixByTupleMultiplication() {
        Matrix4 a = new Matrix4(new double[][]{
                {1, 2, 3, 4},
                {2, 4, 4, 2},
                {8, 6, 4, 1},
                {0, 0, 0, 1}});

        Tuple actual = a.mul(new Tuple(1, 2, 3, 1));
        Tuple expected = new Tuple(18, 24, 33, 1);

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void multiplyByIdentityMatrix() {
        Matrix4 a = new Matrix4(new double[][]{
                {1, 2, 3, 4},
                {2, 4, 4, 2},
                {8, 6, 4, 1},
                {0, 0, 0, 1}});

        Matrix4 actual = a.mul(Matrix4.identity());
        assertThat(actual, is(equalTo(a)));
    }

    @Test
    public void transposeMatrix() {
        Matrix4 a = new Matrix4(new double[][]{
                {0, 9, 3, 0},
                {9, 8, 0, 8},
                {1, 8, 5, 3},
                {0, 0, 5, 8}});
        Matrix4 expected = new Matrix4(new double[][]{
                {0, 9, 1, 0},
                {9, 8, 8, 0},
                {3, 0, 5, 5},
                {0, 8, 3, 8}});

        Matrix4 actual = transpose(a);
        assertThat(actual, is(equalTo(expected)));
    }
}