package pt.up.fe.els2023.export;

public class LatexExporterBuilder extends TableExporterBuilder<LatexExporter> {
    public LatexExporterBuilder(String table, String filename, String path) {
        super(table, filename, path);
    }

    @Override
    public LatexExporter build() {
        return new LatexExporter(this.table, this.filename, this.path, this.endOfLine);
    }
}
