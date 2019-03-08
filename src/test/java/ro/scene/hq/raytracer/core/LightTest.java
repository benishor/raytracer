package ro.scene.hq.raytracer.core;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ro.scene.hq.raytracer.core.Light.lighting;
import static ro.scene.hq.raytracer.core.Light.point_light;
import static ro.scene.hq.raytracer.core.Material.material;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class LightTest {

    @Test
    public void aPointLightHasAPositionAndIntensity() {
        Tuple intensity = color(1, 1, 1);
        Tuple position = point(0, 0, 0);

        Light light = point_light(position, intensity);

        assertThat(light.position, is(equalTo(position)));
        assertThat(light.intensity, is(equalTo(intensity)));
    }

    @Test
    public void theDefaultMaterial() {
        Material m = material();

        assertThat(m.color, is(equalTo(color(1, 1, 1))));
        assertThat(m.ambient, is(equalTo(0.1)));
        assertThat(m.diffuse, is(equalTo(0.9)));
        assertThat(m.specular, is(equalTo(0.9)));
        assertThat(m.shininess, is(equalTo(200.0)));
    }

    @Test
    public void aSpehereHasADefaultMaterial() {
        Sphere s = sphere();
        assertEqualMaterials(s.material, material());
    }

    @Test
    public void aSphereMayBeAssignedAMaterial() {
        Sphere s = sphere();
        Material m = material();
        m.ambient = 1;

        s.material = m;

        assertEqualMaterials(s.material, m);
    }

    @Test
    public void lightingWithTheEyeBetweenTheLightAndTheSurface() {
        Material m = material();
        Tuple position = point(0, 0, 0);

        Tuple eyev = vector(0, 0, -1);
        Tuple normalv = vector(0, 0, -1);
        Light light = point_light(point(0, 0, -10), color(1, 1, 1));

        boolean inShadow = false;
        Tuple result = lighting(m, light, position, eyev, normalv, inShadow);
        assertEqualTuples(result, color(1.9, 1.9, 1.9));
    }

    @Test
    public void lightingWithTheEyeBetweenTheLightAndTheSurfaceOffset45Deg() {
        Material m = material();
        Tuple position = point(0, 0, 0);

        Tuple eyev = vector(0, Math.sqrt(2.0)/2.0, -Math.sqrt(2.0)/2.0);
        Tuple normalv = vector(0, 0, -1);
        Light light = point_light(point(0, 0, -10), color(1, 1, 1));

        boolean inShadow = false;
        Tuple result = lighting(m, light, position, eyev, normalv, inShadow);
        assertEqualTuples(result, color(1.0, 1.0, 1.0));
    }

    @Test
    public void lightingWithEyeOppositeSurfaceLightOffset45Deg() {
        Material m = material();
        Tuple position = point(0, 0, 0);

        Tuple eyev = vector(0, 0, -1);
        Tuple normalv = vector(0, 0, -1);
        Light light = point_light(point(0, 10, -10), color(1, 1, 1));

        boolean inShadow = false;
        Tuple result = lighting(m, light, position, eyev, normalv, inShadow);
        assertEqualTuples(result, color(0.7364, 0.7364, 0.7364));
    }

    @Test
    public void lightingWithEyeInPathOfTheReflectionVector() {
        Material m = material();
        Tuple position = point(0, 0, 0);

        Tuple eyev = vector(0, -Math.sqrt(2.0)/2.0, -Math.sqrt(2.0)/2.0);
        Tuple normalv = vector(0, 0, -1);
        Light light = point_light(point(0, 10, -10), color(1, 1, 1));

        boolean inShadow = false;
        Tuple result = lighting(m, light, position, eyev, normalv, inShadow);
        assertEqualTuples(result, color(1.6364, 1.6364, 1.6364));
    }

    @Test
    public void lightingWithLightBehindSurface() {
        Material m = material();
        Tuple position = point(0, 0, 0);

        Tuple eyev = vector(0, 0, -1);
        Tuple normalv = vector(0, 0, -1);
        Light light = point_light(point(0, 0, 10), color(1, 1, 1));

        boolean inShadow = false;
        Tuple result = lighting(m, light, position, eyev, normalv, inShadow);
        assertEqualTuples(result, color(0.1, 0.1, 0.1));
    }

    @Test
    public void lightingWithTheSurfaceInShadow() {
        Material m = material();
        Tuple position = point(0, 0, 0);

        Tuple eyev = vector(0, 0, -1);
        Tuple normalv = vector(0, 0, -1);
        Light light = point_light(point(0, 0, -10), color(1, 1, 1));

        boolean inShadow = true;
        Tuple result = lighting(m, light, position, eyev, normalv, inShadow);
        assertEqualTuples(result, color(0.1, 0.1, 0.1));
    }


    private void assertEqualMaterials(Material a, Material b) {
        assertEqualTuples(a.color, b.color);
        assertTrue(areEqual(a.ambient, b.ambient));
        assertTrue(areEqual(a.diffuse, b.diffuse));
        assertTrue(areEqual(a.specular, b.specular));
        assertTrue(areEqual(a.shininess, b.shininess));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}