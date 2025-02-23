package pt.up.fe.els2023.export;

public class LatexExporterBuilder extends TableExporterBuilder<LatexExporter> {
    public LatexExporterBuilder(String filename, String path) {
        super(filename, path);
    }

    @Override
    public LatexExporter build() {
        return new LatexExporter(this.filename, this.path, this.endOfLine);
    }
}
