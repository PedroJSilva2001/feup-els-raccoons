package pt.up.fe.els2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.export.CsvExporter;
import pt.up.fe.els2023.sources.JsonSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConfigReaderTest {

    @Test
    public void testReadConfig() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/yaml/config.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedTableSources = Map.of(
                "file1", new JsonSource("file1", List.of("file1.json", "file2.json"))
        );

        var expectedTableSchemas = List.of(
                new TableSchema("table1", List.of(
                        new ColumnSchema("Zau", "prop1.prop2"),
                        new ColumnSchema("prop3", "prop3"), // empty name
                        new ColumnSchema("Zau 2", null) // empty from
                ), new JsonSource("file1", List.of("file1.json", "file2.json"))),
                new TableSchema("table2", List.of(
                        new ColumnSchema("Zau master", null),
                        new ColumnSchema(null, null)
                ), null) // source doesn't exist
        );

        var expectedExporters = List.of(
                new CsvExporter("table1", "Table 1", "/dir1/dir2", System.lineSeparator(), ",")
        );

        for (var expectedTableSource : expectedTableSources.entrySet()) {
            var resultTableSource = config.tableSources().get(expectedTableSource.getKey());
            Assertions.assertEquals(expectedTableSource.getValue().getName(), resultTableSource.getName());
            Assertions.assertEquals(expectedTableSource.getValue().getFiles(), resultTableSource.getFiles());
        }

        for (int i = 0; i < expectedTableSchemas.size(); i++) {
            var expectedTableSchema = expectedTableSchemas.get(i);
            var resultTableSchema = config.tableSchemas().get(i);
            for (int j = 0; j < expectedTableSchema.columnSchemas().size(); j++) {
                var expectedColumnSchema = expectedTableSchema.columnSchemas().get(j);
                var resultColumnSchema = resultTableSchema.columnSchemas().get(j);
                Assertions.assertEquals(expectedColumnSchema.name(), resultColumnSchema.name());
                Assertions.assertEquals(expectedColumnSchema.from(), resultColumnSchema.from());
            }
            Assertions.assertEquals(expectedTableSchema.name(), resultTableSchema.name());
            if (expectedTableSchema.source() != null) {
                Assertions.assertEquals(expectedTableSchema.source().getName(), resultTableSchema.source().getName());
                Assertions.assertEquals(expectedTableSchema.source().getFiles(), resultTableSchema.source().getFiles());
            } else {
                Assertions.assertNull(resultTableSchema.source());
            }
        }

        for (int i = 0; i < expectedExporters.size(); i++) {
            var expectedExporter = expectedExporters.get(i);
            var resultExporter = config.exporters().get(i);
            Assertions.assertEquals(expectedExporter.getTable(), resultExporter.getTable());
            Assertions.assertEquals(expectedExporter.getFilename(), resultExporter.getFilename());
            Assertions.assertEquals(expectedExporter.getPath(), resultExporter.getPath());
            Assertions.assertEquals(expectedExporter.getEndOfLine(), resultExporter.getEndOfLine());
        }
    }
}
