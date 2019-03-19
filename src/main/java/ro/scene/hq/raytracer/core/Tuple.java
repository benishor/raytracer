package ro.scene.hq.raytracer.core;

import java.util.Objects;

public class Tuple {
    public static final double EPSILON = 0.0001;
    public static final double INFINITY = 1e10;
    public double x;
    public double y;
    public double z;
    public double w;

    public Tuple(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public boolean isPoint() {
        return w == 1.0;
    }

    public boolean isVector() {
        return w == 0.0;
    }

    public static Tuple point(double x, double y, double z) {
        return new Tuple(x, y, z, 1.0);
    }

    public static Tuple vector(double x, double y, double z) {
        return new Tuple(x, y, z, 0);
    }

    public static Tuple color(double r, double g, double b) {
        return new Tuple(r, g, b, 0);
    }

    public Tuple add(Tuple other) {
        return new Tuple(x + other.x, y + other.y, z + other.z, w + other.w);
    }

    public Tuple sub(Tuple other) {
        return new Tuple(x - other.x, y - other.y, z - other.z, w - other.w);
    }

    public Tuple neg() {
        return new Tuple(-x, -y, -z, -w);
    }

    public Tuple mul(double scalar) {
        return new Tuple(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    public Tuple div(double scalar) {
        return new Tuple(x / scalar, y / scalar, z / scalar, w / scalar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Double.compare(tuple.x, x) == 0 &&
                Double.compare(tuple.y, y) == 0 &&
                Double.compare(tuple.z, z) == 0 &&
                Double.compare(tuple.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }

    public static double mag(Tuple t) {
        return Math.sqrt(t.x * t.x + t.y * t.y + t.z * t.z + t.w * t.w);
    }

    public static Tuple normalize(Tuple t) {
        double magnitude = mag(t);
        return vector(t.x / magnitude, t.y / magnitude, t.z / magnitude);
    }

    public static double dot(Tuple a, Tuple b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }

    public static Tuple cross(Tuple a, Tuple b) {
        return vector(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    public static boolean areEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    public Tuple mul(Tuple other) {
        return new Tuple(
                x * other.x,
                y * other.y,
                z * other.z,
                w * other.w
        );
    }

    public static Tuple reflect(Tuple in, Tuple normal) {
        return in.sub(normal.mul(2.0).mul(dot(in, normal)));
    }
}
