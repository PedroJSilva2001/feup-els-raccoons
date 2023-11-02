package pt.up.fe.els2023.config;

import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.operations.Pipeline;
import pt.up.fe.els2023.sources.TableSource;

import java.util.List;
import java.util.Map;

public record Config(
        Map<String, TableSource> tableSources,
        List<TableSchema> tableSchemas,
        List<Pipeline> operations,
        List<TableExporter> exporters
) {
}
