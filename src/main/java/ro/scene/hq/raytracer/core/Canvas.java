package ro.scene.hq.raytracer.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static ro.scene.hq.raytracer.core.Tuple.color;

public class Canvas {
    public final int width;
    public final int height;
    private final ArrayList<Tuple> buffer;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;

        buffer = new ArrayList<>(width * height);
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                buffer.add(color(0, 0, 0));
            }
        }
    }

    public Tuple pixelAt(int x, int y) {
        return buffer.get(x + y * width);
    }

    public static Canvas canvas(int w, int h) {
        return new Canvas(w, h);
    }

    public void writePixel(int x, int y, Tuple color) {
        buffer.set(x + y * width, color);
    }

    public List<String> toPPM() {
        List<String> result = new LinkedList<>();
        result.add("P3");
        result.add(width + " " + height);
        result.add("255");

        StringBuffer lineBuffer = new StringBuffer();

        for (int y = 0; y < height; y++) {
            lineBuffer.setLength(0);
            for (int x = 0; x < width; x++) {
                Tuple color = pixelAt(x, y);
                int r = (int) (Math.round(Math.max(Math.min(color.x, 1.0), 0.0) * 255));
                int g = (int) (Math.round(Math.max(Math.min(color.y, 1.0), 0.0) * 255));
                int b = (int) (Math.round(Math.max(Math.min(color.z, 1.0), 0.0) * 255));

                String rAsString = String.valueOf(r);
                String gAsString = String.valueOf(g);
                String bAsString = String.valueOf(b);

                if ((lineBuffer.length() + rAsString.length()) > 70) {
                    result.add(lineBuffer.toString().trim());
                    lineBuffer.setLength(0);
                }
                lineBuffer.append(rAsString);
                lineBuffer.append(' ');

                if ((lineBuffer.length() + gAsString.length()) > 70) {
                    result.add(lineBuffer.toString().trim());
                    lineBuffer.setLength(0);
                }
                lineBuffer.append(gAsString);
                lineBuffer.append(' ');

                if ((lineBuffer.length() + bAsString.length()) > 70) {
                    result.add(lineBuffer.toString().trim());
                    lineBuffer.setLength(0);
                }
                lineBuffer.append(bAsString);
                lineBuffer.append(' ');
            }
            String currentLine = lineBuffer.toString().trim();
            if (!currentLine.isEmpty()) {
                result.add(currentLine);
            }
        }

        return result;
    }

    public void fill(Tuple color) {
        for (int i = 0; i < buffer.size(); i++) {
            buffer.set(i, color);
        }
    }
}
