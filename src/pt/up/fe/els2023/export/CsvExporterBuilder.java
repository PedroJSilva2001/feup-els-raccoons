package pt.up.fe.els2023.export;

public class CsvExporterBuilder extends TableExporterBuilder<CsvExporter> {
    private String separator;

    public CsvExporterBuilder(String table, String filename, String path) {
        super(table, filename, path);
        this.separator = ",";
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public CsvExporter build() {
        return new CsvExporter(this.table, this.filename, this.path, this.endOfLine, this.separator);
    }
}
