package pt.up.fe.els2023.interpreter;

import pt.up.fe.els2023.model.operations.TableOperation;
import pt.up.fe.els2023.sources.TableSource;
import pt.up.fe.els2023.table.schema.TableSchema;

import java.util.List;
import java.util.Map;

/**
 * The configuration object as read from the configuration file.
 *
 * @param tableSources The sources from which to read the data.
 * @param tableSchemas The schemas to which the data will be mapped.
 * @param operations   The operations to be performed on the data.
 */
public record InterpreterData(
        Map<String, TableSource> tableSources,
        List<TableSchema> tableSchemas,
        List<TableOperation> operations
) {
}
