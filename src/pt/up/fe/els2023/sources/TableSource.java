package pt.up.fe.els2023.sources;

import pt.up.fe.els2023.TableSchema;
import pt.up.fe.els2023.table.ITable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TableSource<ResourceNodeType> {
    protected String name;

    protected List<String> files;

    public TableSource(String name, List<String> files) {
        this.name = name;
        this.files = files;
    }

    public void populateTableFrom(TableSchema schema, ITable table) {
        for (var file : files) {
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
                var rootNode = getResourceRootNode(reader);

                for (var columnSchema : schema.columnSchemas()) {
                    if (columnSchema.from() == null) {
                        rowValues.add(null);
                        continue;
                    }

                    var value = getPropertyValue(rootNode, columnSchema.from());

                    rowValues.add(value);
                }

                table.addRow(rowValues);
            } catch (IOException e) {
                // TODO
            }
        }
    }

    protected abstract ResourceNodeType getResourceRootNode(Reader reader) throws IOException;

    protected abstract String getPropertyValue(ResourceNodeType rootNode, String attributePath);
}
