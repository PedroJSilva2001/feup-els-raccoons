package pt.up.fe.els2023.export;

public class TsvExporterBuilder extends TableExporterBuilder<TsvExporter> {
    public TsvExporterBuilder(String table, String filename, String path) {
        super(table, filename, path);
    }

    @Override
    public TsvExporter build() {
        return new TsvExporter(this.table, this.filename, this.path, this.endOfLine);
    }
}
