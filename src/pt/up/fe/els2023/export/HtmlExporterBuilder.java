package pt.up.fe.els2023.export;

import pt.up.fe.els2023.table.Table;

public class HtmlExporterBuilder extends TableExporterBuilder<HtmlExporter> {
    private String title;
    private String style;
    private boolean exportFullHtml = false;

    public HtmlExporterBuilder(String filename, String path) {
        super(filename, path);
        this.title = filename;
        this.style = """
                table {
                   border-collapse: collapse;
                   width: 100%;
                }
                th, td {
                   text-align: left;
                   padding: 8px;
                }
                tr:nth-child(even){background-color: #f2f2f2}
                th {
                   background-color: #4CAF50;
                   color: white;
                }""";
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
