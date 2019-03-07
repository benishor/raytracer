package ro.scene.hq.raytracer.core;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Camera.*;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Tuple.*;
import static ro.scene.hq.raytracer.core.World.default_world;

public class CameraTest {

    @Test
    public void constructingACamera() {
        int hsize = 160;
        int vsize = 120;
        double fov = Math.PI / 2.0;

        Camera c = camera(hsize, vsize, fov);

        assertThat(c.hsize, is(equalTo(hsize)));
        assertThat(c.vsize, is(equalTo(vsize)));
        assertThat(c.fov, is(equalTo(Math.PI / 2.0)));
        assertEqualMatrices(c.transform, identity(4));
    }

    @Test
    public void thePixelSizeForAHorizontalCanvas() {
        Camera c = camera(200, 125, Math.PI / 2.0);
        assertTrue(areEqual(c.pixelSize, 0.01));
    }

    @Test
    public void thePixelSizeForAVerticalCanvas() {
        Camera c = camera(125, 200, Math.PI / 2.0);
        assertTrue(areEqual(c.pixelSize, 0.01));
    }

    @Test
    public void constructingARayThroughTheCenterOfTheCanvas() {
        Camera c = camera(201, 101, Math.PI / 2.0);
        Ray r = ray_for_pixel(c, 100, 50);
        assertEqualTuples(r.origin, point(0, 0, 0));
        assertEqualTuples(r.direction, vector(0, 0, -1));
    }

    @Test
    public void constructingARayThroughACornerOfTheCanvas() {
        Camera c = camera(201, 101, Math.PI / 2.0);
        Ray r = ray_for_pixel(c, 0, 0);
        assertEqualTuples(r.origin, point(0, 0, 0));
        assertEqualTuples(r.direction, vector(0.66519, 0.33259, -0.66851));
    }

    @Test
    public void constructingARayWhenCameraIsTransformed() {
        Camera c = camera(201, 101, Math.PI / 2.0);
        c.transform = rotation_y(Math.PI / 4.0).mul(translation(0, -2, 5));
        Ray r = ray_for_pixel(c, 100, 50);
        assertEqualTuples(r.origin, point(0, 2, -5));
        assertEqualTuples(r.direction, vector(Math.sqrt(2) / 2.0, 0, -Math.sqrt(2) / 2.0));
    }

    @Test
    public void renderingAWorldWithACamera() {
        World w = default_world();
        Camera c = camera(11, 11, Math.PI / 2.0);
        Tuple from = point(0, 0, -5);
        Tuple to = point(0, 0, 0);
        Tuple up = vector(0, 1, 0);
        c.transform = view_transform(from, to, up);
        Canvas image = render(c, w);
        assertEqualTuples(image.pixelAt(5, 5), color(0.38066, 0.47583, 0.2855));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }

    private void assertEqualMatrices(Matrix a, Matrix b) {
        MatcherAssert.assertThat(a.size, is(equalTo(b.size)));
        for (int row = 0; row < a.size; row++) {
            for (int col = 0; col < a.size; col++) {
                MatcherAssert.assertThat(areEqual(a.data[row][col], b.data[row][col]), is(true));
            }
        }
    }
}