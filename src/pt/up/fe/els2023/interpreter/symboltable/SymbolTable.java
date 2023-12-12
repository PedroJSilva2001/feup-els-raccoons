package pt.up.fe.els2023.interpreter.symboltable;

import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.model.schema.TableSchema;
import pt.up.fe.els2023.sources.TableSource;

import java.util.List;

public interface SymbolTable {

    String getRacoonsConfigFilename();

    void addVersion(String version);

    String getVersion();

    boolean hasSource(String name);

    Symbol<TableSource> getSource(String name);

    void addSource(String name, TableSource source, int declarationLine);

    boolean hasTableSchema(String name);

    Symbol<TableSchema> getTableSchema(String name);

    void addTableSchema(String name, TableSchema schema, int declarationLine);

    boolean hasExporter(String name);

    Symbol<TableExporter> getExporter(String name);

    void addExporter(String name, TableExporter exporter, int declarationLine);

    boolean hasRawSymbol(String name);

    Symbol<?> getRawSymbol(String name);

    void addRawSymbol(String name, int declarationLine);

    void updateRawSymbolType(String name, Symbol.Type type);

    boolean hasSymbol(String name);
    List<Symbol<?>> getSymbols();

    void updateRawSymbolValue(String name, Object value);
}
