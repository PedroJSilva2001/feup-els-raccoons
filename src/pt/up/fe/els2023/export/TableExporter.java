package pt.up.fe.els2023.export;

import pt.up.fe.els2023.table.ITable;

public abstract class TableExporter {
    String table;
    String filename;
    String path;

    public abstract Void export(ITable t);
}
