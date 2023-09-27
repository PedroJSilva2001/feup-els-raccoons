package pt.up.fe.els2023;

public abstract class TableExporter {
    String table;
    String filename;
    String path;

    public abstract Void export(ITable t);
}
