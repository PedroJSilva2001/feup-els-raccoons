package pt.up.fe.els2023.export;

public class TsvExporterBuilder extends TableExporterBuilder<TsvExporter> {
    public TsvExporterBuilder(String filename, String path) {
        super(filename, path);
    }

    @Override
    public TsvExporter build() {
        return new TsvExporter(this.filename, this.path, this.endOfLine);
    }
}
