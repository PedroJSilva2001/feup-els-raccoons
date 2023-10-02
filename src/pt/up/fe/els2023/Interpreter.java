package pt.up.fe.els2023;

import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            populateTableFromSource(schema, table);
        }

        return table;
    }

    public void populateTableFromSource(TableSchema schema, ITable table) {
        for (var file : schema.source().getFiles()) {
            List<Object> rowValues = new ArrayList<>();

            BufferedReader reader = null;

            try {
                var fileReader = new FileReader(file);
                reader = new BufferedReader(fileReader);
            } catch (FileNotFoundException e) {
                System.out.println("File " + file + " not found");
                return;
            }

            // First column is always source file
            rowValues.add(file);

            try {
                var rootNode = schema.source().getResourceParser().parse(reader);

                for (var columnSchema : schema.columnSchemas()) {
                    if (columnSchema.from() == null) {
                        rowValues.add(null);
                        continue;
                    }

                    var value = rootNode.getNested(columnSchema.from());

                    rowValues.add(value);
                }

                table.addRow(rowValues);
            } catch (IOException e) {
                // TODO
            }
        }
    }


    void executeOperations(Config config) {}
}
