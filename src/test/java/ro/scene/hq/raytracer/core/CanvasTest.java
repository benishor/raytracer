package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ro.scene.hq.raytracer.core.Canvas.canvas;
import static ro.scene.hq.raytracer.core.Tuple.color;

public class CanvasTest {

    @Test
    public void createCanvas() {
        Canvas c = new Canvas(10, 20);

        assertThat(c.width, is(equalTo(10)));
        assertThat(c.height, is(equalTo(20)));

        Tuple black = color(0, 0, 0);
        for (int y = 0; y < c.height; y++) {
            for (int x = 0; x < c.width; x++) {
                assertThat(c.pixelAt(x, y), is(equalTo(black)));
            }
        }
    }

    @Test
    public void writingPixelsToACanvas() {
        Canvas c = canvas(10, 20);
        Tuple red = color(1, 0, 0);

        c.writePixel(2, 3, red);
        assertThat(c.pixelAt(2, 3), is(equalTo(red)));
    }

    @Test
    public void constructingPPMHeader() {
        Canvas c = canvas(5, 3);
        List<String> ppm = c.toPPM();

        assertThat(ppm.subList(0, 3), is(equalTo(Arrays.asList("P3", "5 3", "255"))));
    }

    @Test
    public void constructingPPMPixelData() {
        // given
        Canvas c = canvas(5, 3);

        Tuple c1 = color(1.5, 0, 0);
        Tuple c2 = color(0, 0.5, 0);
        Tuple c3 = color(-0.5, 0, 1);

        // when
        c.writePixel(0, 0, c1);
        c.writePixel(2, 1, c2);
        c.writePixel(4, 2, c3);
        List<String> ppm = c.toPPM();

        // then
        assertThat(ppm.subList(3, 6), is(equalTo(Arrays.asList(
                "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 128 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255"
        ))));
    }

    @Test
    public void splittingLongLinesInPPM() {
        // given
        Canvas c = canvas(10, 2);
        c.fill(color(1, 0.8, 0.6));

        // when
        List<String> ppm = c.toPPM();

        // then
        assertThat(ppm.subList(3, 7), is(equalTo(Arrays.asList(
                "255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204",
                "153 255 204 153 255 204 153 255 204 153 255 204 153",
                "255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204",
                "153 255 204 153 255 204 153 255 204 153 255 204 153"
        ))));
    }
}