package ro.scene.hq.raytracer.core;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static ro.scene.hq.raytracer.core.Computations.prepare_computations;
import static ro.scene.hq.raytracer.core.Intersection.hit;
import static ro.scene.hq.raytracer.core.Light.lighting;
import static ro.scene.hq.raytracer.core.Light.point_light;
import static ro.scene.hq.raytracer.core.Matrix.scaling;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Sphere.intersect;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class World {
    public List<Shape> objects = new LinkedList<>();
    public Light light;

    public static World world() {
        return new World();
    }

    public boolean contains(Shape s) {
        return objects.contains(s);
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
        for (Shape o : w.objects) {
            result.addAll(intersect(o, r));
        }
        result.sort(Comparator.comparingDouble(a -> a.t));
        return result;
    }

    public static Tuple shade_hit(World w, Computations comps) {
        boolean inShadow = is_shadowed(w, comps.over_point);
        return lighting(
                comps.object.material,
                w.light,
                comps.over_point,
                comps.eyev,
                comps.normalv,
                inShadow);
    }

    public static Tuple color_at(World w, Ray r) {
        List<Intersection> intersections = intersect_world(w, r);
        Optional<Intersection> hitIntersection = hit(intersections);
        if (hitIntersection.isPresent()) {
            Computations comps = prepare_computations(hitIntersection.get(), r);
            return shade_hit(w, comps);
        } else {
            return color(0, 0, 0);
        }
    }

    public static boolean is_shadowed(World w, Tuple point) {
        Tuple v = w.light.position.sub(point);
        double distance = mag(v);
        Tuple direction = normalize(v);

        Ray r = ray(point, direction);
        List<Intersection> intersections = intersect_world(w, r);

        Optional<Intersection> h = hit(intersections);
        return h.isPresent() && h.get().t < distance;
    }
}
