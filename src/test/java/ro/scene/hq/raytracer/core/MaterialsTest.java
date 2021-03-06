package ro.scene.hq.raytracer.core;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Material.material;

public class MaterialsTest {

    @Test
    public void reflectivityForTheDefaultMaterial() {
        Material m = material();
        assertThat(m.reflective, is(equalTo(0.0)));
    }

    @Test
    public void transparencyAndRefractiveIndexForDefaultMaterial() {
        Material m = material();
        assertThat(m.transparency, is(equalTo(0.0)));
        assertThat(m.refractiveIndex, is(equalTo(1.0)));
    }
}
