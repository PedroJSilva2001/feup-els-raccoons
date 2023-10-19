package pt.up.fe.els2023;

import pt.up.fe.els2023.config.Config;
import pt.up.fe.els2023.config.TableSchema;
import pt.up.fe.els2023.sources.TableSource;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.utils.GlobFinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
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

    List<String> columnNames(TableSchema schema) {
        BufferedReader reader = null;
        // TODO: Not sure if getting the first file is the best way to do this
        List<Path> files = null;
        try {
            files = GlobFinder.getFilesGlob(schema.source().getFiles().get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var file = files.get(0);

        try {
            var fileReader = new FileReader(file.toFile());
            reader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            System.out.println("File " + file + " not found");
            return null;
        }

        return null;
    }

    public ITable buildTable(TableSchema schema) {
        ITable table = new Table(schema.name(), schema.source());

        List<String> columnNames = columnNames(schema);
        columnNames.forEach(table::addColumn);

//        if (schema.source() != null) {
//            populateTableFromSource(schema, table);
//        }

        return table;
    }

    private List<Path> getFiles(TableSource source) {
        List<Path> files = new ArrayList<>();

        source.getFiles().forEach((glob) -> {
            try {
                files.addAll(GlobFinder.getFilesGlob(glob));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return files
                .stream()
                .distinct()
                .toList();
    }

    public void populateTable(TableSchema schema) {
        TableSource source = schema.source();
        List<Path> files = getFiles(source);


    }

    public void populateTableFromSource(TableSchema schema, ITable table) {
//        for (var file : schema.source().getFiles()) {
//            List<Object> rowValues = new ArrayList<>();
//
//            BufferedReader reader = null;
//
//            try {
//                var fileReader = new FileReader(file);
//                reader = new BufferedReader(fileReader);
//            } catch (FileNotFoundException e) {
//                System.out.println("File " + file + " not found");
//                return;
//            }
//
//            // First column is always source file
//            rowValues.add(file);
//
//            try {
//                var rootNode = schema.source().getResourceParser().parse(reader);
//
//                for (var columnSchema : schema.columnSchemas()) {
//                    if (columnSchema.from() == null) {
//                        rowValues.add(null);
//                        continue;
//                    }
//
//                    var value = rootNode.getNested(columnSchema.from());
//
//                    rowValues.add(value);
//                }
//
//                table.addRow(rowValues);
//            } catch (IOException e) {
//                // TODO
//            }
//        }
    }


    void executeOperations(Config config) {
    }
}
