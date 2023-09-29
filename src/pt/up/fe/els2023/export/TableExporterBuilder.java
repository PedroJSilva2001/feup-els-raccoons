package pt.up.fe.els2023.export;

public abstract class TableExporterBuilder<T extends TableExporter> {
    protected final String table;
    protected final String filename;
    protected final String path;
    protected String endOfLine;

    public TableExporterBuilder(String table, String filename, String path) {
        this.table = table;
        this.filename = filename;
        this.path = path;
        this.endOfLine = System.lineSeparator();
    }

    public void setEndOfLine(String endOfLine) {
        this.endOfLine = endOfLine;
    }

    public abstract T build();
}
