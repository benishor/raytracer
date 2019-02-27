package ro.scene.hq.raytracer.core;

import java.util.Arrays;

public class Matrix4 {
    public double[][] data = new double[4][4]; // row, column

    public Matrix4() {
    }

    public Matrix4(double[][] data) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                this.data[row][col] = data[row][col];
            }
        }
    }

    public static Matrix4 identity() {
        Matrix4 result = new Matrix4();
        for (int i = 0; i < 4; i++) {
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

    public Matrix4 mul(Matrix4 other) {
        Matrix4 result = new Matrix4();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                double value = 0;
                for (int i = 0; i < 4; i++) {
                    value += data[row][i] * other.data[i][col];
                }
                result.set(row, col, value);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix4 matrix4 = (Matrix4) o;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (data[row][col] != matrix4.data[row][col]) {
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
        return "Matrix4{" +
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

    public static Matrix4 transpose(Matrix4 m) {
        Matrix4 result = new Matrix4();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result.data[row][col] = m.data[col][row];
            }
        }
        return result;
    }
}
