package pt.up.fe.els2023.export;

public class HtmlExporterBuilder extends TableExporterBuilder<HtmlExporter> {
    private String title;
    private String style;
    private boolean exportFullHtml;

    public HtmlExporterBuilder(String filename, String path) {
        super(filename, path);
        this.title = HtmlExporter.DEFAULT_TITLE; // TODO think about how to put filename here while taking into account the attribute values
        this.style = HtmlExporter.DEFAULT_STYLE;
        this.exportFullHtml = HtmlExporter.DEFAULT_EXPORT_FULL_HTML;
    }

    public HtmlExporterBuilder setStyle(String style) {
        this.style = style;
        return this;
    }

    public HtmlExporterBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public HtmlExporterBuilder setExportFullHtml(boolean exportFullHtml) {
        this.exportFullHtml = exportFullHtml;
        return this;
    }

    @Override
    public HtmlExporter build() {
        return new HtmlExporter(this.filename, this.path, this.endOfLine, this.title, this.style, this.exportFullHtml);
    }
}
