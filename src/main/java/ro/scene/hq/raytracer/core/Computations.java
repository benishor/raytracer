package ro.scene.hq.raytracer.core;

import static ro.scene.hq.raytracer.core.Sphere.normal_at;
import static ro.scene.hq.raytracer.core.Tuple.dot;

public class Computations {
    public double t;
    public Sphere object;
    public Tuple point;
    public Tuple eyev;
    public Tuple normalv;
    public boolean inside;

    public static Computations prepare_computations(Intersection i, Ray r) {
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

        return comps;
    }
}
