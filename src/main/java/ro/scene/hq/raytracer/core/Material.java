package ro.scene.hq.raytracer.core;

import java.util.Objects;

public class Material {

    public Pattern pattern;
    public Tuple color = Tuple.color(1, 1, 1);
    public double ambient = 0.1;
    public double diffuse = 0.9;
    public double specular = 0.9;
    public double shininess = 200;
    public double reflective = 0.0;

    public static Material material() {
        return new Material();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Double.compare(material.ambient, ambient) == 0 &&
                Double.compare(material.diffuse, diffuse) == 0 &&
                Double.compare(material.specular, specular) == 0 &&
                Double.compare(material.shininess, shininess) == 0 &&
                color.equals(material.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, ambient, diffuse, specular, shininess);
    }
}
