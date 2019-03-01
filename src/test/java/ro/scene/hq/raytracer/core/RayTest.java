package ro.scene.hq.raytracer.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Ray.position;
import static ro.scene.hq.raytracer.core.Ray.ray;
import static ro.scene.hq.raytracer.core.Tuple.point;
import static ro.scene.hq.raytracer.core.Tuple.vector;

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
}