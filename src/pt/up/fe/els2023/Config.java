package pt.up.fe.els2023;

import java.util.List;
import java.util.Map;

public class Config {
    Map<String, TableSource> tableSources;
    Map<String, TableSchema> tableSchemas;
    List<TableOperation> operations;
    List<TableExporter> exporters;
}
