package pt.up.fe.els2023.export;

import pt.up.fe.els2023.Config;
import pt.up.fe.els2023.table.ITable;

import java.io.Writer;

public class CsvExporter extends TableExporter {
    private final String separator;

    public CsvExporter(String table, String filename, String path, String endOfLine, String separator) {
        super(table, filename, path, endOfLine);
        this.separator = separator;
    }

    @Override
    void export(Writer writer, ITable table) {

    }

    public String getSeparator() {
        return separator;
    }
}
