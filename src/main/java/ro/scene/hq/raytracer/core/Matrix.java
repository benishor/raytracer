package ro.scene.hq.raytracer.core;

import java.util.Arrays;

import static ro.scene.hq.raytracer.core.Tuple.cross;
import static ro.scene.hq.raytracer.core.Tuple.normalize;

public class Matrix {
    public final int size;
    public double[][] data; // row, column

    public Matrix(int size) {
        this.size = size;
        this.data = new double[size][size];
    }

    public Matrix(double[][] data) {
        this(data.length);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.data[row][col] = data[row][col];
            }
        }
    }

    public static Matrix identity(int size) {
        Matrix result = new Matrix(size);
        for (int i = 0; i < size; i++) {
            result.set(i, i, 1);
        }
        return result;
    }

    public double get(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, double value) {
        data[row][col] = value;
    }

    public Matrix mul(Matrix other) {
        Matrix result = new Matrix(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                double value = 0;
                for (int i = 0; i < size; i++) {
                    value += data[row][i] * other.data[i][col];
                }
                result.set(row, col, value);
            }
        }
        return result;
    }

    public Matrix div(double value) {
        Matrix result = new Matrix(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                result.data[row][col] = data[row][col] / value;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix other = (Matrix) o;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (data[row][col] != other.data[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    public Tuple mul(Tuple tuple) {
        return new Tuple(
                tuple.x * data[0][0] + tuple.y * data[0][1] + tuple.z * data[0][2] + tuple.w * data[0][3],
                tuple.x * data[1][0] + tuple.y * data[1][1] + tuple.z * data[1][2] + tuple.w * data[1][3],
                tuple.x * data[2][0] + tuple.y * data[2][1] + tuple.z * data[2][2] + tuple.w * data[2][3],
                tuple.x * data[3][0] + tuple.y * data[3][1] + tuple.z * data[3][2] + tuple.w * data[3][3]);
    }

    public static Matrix transpose(Matrix m) {
        Matrix result = new Matrix(m.size);
        for (int row = 0; row < m.size; row++) {
            for (int col = 0; col < m.size; col++) {
                result.data[row][col] = m.data[col][row];
            }
        }
        return result;
    }

    public static double determinant(Matrix m) {
        if (m.size == 2) {
            return m.data[0][0] * m.data[1][1] - m.data[0][1] * m.data[1][0];
        } else {
            double result = 0;
            for (int i = 0; i < m.size; i++) {
                result += m.data[0][i] * cofactor(m, 0, i);
            }
            return result;
        }
    }

    public static Matrix submatrix(Matrix m, int whichRow, int whichCol) {
        Matrix result = new Matrix(m.size - 1);

        int newRow = 0;
        for (int row = 0; row < m.size; row++) {
            if (row == whichRow) continue;
            int newCol = 0;
            for (int col = 0; col < m.size; col++) {
                if (col == whichCol) continue;
                result.data[newRow][newCol] = m.data[row][col];
                newCol++;
            }
            newRow++;
        }
        return result;
    }

    public static double minor(Matrix m, int row, int col) {
        return determinant(submatrix(m, row, col));
    }

    public static double cofactor(Matrix m, int row, int col) {
        if (((row + col) & 1) == 1) {
            return -minor(m, row, col);
        } else {
            return minor(m, row, col);
        }
    }

    public static boolean invertible(Matrix m) {
        return determinant(m) != 0.0;
    }

    public static Matrix inverseNaive(Matrix m) {
        Matrix cofactors = new Matrix(m.size);
        for (int row = 0; row < m.size; row++) {
            for (int col = 0; col < m.size; col++) {
                cofactors.data[row][col] = cofactor(m, row, col);
            }
        }
        return transpose(cofactors).div(determinant(m));
    }

    public static Matrix inverse(Matrix m) {
        double det = determinant(m);

        Matrix result = new Matrix(m.size);
        for (int row = 0; row < m.size; row++) {
            for (int col = 0; col < m.size; col++) {
                double c = cofactor(m, row, col);
                result.data[col][row] = c / det;
            }
        }

        return result;
    }

    public static Matrix translation(double x, double y, double z) {
        Matrix result = identity(4);
        result.data[0][3] = x;
        result.data[1][3] = y;
        result.data[2][3] = z;
        return result;
    }

    public static Matrix scaling(double x, double y, double z) {
        Matrix result = identity(4);
        result.data[0][0] = x;
        result.data[1][1] = y;
        result.data[2][2] = z;
        return result;
    }

    public static Matrix rotation_x(double radians) {
        Matrix result = new Matrix(4);
        result.data[0][0] = result.data[3][3] = 1.0;
        result.data[1][1] = Math.cos(radians);
        result.data[1][2] = -Math.sin(radians);
        result.data[2][1] = Math.sin(radians);
        result.data[2][2] = Math.cos(radians);
        return result;
    }

    public static Matrix rotation_y(double radians) {
        Matrix result = new Matrix(4);
        result.data[0][0] = Math.cos(radians);
        result.data[0][2] = Math.sin(radians);
        result.data[1][1] = result.data[3][3] = 1.0;
        result.data[2][0] = -Math.sin(radians);
        result.data[2][2] = Math.cos(radians);
        return result;
    }

    public static Matrix rotation_z(double radians) {
        Matrix result = new Matrix(4);
        result.data[0][0] = Math.cos(radians);
        result.data[0][1] = -Math.sin(radians);
        result.data[1][0] = Math.sin(radians);
        result.data[1][1] = Math.cos(radians);
        result.data[2][2] = result.data[3][3] = 1.0;
        return result;
    }

    public static Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        Matrix result = identity(4);
        result.data[0][1] = xy;
        result.data[0][2] = xz;
        result.data[1][0] = yx;
        result.data[1][2] = yz;
        result.data[2][0] = zx;
        result.data[2][1] = zy;
        return result;
    }

    public static Matrix view_transform(Tuple from, Tuple to, Tuple up) {
        Tuple forward = normalize(to.sub(from));
        Tuple left = cross(forward, normalize(up));
        Tuple trueUp = cross(left, forward);
        Matrix orientation = new Matrix(new double[][]{
                {left.x, left.y, left.z, 0.0},
                {trueUp.x, trueUp.y, trueUp.z, 0.0},
                {-forward.x, -forward.y, -forward.z, 0.0},
                {0, 0, 0, 1}
        });
        return orientation.mul(translation(-from.x, -from.y, -from.z));
    }
}
