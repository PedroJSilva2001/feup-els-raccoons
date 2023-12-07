package pt.up.fe.els2023.export;

public class TsvExporter extends CsvExporter {
    public TsvExporter(String filename, String path, String endOfLine) {
        super(filename, path, endOfLine, "\t");
    }
}
