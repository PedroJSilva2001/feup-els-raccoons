package pt.up.fe.els2023.export;

public class MarkdownExporterBuilder extends TableExporterBuilder<MarkdownExporter> {
    public MarkdownExporterBuilder(String table, String filename, String path) {
        super(table, filename, path);
    }

    @Override
    public MarkdownExporter build() {
        return new MarkdownExporter(table, filename, path, endOfLine);
    }
}
