package pt.up.fe.els2023.export;

public class TsvExporter extends CsvExporter{
    public TsvExporter(String table, String filename, String path, String endOfLine) {
        super(table, filename, path, endOfLine, "\t");
    }
}
