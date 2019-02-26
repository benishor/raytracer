package ro.scene.hq.raytracer.core;

import java.util.Objects;

public class Tuple {
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

    public Tuple add(Tuple other) {
        return new Tuple(x + other.x, y + other.y, z + other.z, w + other.w);
    }

    public Tuple sub(Tuple other) {
        return new Tuple(x - other.x, y - other.y, z - other.z, w - other.w);
    }

    public Tuple negate() {
        return new Tuple(-x, -y, -z, -w);
    }

    public Tuple mul(double scalar) {
        return new Tuple(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    public Tuple div(double scalar) {
        return new Tuple(x / scalar, y / scalar, z / scalar, w / scalar);
    }
}
