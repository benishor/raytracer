package ro.scene.hq.raytracer.core;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static ro.scene.hq.raytracer.core.Light.lighting;
import static ro.scene.hq.raytracer.core.Light.point_light;
import static ro.scene.hq.raytracer.core.Matrix.scaling;
import static ro.scene.hq.raytracer.core.Sphere.intersect;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.color;
import static ro.scene.hq.raytracer.core.Tuple.point;

public class World {
    public List<Sphere> objects = new LinkedList<>();
    public Light light;

    public static World world() {
        return new World();
    }

    public boolean contains(Sphere s) {
        for (Sphere o : objects) {
            if (s.equals(o)) {
                return true;
            }
        }
        return false;
    }

    public static World default_world() {
        World w = new World();
        w.light = point_light(point(-10, 10, -10), color(1, 1, 1));

        Sphere s1 = sphere();
        s1.material.color = color(0.8, 1.0, 0.6);
        s1.material.diffuse = 0.7;
        s1.material.specular = 0.2;
        w.objects.add(s1);

        Sphere s2 = sphere();
        s2.transform = scaling(0.5, 0.5, 0.5);
        w.objects.add(s2);

        return w;
    }

    public static List<Intersection> intersect_world(World w, Ray r) {
        List<Intersection> result = new LinkedList<>();
        for (Sphere o : w.objects) {
            result.addAll(intersect(o, r));
        }
        result.sort(Comparator.comparingDouble(a -> a.t));
        return result;
    }

    public static Tuple shade_hit(World w, Computations comps) {
        return lighting(
                comps.object.material,
                w.light,
                comps.point,
                comps.eyev,
                comps.normalv);
    }
}
