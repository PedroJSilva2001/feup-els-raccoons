package pt.up.fe.els2023.interpreter.symboltable;

import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.model.schema.TableSchema;
import pt.up.fe.els2023.sources.TableSource;

public interface SymbolTable {

    String getRacoonsConfigFilename();

    void addVersion(String version);

    String getVersion();

    boolean hasSource(String name);

    TableSource getSource(String name);

    void addSource(String name, TableSource source);

    boolean hasTableSchema(String name);

    TableSchema getTableSchema(String name);

    void addTableSchema(String name, TableSchema schema);

    boolean hasExporter(String name);

    TableExporter getExporter(String name);

    void addExporter(String name, TableExporter exporter);

}
