package ro.scene.hq.raytracer.core;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    public void trianglesInGroups() throws IOException {
        String pathToObjFile = getClass().getClassLoader().getResource("triangles.obj").getFile();
        ObjParser parser = ObjParser.parse(pathToObjFile);
        Group g1 = parser.groupByName("FirstGroup");
        Group g2 = parser.groupByName("SecondGroup");
        Triangle t1 = (Triangle) g1.shapes.get(0);
        Triangle t2 = (Triangle) g2.shapes.get(0);

        assertEqualTuples(t1.p1, parser.vertices.get(0));
        assertEqualTuples(t1.p2, parser.vertices.get(1));
        assertEqualTuples(t1.p3, parser.vertices.get(2));

        assertEqualTuples(t2.p1, parser.vertices.get(0));
        assertEqualTuples(t2.p2, parser.vertices.get(2));
        assertEqualTuples(t2.p3, parser.vertices.get(3));
    }

    @Test
    public void convertingAnObjFileToAGroup() throws IOException {
        String pathToObjFile = getClass().getClassLoader().getResource("triangles.obj").getFile();
        ObjParser parser = ObjParser.parse(pathToObjFile);
        Group g = parser.toGroup();
        assertThat(g.shapes.contains(parser.groupByName("FirstGroup")), is(true));
        assertThat(g.shapes.contains(parser.groupByName("SecondGroup")), is(true));
    }

    private void assertEqualTuples(Tuple a, Tuple b) {
        assertThat(areEqual(a.x, b.x), is(true));
        assertThat(areEqual(a.y, b.y), is(true));
        assertThat(areEqual(a.z, b.z), is(true));
        assertThat(areEqual(a.w, b.w), is(true));
    }
}