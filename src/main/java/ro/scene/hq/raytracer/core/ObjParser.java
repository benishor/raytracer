package ro.scene.hq.raytracer.core;

import java.util.LinkedList;
import java.util.List;

import static ro.scene.hq.raytracer.core.Group.group;
import static ro.scene.hq.raytracer.core.Triangle.triangle;
import static ro.scene.hq.raytracer.core.Tuple.point;

public class ObjParser {
    public int ignoredLines = 0;
    public List<Tuple> vertices = new LinkedList<>();
    public Group defaultGroup = group();

    public static ObjParser parse(List<String> lines) {
        ObjParser parser = new ObjParser();

        for (String line : lines) {
            String[] pieces = line.trim().split(" ");
            if (pieces.length == 4 && pieces[0].equals("v")) {
                parser.vertices.add(point(
                        Double.parseDouble(pieces[1]),
                        Double.parseDouble(pieces[2]),
                        Double.parseDouble(pieces[3])
                ));
            } else if (pieces.length == 4 && pieces[0].equals("f")) {
                parser.defaultGroup.add(triangle(
                        parser.vertices.get(Integer.parseInt(pieces[1]) - 1),
                        parser.vertices.get(Integer.parseInt(pieces[2]) - 1),
                        parser.vertices.get(Integer.parseInt(pieces[3]) - 1)
                ));
            } else if (pieces.length > 4 && pieces[0].equals("f")) {
                int nrOfVerticesInPolygon = pieces.length - 1;
                for (int i = 1; i < (nrOfVerticesInPolygon - 1); i++) {
                    int a = 1;
                    int b = Integer.parseInt(pieces[i + 1]);
                    int c = Integer.parseInt(pieces[i + 2]);
                    parser.defaultGroup.add(triangle(
                            parser.vertices.get(a - 1),
                            parser.vertices.get(b - 1),
                            parser.vertices.get(c - 1)
                    ));
                }

            } else {
                parser.ignoredLines++;
            }
        }

        return parser;
    }
}
