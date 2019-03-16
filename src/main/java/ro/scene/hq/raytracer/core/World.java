package ro.scene.hq.raytracer.core;

import java.util.*;

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

    public static Tuple shade_hit(World w, Computations comps, int remaining) {
        boolean inShadow = is_shadowed(w, comps.over_point);
        Tuple surface = lighting(
                comps.object.material,
                comps.object,
                w.light,
                comps.over_point,
                comps.eyev,
                comps.normalv,
                inShadow);
        Tuple reflectedColor = reflected_color(w, comps, remaining);
        Tuple refractedColor = refracted_color(w, comps, remaining);

        Material material = comps.object.material;
        if (material.reflective > 0 && material.transparency> 0) {
            double reflectance = schlick(comps);
            return surface.add(reflectedColor.mul(reflectance)).add(refractedColor.mul(1.0 - reflectance));
        } else {
            return surface.add(reflectedColor).add(refractedColor);
        }
    }

    public static Tuple color_at(World w, Ray r, int remaining) {
        List<Intersection> intersections = intersect_world(w, r);
        Optional<Intersection> hitIntersection = hit(intersections);
        if (hitIntersection.isPresent()) {
            Computations comps = prepare_computations(hitIntersection.get(), r, intersections);
            return shade_hit(w, comps, remaining);
        } else {
            return color(0, 0, 0);
        }
    }

    public static Tuple reflected_color(World w, Computations comps, int remaining) {
        if (remaining <= 0) {
            return color(0, 0, 0);
        }

        if (comps.object.material.reflective <= 0.0) {
            return color(0, 0, 0);
        }
        Ray reflectRay = ray(comps.over_point, comps.reflectv);
        Tuple color = color_at(w, reflectRay, remaining - 1);
        return color.mul(comps.object.material.reflective);
    }

    public static Tuple refracted_color(World w, Computations comps, int remaining) {
        if (remaining == 0) {
            return color(0, 0, 0);
        }

        if (comps.object.material.transparency <= 0.0) {
            return color(0, 0, 0);
        }

        // Find the ratio of first index of refraction to the second.
        // (Yup, this is inverted from the definition of Snell's Law.)
        double n_ratio = comps.n1 / comps.n2;
        // cos(theta_i) is the same as the dot product of the two vectors
        double cos_i = dot(comps.eyev, comps.normalv);
        // Find sin(theta_t)^2 via trigonometric identity
        double sin2_t = n_ratio * n_ratio * (1.0 - cos_i * cos_i);
        if (sin2_t > 1.0) {
            return color(0, 0, 0);
        }
        // Find cos(theta_t) via trigonometric identity
        double cos_t = Math.sqrt(1.0 - sin2_t);

        // Compute the direction of the refracted ray
        Tuple direction = comps.normalv.mul(n_ratio * cos_i - cos_t).sub(comps.eyev.mul(n_ratio));

        // Create the refracted ray
        Ray refractedRay = ray(comps.under_point, direction);

        // Find the color of the refracted ray, making sure to multiply
        // by the transparency value to account for any opacity
        return color_at(w, refractedRay, remaining - 1).mul(comps.object.material.transparency);
    }

    public static double schlick(Computations comps) {
        // find the cosine of the angle between the eye and normal vectors
        double cos = dot(comps.eyev, comps.normalv);
        // total internal reflection can only occur if n1 > n2
        if (comps.n1 > comps.n2) {
            double n = comps.n1 / comps.n2;
            double sin2_t = n * n * (1.0 - cos * cos);
            if (sin2_t > 1.0) {
                return 1.0;
            }
            // compute cosine of theta_t using trig identity
            double cos_t = Math.sqrt(1.0 - sin2_t);
            // when n1 > n2, use cos(theta_t) instead
            cos = cos_t;
        }

        double r0 = Math.pow(((comps.n1 - comps.n2) / (comps.n1 + comps.n2)), 2.0);
        return r0 + (1 - r0) * Math.pow((1 - cos), 5);
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
