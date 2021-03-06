package ro.scene.hq.raytracer.core;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class MatrixTest {
    @Test
    public void mat4CanBeRepresented() {
        Matrix m = new Matrix(new double[][]{
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
    public void mat2CanBeRepresented() {
        Matrix m = new Matrix(new double[][]{
                {-3, 5},
                {1, -2}});

        assertThat(m.get(0, 0), is(equalTo(-3.0)));
        assertThat(m.get(0, 1), is(equalTo(5.0)));
        assertThat(m.get(1, 0), is(equalTo(1.0)));
        assertThat(m.get(1, 1), is(equalTo(-2.0)));
    }

    @Test
    public void mat3CanBeRepresented() {
        Matrix m = new Matrix(new double[][]{
                {-3, 5, 0},
                {1, -2, -7},
                {0, 1, 1}});

        assertThat(m.get(0, 0), is(equalTo(-3.0)));
        assertThat(m.get(1, 1), is(equalTo(-2.0)));
        assertThat(m.get(2, 2), is(equalTo(1.0)));
    }

    @Test
    public void matrixEquality_sameMatrix() {
        Matrix a = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}});
        Matrix b = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}});

        assertThat(a, is(equalTo(b)));
    }

    @Test
    public void matrixEquality_differentMatrix() {
        Matrix a = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}});
        Matrix b = new Matrix(new double[][]{
                {2, 3, 4, 5},
                {6, 7, 8, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}});

        assertThat(a, is(not(equalTo(b))));
    }

    @Test
    public void matrixMultiplication() {
        Matrix a = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}});
        Matrix b = new Matrix(new double[][]{
                {-2, 1, 2, 3},
                {3, 2, 1, -1},
                {4, 3, 6, 5},
                {1, 2, 7, 8}});

        Matrix expected = new Matrix(new double[][]{
                {20, 22, 50, 48},
                {44, 54, 114, 108},
                {40, 58, 110, 102},
                {16, 26, 46, 42}});

        assertThat(a.mul(b), is(equalTo(expected)));
    }

    @Test
    public void matrixByTupleMultiplication() {
        Matrix a = new Matrix(new double[][]{
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
        Matrix a = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {2, 4, 4, 2},
                {8, 6, 4, 1},
                {0, 0, 0, 1}});

        Matrix actual = a.mul(Matrix.identity(4));
        assertThat(actual, is(equalTo(a)));
    }

    @Test
    public void transposeMatrix() {
        Matrix a = new Matrix(new double[][]{
                {0, 9, 3, 0},
                {9, 8, 0, 8},
                {1, 8, 5, 3},
                {0, 0, 5, 8}});
        Matrix expected = new Matrix(new double[][]{
                {0, 9, 1, 0},
                {9, 8, 8, 0},
                {3, 0, 5, 5},
                {0, 8, 3, 8}});

        Matrix actual = transpose(a);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void determinantMat2() {
        Matrix m = new Matrix(new double[][]{
                {1, 5},
                {-3, 2}});
        assertThat(determinant(m), is(equalTo(17.0)));
    }

    @Test
    public void submatrixOfMat3IsMat2() {
        Matrix m = new Matrix(new double[][]{
                {1, 5, 0},
                {-3, 2, 7},
                {0, 6, -3}});

        Matrix expected = new Matrix(new double[][]{
                {-3, 2},
                {0, 6}});
        assertThat(submatrix(m, 0, 2), is(equalTo(expected)));
    }

    @Test
    public void submatrixOfMat4IsMat3() {
        Matrix m = new Matrix(new double[][]{
                {-6, 1, 1, 6},
                {-8, 5, 8, 6},
                {-1, 0, 8, 2},
                {-7, 1, -1, 1}});

        Matrix expected = new Matrix(new double[][]{
                {-6, 1, 6},
                {-8, 8, 6},
                {-7, -1, 1}});

        Matrix actual = submatrix(m, 2, 1);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void minorOfMat3() {
        Matrix a = new Matrix(new double[][]{
                {3, 5, 0},
                {2, -1, -7},
                {6, -1, 5}});
        Matrix b = submatrix(a, 1, 0);

        assertThat(determinant(b), is(equalTo(25.0)));
        assertThat(minor(a, 1, 0), is(equalTo(25.0)));
    }

    @Test
    public void cofactorOfMat3() {
        Matrix a = new Matrix(new double[][]{
                {3, 5, 0},
                {2, -1, -7},
                {6, -1, 5}});

        assertThat(minor(a, 0, 0), is(equalTo(-12.0)));
        assertThat(cofactor(a, 0, 0), is(equalTo(-12.0)));
        assertThat(minor(a, 1, 0), is(equalTo(25.0)));
        assertThat(cofactor(a, 1, 0), is(equalTo(-25.0)));
    }

    @Test
    public void determinantOfMat3() {
        Matrix a = new Matrix(new double[][]{
                {1, 2, 6},
                {-5, 8, -4},
                {2, 6, 4}});

        assertThat(cofactor(a, 0, 0), is(equalTo(56.0)));
        assertThat(cofactor(a, 0, 1), is(equalTo(12.0)));
        assertThat(cofactor(a, 0, 2), is(equalTo(-46.0)));
        assertThat(determinant(a), is(equalTo(-196.0)));
    }

    @Test
    public void determinantOfMat4() {
        Matrix a = new Matrix(new double[][]{
                {-2, -8, 3, 5},
                {-3, 1, 7, 3},
                {1, 2, -9, 6},
                {-6, 7, 7, -9}});

        assertThat(cofactor(a, 0, 0), is(equalTo(690.0)));
        assertThat(cofactor(a, 0, 1), is(equalTo(447.0)));
        assertThat(cofactor(a, 0, 2), is(equalTo(210.0)));
        assertThat(cofactor(a, 0, 3), is(equalTo(51.0)));
        assertThat(determinant(a), is(equalTo(-4071.0)));
    }

    @Test
    public void checkingInvertibleMat4ForInvertibility() {
        Matrix a = new Matrix(new double[][]{
                {6, 4, 4, 4},
                {5, 5, 7, 6},
                {4, -9, 3, -7},
                {9, 1, 7, -6}});

        assertThat(determinant(a), is(equalTo(-2120.0)));
        assertThat(invertible(a), is(true));
    }

    @Test
    public void checkingNotInvertibleMat4ForInvertibility() {
        Matrix a = new Matrix(new double[][]{
                {-4, 2, -2, -3},
                {9, 6, 2, 6},
                {0, -5, 1, -5},
                {0, 0, 0, 0}});

        assertThat(determinant(a), is(equalTo(0.0)));
        assertThat(invertible(a), is(false));
    }

    @Test
    public void invertingMat4() {
        Matrix a = new Matrix(new double[][]{
                {-5, 2, 6, -8},
                {1, -5, 1, 8},
                {7, 7, -6, -7},
                {1, -3, 7, 4}});

        Matrix b = inverse(a);

        assertThat(determinant(a), is(equalTo(532.0)));
        assertThat(cofactor(a, 2, 3), is(equalTo(-160.0)));
        assertThat(b.data[3][2], is(equalTo(-160.0 / 532.0)));
        assertThat(cofactor(a, 3, 2), is(equalTo(105.0)));
        assertThat(b.data[2][3], is(equalTo(105.0 / 532.0)));

        Matrix expected = new Matrix(new double[][]{
                {0.21805, 0.45113, 0.24060, -0.04511},
                {-0.80827, -1.45677, -0.44361, 0.52068},
                {-0.07895, -0.22368, -0.05263, 0.19737},
                {-0.52256, -0.81391, -0.30075, 0.30639}});
        assertMatricesEqual(b, expected);
    }

    @Test
    public void inverseMat4Again() {
        Matrix a = new Matrix(new double[][]{
                {8, -5, 9, 2},
                {7, 5, 6, 1},
                {-6, 0, 9, 6},
                {-3, 0, -9, -4}});

        Matrix b = inverse(a);

        Matrix expected = new Matrix(new double[][]{
                {-0.15385, -0.15385, -0.28205, -0.53846},
                {-0.07692, 0.12308, 0.02564, 0.03077},
                {0.35897, 0.35897, 0.43590, 0.92308},
                {-0.69231, -0.69231, -0.76923, -1.92308}});
        assertMatricesEqual(b, expected);
    }

    @Test
    public void inverseOfThirdMat4() {
        Matrix a = new Matrix(new double[][]{
                {9, 3, 0, 9},
                {-5, -2, -6, -3},
                {-4, 9, 6, 4},
                {-7, 6, 6, 2}});

        Matrix b = inverse(a);

        Matrix expected = new Matrix(new double[][]{
                {-0.04074, -0.07778, 0.14444, -0.22222},
                {-0.07778, 0.03333, 0.36667, -0.33333},
                {-0.02901, -0.14630, -0.10926, 0.12963},
                {0.17778, 0.06667, -0.26667, 0.33333}});
        assertMatricesEqual(b, expected);
    }

    @Test
    public void multiplyAProductByItsInverse() {
        Matrix a = new Matrix(new double[][]{
                {3, -9, 7, 3},
                {3, -8, 2, -9},
                {-4, 4, 4, 1},
                {-6, 5, -1, 1}});

        Matrix b = new Matrix(new double[][]{
                {8, 2, 2, 2},
                {3, -1, 7, 0},
                {7, 0, 5, 4},
                {6, -2, 0, 5}});

        Matrix c = a.mul(b);
        Matrix actual = c.mul(inverse(b));

        assertMatricesEqual(actual, a);
    }

    @Test
    public void multiplyingMatByInverseYieldsUnit() {
        Matrix a = new Matrix(new double[][]{
                {3, -9, 7, 3},
                {3, -8, 2, -9},
                {-4, 4, 4, 1},
                {-6, 5, -1, 1}});
        Matrix actual = a.mul(inverse(a));
        assertMatricesEqual(actual, identity(4));
    }

    @Test
    public void multiplyByTranslationMat() {
        Matrix transform = translation(5, -3, 2);
        Tuple p = point(-3, 4, 5);
        assertThat(transform.mul(p), is(equalTo(point(2, 1, 7))));
    }

    @Test
    public void multiplyByInverseOfTranslationMat() {
        Matrix transform = translation(5, -3, 2);
        Tuple p = point(-3, 4, 5);
        assertThat(inverse(transform).mul(p), is(equalTo(point(-8, 7, 3))));
    }

    @Test
    public void translationDoesNotAffectVectors() {
        Matrix transform = translation(5, -3, 2);
        Tuple v = vector(-3, 4, 5);
        assertThat(transform.mul(v), is(equalTo(v)));
    }

    @Test
    public void scalingAppliedToAPoint() {
        Matrix transform = scaling(2, 3, 4);
        Tuple p = point(-4, 6, 8);
        assertThat(transform.mul(p), is(equalTo(point(-8, 18, 32))));
    }

    @Test
    public void scalingAppliedToAVector() {
        Matrix transform = scaling(2, 3, 4);
        Tuple p = vector(-4, 6, 8);
        assertThat(transform.mul(p), is(equalTo(vector(-8, 18, 32))));
    }

    @Test
    public void multiplyByTheInverseOfAScalingMatrix() {
        Matrix transform = scaling(2, 3, 4);
        Tuple p = vector(-4, 6, 8);
        assertThat(inverse(transform).mul(p), is(equalTo(vector(-2, 2, 2))));
    }

    @Test
    public void reflectionIsScalingByNegative() {
        Matrix transform = scaling(-1, 1, 1);
        Tuple p = point(2, 3, 4);
        assertThat(transform.mul(p), is(equalTo(point(-2, 3, 4))));
    }

    @Test
    public void rotateAroundX() {
        Tuple p = point(0, 1, 0);
        Matrix halfQuarter = rotation_x(Math.PI / 4.0);
        Matrix fullQuarter = rotation_x(Math.PI / 2.0);

        assertTupleEquals(halfQuarter.mul(p), point(0, Math.sqrt(2.0) / 2.0, Math.sqrt(2.0) / 2.0));
        assertTupleEquals(fullQuarter.mul(p), point(0, 0, 1));
    }

    @Test
    public void rotateAroundY() {
        Tuple p = point(0, 0, 1);
        Matrix halfQuarter = rotation_y(Math.PI / 4.0);
        Matrix fullQuarter = rotation_y(Math.PI / 2.0);

        assertTupleEquals(halfQuarter.mul(p), point(Math.sqrt(2.0) / 2.0, 0, Math.sqrt(2.0) / 2.0));
        assertTupleEquals(fullQuarter.mul(p), point(1, 0, 0));
    }

    @Test
    public void rotateAroundZ() {
        Tuple p = point(0, 1, 0);
        Matrix halfQuarter = rotation_z(Math.PI / 4.0);
        Matrix fullQuarter = rotation_z(Math.PI / 2.0);

        assertTupleEquals(halfQuarter.mul(p), point(-Math.sqrt(2.0) / 2.0, Math.sqrt(2.0) / 2.0, 0));
        assertTupleEquals(fullQuarter.mul(p), point(-1, 0, 0));
    }

    @Test
    public void shearingMovesXInProportionToY() {
        Matrix transform = shearing(1, 0, 0, 0, 0, 0);
        Tuple p = point(2, 3, 4);
        assertThat(transform.mul(p), is(equalTo(point(5, 3, 4))));
    }

    @Test
    public void shearingMovesYInProportionToX() {
        Matrix transform = shearing(0, 0, 1, 0, 0, 0);
        Tuple p = point(2, 3, 4);
        assertThat(transform.mul(p), is(equalTo(point(2, 5, 4))));
    }

    @Test
    public void shearingMovesYInProportionToZ() {
        Matrix transform = shearing(0, 0, 0, 1, 0, 0);
        Tuple p = point(2, 3, 4);
        assertThat(transform.mul(p), is(equalTo(point(2, 7, 4))));
    }

    @Test
    public void shearingMovesZInProportionToX() {
        Matrix transform = shearing(0, 0, 0, 0, 1, 0);
        Tuple p = point(2, 3, 4);
        assertThat(transform.mul(p), is(equalTo(point(2, 3, 6))));
    }

    @Test
    public void shearingMovesZInProportionToY() {
        Matrix transform = shearing(0, 0, 0, 0, 0, 1);
        Tuple p = point(2, 3, 4);
        assertThat(transform.mul(p), is(equalTo(point(2, 3, 7))));
    }

    @Test
    public void individualTransformationsAreAppliedInSequence() {
        Tuple p = point(1, 0, 1);
        Matrix a = rotation_x(Math.PI / 2.0);
        Matrix b = scaling(5, 5, 5);
        Matrix c = translation(10, 5, 7);

        // apply rotation
        Tuple p2 = a.mul(p);
        assertTupleEquals(p2, point(1, -1, 0));

        // apply scaling
        Tuple p3 = b.mul(p2);
        assertTupleEquals(p3, point(5, -5, 0));

        // apply translation
        Tuple p4 = c.mul(p3);
        assertTupleEquals(p4, point(15, 0, 7));
    }

    @Test
    public void chainedTransformationsAppliedInReverseOrder() {
        Tuple p = point(1, 0, 1);

        Matrix a = rotation_x(Math.PI / 2.0);
        Matrix b = scaling(5, 5, 5);
        Matrix c = translation(10, 5, 7);

        Matrix transform = c.mul(b).mul(a);
        Tuple p2 = transform.mul(p);

        assertTupleEquals(p2, point(15, 0, 7));
    }

    @Test
    public void theTransformationMatrixForTheDefaultOrientation() {
        Tuple from = point(0, 0, 0);
        Tuple to = point(0, 0, -1);
        Tuple up = vector(0, 1, 0);
        Matrix t = view_transform(from, to, up);
        assertMatricesEqual(t, identity(4));
    }

    @Test
    public void aViewTransformationMatrixLookingInPositiveZDirection() {
        Tuple from = point(0, 0, 0);
        Tuple to = point(0, 0, 1);
        Tuple up = vector(0, 1, 0);
        Matrix t = view_transform(from, to, up);
        assertMatricesEqual(t, scaling(-1, 1, -1));
    }

    @Test
    public void anArbitraryViewTransformation() {
        Tuple from = point(1, 3, 2);
        Tuple to = point(4, -2, 8);
        Tuple up = vector(1, 1, 0);
        Matrix t = view_transform(from, to, up);

        Matrix expected = new Matrix(new double[][]{
                {-0.50709, 0.50709, 0.67612, -2.36643},
                {0.76772, 0.60609, 0.12122, -2.82843},
                {-0.35857, 0.59761, -0.71714, 0.00000},
                {0.00000, 0.00000, 0.00000, 1.00000}
        });
        assertMatricesEqual(t, expected);
    }

    private void assertMatricesEqual(Matrix a, Matrix b) {
        assertThat(a.size, is(equalTo(b.size)));
        for (int row = 0; row < a.size; row++) {
            for (int col = 0; col < a.size; col++) {
                assertThat(areEqual(a.data[row][col], b.data[row][col]), is(true));
            }
        }
    }

    private void assertTupleEquals(Tuple a, Tuple b) {
        Assert.assertThat(areEqual(a.x, b.x), CoreMatchers.is(true));
        Assert.assertThat(areEqual(a.y, b.y), CoreMatchers.is(true));
        Assert.assertThat(areEqual(a.z, b.z), CoreMatchers.is(true));
        Assert.assertThat(areEqual(a.w, b.w), CoreMatchers.is(true));
    }
}