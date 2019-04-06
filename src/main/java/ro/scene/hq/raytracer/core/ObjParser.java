package ro.scene.hq.raytracer.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static ro.scene.hq.raytracer.core.Group.group;
import static ro.scene.hq.raytracer.core.Triangle.triangle;
import static ro.scene.hq.raytracer.core.Tuple.point;

public class ObjParser {
    public int ignoredLines = 0;
    public List<Tuple> vertices = new LinkedList<>();
    public Group defaultGroup = group();
    public Group currentGroup = defaultGroup;

    public void parseLine(String line) {
        String[] pieces = line.trim().split(" ");
        if (line.startsWith("g ")) {
            currentGroup = group();
            currentGroup.name = line.trim().substring(2);
            defaultGroup.add(currentGroup);
        } else if (pieces.length == 4 && pieces[0].equals("v")) {
            vertices.add(point(
                    Double.parseDouble(pieces[1]),
                    Double.parseDouble(pieces[2]),
                    Double.parseDouble(pieces[3])
            ));
        } else if (pieces.length == 4 && pieces[0].equals("f")) {
            currentGroup.add(triangle(
                    vertices.get(Integer.parseInt(pieces[1]) - 1),
                    vertices.get(Integer.parseInt(pieces[2]) - 1),
                    vertices.get(Integer.parseInt(pieces[3]) - 1)
            ));
        } else if (pieces.length > 4 && pieces[0].equals("f")) {
            int nrOfVerticesInPolygon = pieces.length - 1;
            for (int i = 1; i < (nrOfVerticesInPolygon - 1); i++) {
                int a = 1;
                int b = Integer.parseInt(pieces[i + 1]);
                int c = Integer.parseInt(pieces[i + 2]);
                currentGroup.add(triangle(
                        vertices.get(a - 1),
                        vertices.get(b - 1),
                        vertices.get(c - 1)
                ));
            }
        } else {
            ignoredLines++;
        }
    }

    public static ObjParser parse(List<String> lines) {
        ObjParser parser = new ObjParser();
        for (String line : lines) {
            parser.parseLine(line);
        }
        return parser;
    }

    public static ObjParser parse(String filename) throws IOException {
        ObjParser parser = new ObjParser();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                parser.parseLine(line);
            }
        }
        return parser;
    }

    public Group groupByName(String groupName) {
        for (Shape s : defaultGroup.shapes) {
            if (s instanceof Group && groupName.equals(((Group) s).name)) {
                return (Group) s;
            }
        }
        return null;
    }

    public Group toGroup() {
        return defaultGroup;
    }
}
