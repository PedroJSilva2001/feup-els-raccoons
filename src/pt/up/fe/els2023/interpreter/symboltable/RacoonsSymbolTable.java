package pt.up.fe.els2023.interpreter.symboltable;

import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.model.schema.TableSchema;
import pt.up.fe.els2023.sources.TableSource;

import java.util.HashMap;
import java.util.Map;

public class RacoonsSymbolTable implements SymbolTable {

    private final String racoonsConfigFilename;

    private String version;

    private final Map<String, Symbol<TableSource>> sources;

    private final Map<String, Symbol<TableSchema>> tableSchemas;

    private final Map<String, Symbol<TableExporter>> exporters;

    private final Map<String, Symbol<?>> rawSymbols; // tables + values + maps

    public RacoonsSymbolTable(String fileName) {
        this.racoonsConfigFilename = fileName;
        this.sources = new HashMap<>();
        this.tableSchemas = new HashMap<>();
        this.exporters = new HashMap<>();
        this.rawSymbols = new HashMap<>();
    }

    @Override
    public String getRacoonsConfigFilename() {
        return racoonsConfigFilename;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void addVersion(String version) {
        this.version = version;
    }


    @Override
    public boolean hasSource(String name) {
        return sources.containsKey(name);
    }

    @Override
    public Symbol<TableSource> getSource(String name) {
        return sources.get(name);
    }

    @Override
    public void addSource(String name, TableSource source, int declarationLine) {
        sources.put(name, Symbol.of(name, Symbol.Type.SOURCE, source, declarationLine));
    }

    @Override
    public boolean hasTableSchema(String name) {
        return tableSchemas.containsKey(name);
    }

    @Override
    public Symbol<TableSchema> getTableSchema(String name) {
        return tableSchemas.get(name);
    }

    @Override
    public void addTableSchema(String name, TableSchema schema, int declarationLine) {
        tableSchemas.put(name, Symbol.of(name, Symbol.Type.TABLE_SCHEMA, schema, declarationLine));
    }

    @Override
    public boolean hasExporter(String name) {
        return exporters.containsKey(name);
    }

    @Override
    public Symbol<TableExporter> getExporter(String name) {
        return exporters.get(name);
    }

    @Override
    public void addExporter(String name, TableExporter exporter, int declarationLine) {
        exporters.put(name, Symbol.of(name, Symbol.Type.EXPORTER, exporter, declarationLine));
    }

    @Override
    public boolean hasRawSymbol(String name) {
        return rawSymbols.containsKey(name);
    }

    @Override
    public Symbol<?> getRawSymbol(String name) {
        return rawSymbols.get(name);
    }

    @Override
    public void addRawSymbol(String name, int declarationLine) {
        rawSymbols.put(name, Symbol.ofVoid(name, declarationLine));
    }

    @Override
    public void updateRawSymbolType(String name, Symbol.Type type) {
        var symbol = rawSymbols.get(name);

        rawSymbols.put(name, Symbol.of(name, type, symbol.value(), symbol.declarationLine()));
    }

    @Override
    public void updateRawSymbolValue(String name, Object value) {
        var symbol = rawSymbols.get(name);

        rawSymbols.put(name, Symbol.of(name, symbol.type(), value, symbol.declarationLine()));
    }
}
