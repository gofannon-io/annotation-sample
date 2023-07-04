package io.gofannon.apl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class PropertiesGenerator implements AutoCloseable {

    private final PrintWriter printWriter;


    public PropertiesGenerator(OutputStream out) {
        this.printWriter = new PrintWriter(out, true, StandardCharsets.UTF_8);
    }

    public PropertiesGenerator comment(String comment) {
        String[] lines = comment.replaceAll("\r", "").split("\n");
        for (String line : lines) {
            printWriter.print("# ");
            printWriter.println(line);
        }

        return this;
    }

    public PropertiesGenerator property(String name, Object value) {
        String line = formatProperty(name, value);
        printWriter.println(line);
        return this;
    }

    private String formatProperty(String name, Object value) {
        return name + " = " + (value == null ? "" : value);
    }

    public PropertiesGenerator commentedProperty(String name, Object value) {
        printWriter.print("# ");
        String line = formatProperty(name, value);
        printWriter.println(line);
        return this;
    }

    public PropertiesGenerator skipLines(int count) {
        if (count < 0)
            throw new IllegalArgumentException();
        for (int i = 0; i < count; i++) {
            printWriter.println();
        }
        return this;
    }

    @Override
    public void close() throws IOException {
        printWriter.close();
    }
}
