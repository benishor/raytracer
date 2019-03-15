package ro.scene.hq.raytracer.core;

import java.util.Objects;

import static ro.scene.hq.raytracer.core.Pattern.stripe_at_object;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class Light {
    public final Tuple intensity;
    public final Tuple position;

    public Light(Tuple position, Tuple intensity) {
        this.position = position;
        this.intensity = intensity;
    }

    public static Light point_light(Tuple position, Tuple intensity) {
        return new Light(position, intensity);
    }

    public static Tuple lighting(Material m, Shape object, Light light, Tuple position, Tuple eyev, Tuple normalv, boolean inShadow) {
        Tuple color = m.pattern == null ? m.color : stripe_at_object(m.pattern, object, position);
        Tuple effectiveColor = color.mul(light.intensity);
        Tuple lightv = normalize(light.position.sub(position));
        Tuple ambient = effectiveColor.mul(m.ambient);
        if (inShadow) {
            return ambient;
        }

        Tuple diffuse;
        Tuple specular;


        double lightDotNormal = dot(lightv, normalv);
        if (lightDotNormal < 0) {
            diffuse = color(0, 0, 0);
            specular = color(0, 0, 0);
        } else {
            diffuse = effectiveColor.mul(m.diffuse).mul(lightDotNormal);
            Tuple reflectv = reflect(lightv.neg(), normalv);
            double reflectDotEye = dot(reflectv, eyev);

            if (reflectDotEye <= 0) {
                specular = color(0, 0, 0);
            } else {
                double factor = Math.pow(reflectDotEye, m.shininess);
                specular = light.intensity.mul(m.specular).mul(factor);
            }
        }

        return ambient.add(diffuse).add(specular);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Light light = (Light) o;
        return intensity.equals(light.intensity) &&
                position.equals(light.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intensity, position);
    }
}
