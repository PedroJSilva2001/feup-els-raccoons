package pt.up.fe.els2023.export;

import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.table.ITable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public abstract class TableExporter {
    protected final String endOfLine;
    protected final String table;
    protected final String filename;
    protected final String path;

    public TableExporter(String table, String filename, String path, String endOfLine) {
        this.table = table;
        this.filename = filename;
        this.path = path;
        this.endOfLine = endOfLine;
    }

    public void export(Map<String, ITable> tables) throws IOException, TableNotFoundException {
        Path fullPath = Paths.get(path, filename);
        ITable exportTable = tables.get(table);

        if (exportTable == null) {
            throw new TableNotFoundException(table);
        }

        try (FileWriter writer = new FileWriter(new File(fullPath.toString()).getAbsoluteFile())) {
            export(writer, exportTable);
        }
    }

    abstract void export(Writer writer, ITable table) throws IOException;

    public String getEndOfLine() {
        return endOfLine;
    }

    public String getTable() {
        return table;
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
        return Objects.equals(endOfLine, that.endOfLine) && Objects.equals(table, that.table) && Objects.equals(filename, that.filename) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endOfLine, table, filename, path);
    }
}
