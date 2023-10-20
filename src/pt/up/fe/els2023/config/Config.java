package pt.up.fe.els2023.config;

import pt.up.fe.els2023.config.TableSchema;
import pt.up.fe.els2023.export.TableExporter;
import pt.up.fe.els2023.operations.TableOperation;
import pt.up.fe.els2023.sources.TableSource;

import java.util.List;
import java.util.Map;

/**
 * The configuration object as read from the configuration file.
 *
 * @param tableSources The sources from which to read the data.
 * @param tableSchemas The schemas to which the data will be mapped.
 * @param operations The operations to be performed on the data.
 * @param exporters The exporters to which the data will be written.
 */
public record Config(
        Map<String, TableSource> tableSources,
        List<TableSchema> tableSchemas,
        List<TableOperation> operations,
        List<TableExporter> exporters
) {
}
