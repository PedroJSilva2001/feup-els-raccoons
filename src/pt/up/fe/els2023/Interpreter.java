package pt.up.fe.els2023;

import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    Map<String, ITable> buildTables(Config config) {
        Map<String, ITable> tables = new HashMap<>();

        for (var schema : config.tableSchemas()) {
            var table = buildTable(schema);

            if (table == null) {
                // TODO better error handling
                return null;
            }

            tables.put(table.getName(), table);
        }
        return tables;
    }

    public ITable buildTable(TableSchema schema) {
        ITable table = new Table(schema.name(), schema.source());

        for (var columnSchema : schema.columnSchemas()) {
            String columnName = columnSchema.name() == null? columnSchema.from() : columnSchema.name();

            if (!table.addColumn(columnName)) {
                System.out.println("Column " + columnName + " in table " + table.getName() + " already exists");
                return null;
            }
        }

        if (schema.source() != null) {
            schema.source().populateTableFrom(schema, table);
        }

        return table;
    }

    void executeOperations(Config config) {}
}
