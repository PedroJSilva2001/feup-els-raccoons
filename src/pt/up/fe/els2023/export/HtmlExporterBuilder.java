package pt.up.fe.els2023.export;

public class HtmlExporterBuilder extends TableExporterBuilder<HtmlExporter> {
    private String title;
    private String style;

    public HtmlExporterBuilder(String table, String filename, String path) {
        super(table, filename, path);
        this.title = "Table";
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

    public void setStyle(String style) {
        this.style = style;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public HtmlExporter build() {
        return new HtmlExporter(this.table, this.filename, this.path, this.endOfLine, this.title, this.style);
    }
}
