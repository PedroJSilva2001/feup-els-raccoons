package pt.up.fe.els2023.export;

import pt.up.fe.els2023.model.table.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class TableExporter {
    public record AttributeValue(Type type, Object defaultValue, boolean required) {
        public enum Type {
            STRING(),
            BOOLEAN()
        }
    }

    protected final String endOfLine;
    protected final String filename;
    protected final String path;

    public TableExporter(String filename, String path, String endOfLine) {
        this.filename = filename;
        this.path = path;
        this.endOfLine = endOfLine;
    }

    public void export(Table table) throws IOException {
        Path fullPath = Paths.get(path, filename);

        try (FileWriter writer = new FileWriter(new File(fullPath.toString()).getAbsoluteFile())) {
            export(writer, table);
        }
    }

    abstract void export(Writer writer, Table table) throws IOException;

    public String getEndOfLine() {
        return endOfLine;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableExporter that)) return false;
        return Objects.equals(endOfLine, that.endOfLine) && Objects.equals(filename, that.filename) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endOfLine, filename, path);
    }
}
