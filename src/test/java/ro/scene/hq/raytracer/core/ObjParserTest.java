package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static ro.scene.hq.raytracer.core.Tuple.areEqual;
import static ro.scene.hq.raytracer.core.Tuple.point;

public class ObjParserTest {

    @Test
    public void ignoringUnrecognizedLines() {
        List<String> lines = Arrays.asList(
                "There was a young lady named Bright",
                "who traveled much faster than light.",
                "She set out one day",
                "in a relative way,",
                "and came back the previous night."
        );
        ObjParser parser = ObjParser.parse(lines);
        assertThat(parser.ignoredLines, is(equalTo(5)));
    }

    @Test
    public void vertexRecords() {
        List<String> lines = Arrays.asList(
                "v -1 1 0",
                "v -1.0000 0.5000 0.0000",
                "v 1 0 0",
                "v 1 1 0"
        );
        ObjParser parser = ObjParser.parse(lines);
        assertEqualTuples(parser.vertices.get(0), point(-1, 1, 0));
        assertEqualTuples(parser.vertices.get(1), point(-1, 0.5, 0));
        assertEqualTuples(parser.vertices.get(2), point(1, 0, 0));
        assertEqualTuples(parser.vertices.get(3), point(1, 1, 0));
    }

    @Test
    public void parsingTriangleFaces() {
        List<String> lines = Arrays.asList(
                "v -1 1 0",
                "v -1 0 0",
                "v 1 0 0",
                "v 1 1 0",
                "",
                "f 1 2 3",
                "f 1 3 4"
        );
        ObjParser parser = ObjParser.parse(lines);
        Group g = parser.defaultGroup;
        Triangle t1 = (Triangle) g.shapes.get(0);
        Triangle t2 = (Triangle) g.shapes.get(1);

        assertEqualTuples(t1.p1, parser.vertices.get(0));
        assertEqualTuples(t1.p2, parser.vertices.get(1));
        assertEqualTuples(t1.p3, parser.vertices.get(2));

        assertEqualTuples(t2.p1, parser.vertices.get(0));
        assertEqualTuples(t2.p2, parser.vertices.get(2));
        assertEqualTuples(t2.p3, parser.vertices.get(3));
    }

    @Test
    public void triangulatingPolygons() {
        List<String> lines = Arrays.asList(
                "v -1 1 0",
                "v -1 0 0",
                "v 1 0 0",
                "v 1 1 0",
                "v 0 2 0",
                "",
                "f 1 2 3 4 5"
        );
        ObjParser parser = ObjParser.parse(lines);
        Group g = parser.defaultGroup;
        Triangle t1 = (Triangle) g.shapes.get(0);
        Triangle t2 = (Triangle) g.shapes.get(1);
        Triangle t3 = (Triangle) g.shapes.get(2);

        assertEqualTuples(t1.p1, parser.vertices.get(0));
        assertEqualTuples(t1.p2, parser.vertices.get(1));
        assertEqualTuples(t1.p3, parser.vertices.get(2));

        assertEqualTuples(t2.p1, parser.vertices.get(0));
        assertEqualTuples(t2.p2, parser.vertices.get(2));
        assertEqualTuples(t2.p3, parser.vertices.get(3));

        assertEqualTuples(t3.p1, parser.vertices.get(0));
        assertEqualTuples(t3.p2, parser.vertices.get(3));
        assertEqualTuples(t3.p3, parser.vertices.get(4));
    }

    @Test
    public void trianglesInGroups() {
        fail("not implemented yet");
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}