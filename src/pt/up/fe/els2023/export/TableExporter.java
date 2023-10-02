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

public abstract class TableExporter {
    protected final String endOfLine;
    private final String table;
    private final String filename;
    private final String path;

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
}
