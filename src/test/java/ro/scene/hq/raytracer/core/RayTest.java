package ro.scene.hq.raytracer.core;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Ray.*;
import static ro.scene.hq.raytracer.core.Sphere.*;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class RayTest {

    @Test
    public void creatingAndQueryingARay() {
        Tuple origin = point(1, 2, 3);
        Tuple direction = vector(4, 5, 6);
        Ray r = ray(origin, direction);

        assertThat(r.origin, is(equalTo(origin)));
        assertThat(r.direction, is(equalTo(direction)));
    }

    @Test
    public void computingAPointFromADistance() {
        Ray r = ray(point(2, 3, 4), vector(1, 0, 0));

        assertThat(position(r, 0), is(equalTo(point(2, 3, 4))));
        assertThat(position(r, 1), is(equalTo(point(3, 3, 4))));
        assertThat(position(r, -1), is(equalTo(point(1, 3, 4))));
        assertThat(position(r, 2.5), is(equalTo(point(4.5, 3, 4))));
    }

    @Test
    public void translatingARay() {
        Ray r = ray(point(1, 2, 3), vector(0, 1, 0));
        Matrix m = translation(3, 4, 5);

        Ray r2 = transform(r, m);

        assertThat(r2.origin, is(equalTo(point(4, 6, 8))));
        assertThat(r2.direction, is(equalTo(vector(0, 1, 0))));
    }

    @Test
    public void scalingARay() {
        Ray r = ray(point(1, 2, 3), vector(0, 1, 0));
        Matrix m = scaling(2, 3, 4);

        Ray r2 = transform(r, m);

        assertThat(r2.origin, is(equalTo(point(2, 6, 12))));
        assertThat(r2.direction, is(equalTo(vector(0, 3, 0))));
    }

    @Test
    public void aSpheresDefaultTransformation() {
        Sphere s = sphere();
        assertMatricesEqual(s.transform, identity(4));
    }

    @Test
    public void interesctingAScaledSphereWithARay() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Sphere s = sphere();

        Shape.set_transform(s, scaling(2, 2, 2));
        List<Intersection> xs = intersect(s, r);

        assertThat(xs.size(), is(2));
        assertThat(xs.get(0).t, is(equalTo(3.0)));
        assertThat(xs.get(1).t, is(equalTo(7.0)));
    }

    @Test
    public void intersectingATranslatedSphereWithARay() {
        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        Sphere s = sphere();

        Shape.set_transform(s, translation(5, 0, 0));
        List<Intersection> xs = intersect(s, r);

        assertThat(xs.isEmpty(), is(true));
    }

    private void assertMatricesEqual(Matrix a, Matrix b) {
        MatcherAssert.assertThat(a.size, Is.is(IsEqual.equalTo(b.size)));
        for (int row = 0; row < a.size; row++) {
            for (int col = 0; col < a.size; col++) {
                MatcherAssert.assertThat(areEqual(a.data[row][col], b.data[row][col]), Is.is(true));
            }
        }
    }

}