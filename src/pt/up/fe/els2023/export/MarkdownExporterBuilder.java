package pt.up.fe.els2023.export;

public class MarkdownExporterBuilder extends TableExporterBuilder<MarkdownExporter> {
    public MarkdownExporterBuilder(String filename, String path) {
        super(filename, path);
    }

    @Override
    public MarkdownExporter build() {
        return new MarkdownExporter(filename, path, endOfLine);
    }
}
