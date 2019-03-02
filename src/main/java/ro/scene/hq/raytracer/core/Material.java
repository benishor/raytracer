package ro.scene.hq.raytracer.core;

public class Material {

    public Tuple color = Tuple.color(1, 1, 1);
    public double ambient = 0.1;
    public double diffuse = 0.9;
    public double specular = 0.9;
    public double shininess = 200;

    public static Material material() {
        return new Material();
    }
}
