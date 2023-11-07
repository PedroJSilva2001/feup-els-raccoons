package pt.up.fe.els2023.export;

import pt.up.fe.els2023.table.Table;

public class TsvExporter extends CsvExporter {
    public TsvExporter(String filename, String path, String endOfLine) {
        super(filename, path, endOfLine, "\t");
    }
}
