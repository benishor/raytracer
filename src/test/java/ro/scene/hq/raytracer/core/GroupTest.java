package ro.scene.hq.raytracer.core;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static ro.scene.hq.raytracer.core.Group.group;
import static ro.scene.hq.raytracer.core.Matrix.*;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Shape.normal_at;
import static ro.scene.hq.raytracer.core.ShapesTest.TestShape.test_shape;
import static ro.scene.hq.raytracer.core.Sphere.sphere;
import static ro.scene.hq.raytracer.core.Tuple.*;

public class GroupTest {
    @Test
    public void creatingANewGroup() {
        Group g = group();
        assertEqualMatrices(g.transform, identity(4));
        assertThat(g.shapes.isEmpty(), is(true));
    }

    @Test
    public void aShapeHasAParentAttribute() {
        Shape s = test_shape();
        assertThat(s.parent, is(nullValue()));
    }

    @Test
    public void addingAChildToAGroup() {
        Group g = group();
        Shape s = test_shape();
        g.add(s);
        assertThat(g.shapes.isEmpty(), is(false));
        assertThat(g.shapes.contains(s), is(true));
        assertThat(s.parent, is(g));
    }

    @Test
    public void intersectingARayWithAnEmptyGroup() {
        Group g = group();
        Ray r = ray(point(0, 0, 0), vector(0, 0, 1));
        List<Intersection> xs = g.localIntersect(r);
        assertThat(xs.isEmpty(), is(true));
    }

    @Test
    public void intersectingARayWithANonEmptyGroup() {
        Group g = group();
        Sphere s1 = sphere();
        Sphere s2 = sphere();
        s2.transform = translation(0, 0, -3);
        Sphere s3 = sphere();
        s3.transform = translation(5, 0, 0);
        g.add(s1);
        g.add(s2);
        g.add(s3);

        Ray r = ray(point(0, 0, -5), vector(0, 0, 1));
        List<Intersection> xs = g.localIntersect(r);
        assertThat(xs.size(), is(4));
        assertThat(xs.get(0).object, is(s2));
        assertThat(xs.get(1).object, is(s2));
        assertThat(xs.get(2).object, is(s1));
        assertThat(xs.get(3).object, is(s1));
    }

    @Test
    public void intersectATransformedGroup() {
        Group g = group();
        g.transform = scaling(2, 2, 2);
        Sphere s = sphere();
        s.transform = translation(5, 0, 0);
        g.add(s);

        Ray r = ray(point(10, 0, -10), vector(0, 0, 1));
        List<Intersection> xs = g.localIntersect(r);
        assertThat(xs.size(), is(2));
    }

    @Test
    public void convertingAPointFromWorldToObjectSpace() {
        Group g1 = group();
        g1.transform = rotation_y(Math.PI / 2.0);

        Group g2 = group();
        g2.transform = scaling(2, 2, 2);

        g1.add(g2);

        Sphere s = sphere();
        s.transform = translation(5, 0, 0);
        g2.add(s);

        Tuple p = s.worldToObject(point(-2, 0, -10));
        assertEqualTuples(p, point(0, 0, -1));
    }

    @Test
    public void convertingANormalVectorFromObjectSpaceToWorldSpace() {
        Group g1 = group();
        g1.transform = rotation_y(Math.PI / 2.0);

        Group g2 = group();
        g2.transform = scaling(1, 2, 3);

        g1.add(g2);

        Sphere s = sphere();
        s.transform = translation(5, 0, 0);
        g2.add(s);

        Tuple n = s.normalToWorld(vector(Math.sqrt(3.0) / 3.0, Math.sqrt(3.0) / 3.0, Math.sqrt(3.0) / 3.0));
        assertEqualTuples(n, vector(0.2857, 0.4286, -0.8571));
    }

    @Test
    public void findingTheNormalOnAChildObject() {
        Group g1 = group();
        g1.transform = rotation_y(Math.PI / 2.0);

        Group g2 = group();
        g2.transform = scaling(1, 2, 3);

        g1.add(g2);

        Sphere s = sphere();
        s.transform = translation(5, 0, 0);
        g2.add(s);

        Tuple n = normal_at(s, point(1.7321, 1.1547, -5.5774));
        assertEqualTuples(n, vector(0.2857, 0.4286, -0.8571));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }

    private void assertEqualMatrices(Matrix a, Matrix b) {
        assertThat(a.size, is(equalTo(b.size)));
        for (int row = 0; row < a.size; row++) {
            for (int col = 0; col < a.size; col++) {
                assertThat(areEqual(a.data[row][col], b.data[row][col]), is(true));
            }
        }
    }

}