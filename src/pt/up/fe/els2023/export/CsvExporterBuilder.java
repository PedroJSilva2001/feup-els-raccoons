package pt.up.fe.els2023.export;

public class CsvExporterBuilder extends TableExporterBuilder<CsvExporter> {
    private String separator;

    public CsvExporterBuilder(String filename, String path) {
        super(filename, path);
        this.separator = CsvExporter.DEFAULT_SEPARATOR;
    }

    public CsvExporterBuilder setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    @Override
    public CsvExporter build() {
        return new CsvExporter(this.filename, this.path, this.endOfLine, this.separator);
    }
}
