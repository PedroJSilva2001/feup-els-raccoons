package pt.up.fe.els2023;

import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.operations.TableOperation;
import pt.up.fe.els2023.sources.TableSource;

import java.util.List;
import java.util.Map;

public class Config {
    Map<String, TableSource> tableSources;
    Map<String, TableSchema> tableSchemas;
    List<TableOperation> operations;
    List<TableExporter> exporters;

    public Config(Map<String, TableSource> tableSources, Map<String, TableSchema> tableSchemas, List<TableOperation> operations, List<TableExporter> exporters) {
        this.tableSources = tableSources;
        this.tableSchemas = tableSchemas;
        this.operations = operations;
        this.exporters = exporters;
    }
}
