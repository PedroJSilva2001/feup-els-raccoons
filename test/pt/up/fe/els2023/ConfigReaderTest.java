package pt.up.fe.els2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.export.CsvExporter;
import pt.up.fe.els2023.sources.YamlSource;

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
                "decision_tree", new YamlSource("decision_tree", List.of("files/yaml/decision_tree_*.yaml"))
        );

        var expectedTableSchemas = List.of(
                new TableSchema("decision_tree")
                        .source(expectedTableSources.get("decision_tree"))
                        .from(
                            new ChildNode("params", new ListNode(
                                    new ChildNode("ccp_alpha", new ColumnNode("CCP Alpha")),
                                    new ChildNode("class_weight", new ColumnNode("Class weight")),
                                    new ChildNode("criterion", new ColumnNode("Criterion")),
                                    new ChildNode("min_samples_split", new NullNode())
                            )),
                            new ChildNode("feature_importances",
                                    new EachNode(new ColumnNode("Feature importances"))
                            ),
                            new ChildNode("nodes",
                                    new IndexNode(0, new ListNode(
                                            new ChildNode("$node[d]", new ColumnNode("Node 0")),
                                            new IndexNode(1, "node", new ColumnNode("Node 1")),
                                            new ChildNode("\\$no\\$de[2]", new ColumnNode("Node 2"))
                                    ))
                            )
                        ),
                new TableSchema("Table 2")
                        .source(null)
                        .from(
                                new ChildNode("params", new NullNode())
                        )
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
            Assertions.assertEquals(expectedTableSchema.from(), resultTableSchema.from());
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
