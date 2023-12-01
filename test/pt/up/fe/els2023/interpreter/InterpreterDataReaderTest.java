package pt.up.fe.els2023.interpreter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.export.CsvExporter;
import pt.up.fe.els2023.export.HtmlExporter;
import pt.up.fe.els2023.export.TsvExporter;
import pt.up.fe.els2023.interpreter.operations.*;
import pt.up.fe.els2023.sources.YamlSource;
import pt.up.fe.els2023.table.schema.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InterpreterDataReaderTest {

    @Test
    public void testReadConfig() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedTableSources = Map.of(
                "decision_tree", new YamlSource("decision_tree", List.of("files/yaml/decision_tree_*.yaml"))
        );

        var expectedTableSchemas = List.of(
                new TableSchema("decision_tree")
                        .source(expectedTableSources.get("decision_tree"))
                        .nft(
                                new FileNode("File"),
                                new DirectoryNode(),
                                new PropertyNode("params", new ListNode(
                                        new PropertyNode("ccp_alpha", new ColumnNode("CCP Alpha")),
                                        new PropertyNode("class_weight", new ColumnNode("Class weight")),
                                        new PropertyNode("criterion", new ColumnNode("Criterion")),
                                        new PropertyNode("min_samples_split", new NullNode())
                                )),
                                new PropertyNode("feature_importances",
                                        new EachNode(new ColumnNode("Feature importances"))
                                ),
                                new PropertyNode("nodes",
                                        new IndexNode(0, new ListNode(
                                                new PropertyNode("$node[d]", new ColumnNode("Node 0")),
                                                new IndexOfNode(1, "node", new ColumnNode("Node 1")),
                                                new PropertyNode("\\$no\\$de[2]", new ColumnNode("Node 2"))
                                        ))
                                )
                        ),
                new TableSchema("Table 2")
                        .source(null)
                        .nft(
                                new PropertyNode("params", new NullNode())
                        )
        );

        var expectedOperations = List.of(
                new ArgMaxOperation("decision_tree", "maxRow","min_samples_split"),

                new SelectOperation("decision_tree", "selectResult", List.of("File")),

                new CompositeOperationBuilder(
                    "decision_tree", List.of(
                    new ArgMinOperation(null,null,  "min_samples_split"),
                    new ConcatVerticalOperation(null, null, List.of("maxRow"))
                )).setResultVariableName("test").build(),

                new WhereOperation("decision_tree", "table1", "Criterion == gini"),

                new ExportOperation("table1", "Table 1",
                        new CsvExporter("Table 1", "/dir1/dir2", System.lineSeparator(), ","))
        );


        Assertions.assertEquals(expectedOperations.size(), config.operations().size());

        for (int i = 0; i < expectedOperations.size(); i++) {
            var expectedOperation = expectedOperations.get(i);
            var resultOperation = config.operations().get(i);
            Assertions.assertEquals(expectedOperation.getResultVariableName(), resultOperation.getResultVariableName());
            Assertions.assertEquals(expectedOperation.getClass(), resultOperation.getClass());
        }

        for (var expectedTableSource : expectedTableSources.entrySet()) {
            var resultTableSource = config.tableSources().get(expectedTableSource.getKey());
            Assertions.assertEquals(expectedTableSource.getValue().getName(), resultTableSource.getName());
            Assertions.assertEquals(expectedTableSource.getValue().getFiles(), resultTableSource.getFiles());
        }

        Assertions.assertEquals(expectedTableSchemas, config.tableSchemas());
    }

    @Test
    public void testReadConfigExcept() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_except.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedNft = List.of(
                new ExceptNode(Set.of("nodes", "feature_importances", "params")),
                new ExceptNode(Set.of("nodes")),
                new ExceptNode(Set.of("params"))
        );

        Assertions.assertEquals(expectedNft, config.tableSchemas().get(0).nft());
    }

    @Test
    public void testReadConfigDirectory() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_directory.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedNft = List.of(
                new DirectoryNode(),
                new DirectoryNode("Directory")
        );

        Assertions.assertEquals(expectedNft, config.tableSchemas().get(0).nft());
    }

    @Test
    public void testReadConfigFile() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_file.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedNft = List.of(
                new FileNode(),
                new FileNode("File")
        );

        Assertions.assertEquals(expectedNft, config.tableSchemas().get(0).nft());
    }

    @Test
    public void testReadConfigPath() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_path.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedNft = List.of(
                new PathNode(),
                new PathNode("Path")
        );

        Assertions.assertEquals(expectedNft, config.tableSchemas().get(0).nft());
    }

    @Test
    public void testReadConfigAll() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_all.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedNft = List.of(
                new AllNode(),
                new AllValueNode(),
                new AllContainerNode()
        );

        Assertions.assertEquals(expectedNft, config.tableSchemas().get(0).nft());
    }

    @Test
    public void testReadConfigAllFormat() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_all_format.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedNft = List.of(
                new AllNode("all %s end"),
                new AllValueNode("all value %s end"),
                new AllContainerNode("all container %s end")
        );

        Assertions.assertEquals(expectedNft, config.tableSchemas().get(0).nft());
    }

    @Test
    public void testReadConfigEach() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_each.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedNft = List.of(
                new EachNode(new NullNode()),
                new EachNode(new ColumnNode("Each")),
                new EachNode(new ListNode(
                        new PropertyNode("node", new ColumnNode("Node")),
                        new PropertyNode("edge", new ColumnNode("Edge"))
                ))
        );

        Assertions.assertEquals(expectedNft, config.tableSchemas().get(0).nft());
    }

    // TODO: RETEST
    @Test
    public void testReadConfigCsvExporter() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_csv.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedExporters = List.of(
                new CsvExporter("Table 1", "/dir1/dir2", System.lineSeparator(), ","),
                new CsvExporter("Table 2", "/dir1/dir2", "\r\n", ";"),
                new TsvExporter("Table 3", "/dir1/dir2", "\n")
        );
    }

    @Test
    public void testReadConfigHtmlExporter() throws IOException {
        var configLocation = "./test/pt/up/fe/els2023/files/config/config_html.yaml";
        var configReader = new ConfigReader(configLocation);
        var config = configReader.readConfig();

        var expectedExporters = List.of(
                new HtmlExporter("Table 1", "/dir1/dir2", System.lineSeparator(), "table1", """
                        table {
                           border-collapse: collapse;
                           width: 100%;
                        }
                        th, td {
                           text-align: left;
                           padding: 8px;
                        }
                        tr:nth-child(even){background-color: #f2f2f2}
                        th {
                           background-color: #4CAF50;
                           color: white;
                        }""", false),
                new HtmlExporter("Table 2", "/dir1/dir2", System.lineSeparator(), "Table", """
                        body {
                            background-color: red;
                        }
                        * {
                            font-family: 'Fira Code', 'serif';
                        }
                        """, true)
        );

    }
}
