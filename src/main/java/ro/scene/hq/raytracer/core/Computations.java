package ro.scene.hq.raytracer.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ro.scene.hq.raytracer.core.Intersection.hit;
import static ro.scene.hq.raytracer.core.Sphere.normal_at;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class Computations {
    public double t;
    public Shape object;
    public Tuple point;
    public Tuple over_point;
    public Tuple under_point;
    public Tuple eyev;
    public Tuple normalv;
    public Tuple reflectv;
    public boolean inside;
    public double n1;
    public double n2;

    public static Computations prepare_computations(Intersection i, Ray r, List<Intersection> xs) {
        Computations comps = new Computations();
        comps.t = i.t;
        comps.object = i.object;
        comps.point = Ray.position(r, i.t);
        comps.eyev = r.direction.neg();
        comps.normalv = normal_at(comps.object, comps.point);

        if (dot(comps.normalv, comps.eyev) < 0) {
            comps.inside = true;
            comps.normalv = comps.normalv.neg();
        } else {
            comps.inside = false;
        }

        comps.over_point = comps.point.add(comps.normalv.mul(EPSILON));
        comps.under_point = comps.point.sub(comps.normalv.mul(EPSILON));
        comps.reflectv = reflect(r.direction, comps.normalv);

        List<Shape> containers = new ArrayList<>(xs.size());
        for (Intersection intersection : xs) {
            if (intersection.equals(i)) {
                if (containers.isEmpty()) {
                    comps.n1 = 1.0;
                } else {
                    comps.n1 = containers.get(containers.size() - 1).material.refractiveIndex;
                }
            }
            if (containers.contains(intersection.object)) {
                containers.remove(intersection.object);
            } else {
                containers.add(intersection.object);
            }

            if (intersection.equals(i)) {
                if (containers.isEmpty()) {
                    comps.n2 = 1.0;
                } else {
                    comps.n2 = containers.get(containers.size() - 1).material.refractiveIndex;
                }
                break;
            }
        }

        return comps;
    }
}
