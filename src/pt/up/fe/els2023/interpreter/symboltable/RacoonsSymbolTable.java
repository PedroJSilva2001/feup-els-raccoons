package pt.up.fe.els2023.interpreter.symboltable;

import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.model.schema.TableSchema;
import pt.up.fe.els2023.sources.TableSource;

import java.util.HashMap;
import java.util.Map;

public class RacoonsSymbolTable implements SymbolTable {

    private String racoonsConfigFilename;

    private String version;

    private Map<String, TableSource> sources;

    private Map<String, TableSchema> tableSchemas;

    private Map<String, TableExporter> exporters;

    public RacoonsSymbolTable(String fileName) {
        this.racoonsConfigFilename = fileName;
        this.sources = new HashMap<>();
        this.tableSchemas = new HashMap<>();
        this.exporters = new HashMap<>();
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
    public TableSource getSource(String name) {
        return sources.get(name);
    }

    @Override
    public void addSource(String name, TableSource source) {
        sources.put(name, source);
    }

    @Override
    public boolean hasTableSchema(String name) {
        return tableSchemas.containsKey(name);
    }

    @Override
    public TableSchema getTableSchema(String name) {
        return tableSchemas.get(name);
    }

    @Override
    public void addTableSchema(String name, TableSchema schema) {
        tableSchemas.put(name, schema);
    }

    @Override
    public boolean hasExporter(String name) {
        return exporters.containsKey(name);
    }

    @Override
    public TableExporter getExporter(String name) {
        return exporters.get(name);
    }

    @Override
    public void addExporter(String name, TableExporter exporter) {
        exporters.put(name, exporter);
    }
}
