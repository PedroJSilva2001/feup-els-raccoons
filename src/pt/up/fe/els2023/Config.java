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
}
