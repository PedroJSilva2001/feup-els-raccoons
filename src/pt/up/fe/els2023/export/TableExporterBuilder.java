package pt.up.fe.els2023.export;

public abstract class TableExporterBuilder<T extends TableExporter> {
    protected final String filename;
    protected final String path;
    protected String endOfLine;

    public TableExporterBuilder(String filename, String path) {
        this.filename = filename;
        this.path = path;
        this.endOfLine = System.lineSeparator();
    }

    public TableExporterBuilder<T> setEndOfLine(String endOfLine) {
        this.endOfLine = endOfLine;
        return this;
    }

    public abstract T build();
}
