package pt.up.fe.els2023.imports;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.sources.YamlSource;
import pt.up.fe.els2023.table.Value;
import pt.up.fe.els2023.table.schema.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaVisitorTest {

    @Test
    public void testSchemaNodeOrder() {
        var source = new YamlSource(
                "decision_tree",
                List.of("./test/pt/up/fe/els2023/files/yaml/decision_tree_*.yaml"));

        SchemaNode ccpAlphaColumn = new ColumnNode("CCP Alpha");
        SchemaNode classWeightColumn = new ColumnNode("Class weight");
        SchemaNode criterionColumn = new ColumnNode("Criterion");
        SchemaNode minSamplesSplitColumn = new NullNode();
        SchemaNode featureImportancesColumn = new ColumnNode("Feature importances");
        SchemaNode nodesColumn = new NullNode();
        SchemaNode paramsColumn = new ExceptNode(Set.of("ccp_alpha"));

        var tableSchema = new TableSchema("decision_tree")
                .source(source)
                .nft(
                        new PropertyNode("params", new ListNode(
                                new PropertyNode("ccp_alpha", ccpAlphaColumn),
                                new PropertyNode("class_weight", classWeightColumn),
                                new PropertyNode("criterion", criterionColumn),
                                new PropertyNode("min_samples_split", minSamplesSplitColumn)
                        )),
                        new PropertyNode("feature_importances_",
                                new EachNode(featureImportancesColumn)
                        ),
                        new PropertyNode("tree_", new PropertyNode("nodes",
                                new IndexNode(0, nodesColumn))
                        ),
                        new PropertyNode("params", paramsColumn)
                );

        NodeOrderVisitor visitor = new NodeOrderVisitor();
        List<SchemaNode> nodes = visitor.getNodeOrder(tableSchema.nft());

        Assertions.assertEquals(7, nodes.size());

        Assertions.assertSame(ccpAlphaColumn, nodes.get(0));
        Assertions.assertSame(classWeightColumn, nodes.get(1));
        Assertions.assertSame(criterionColumn, nodes.get(2));
        Assertions.assertSame(minSamplesSplitColumn, nodes.get(3));
        Assertions.assertSame(featureImportancesColumn, nodes.get(4));
        Assertions.assertSame(nodesColumn, nodes.get(5));
        Assertions.assertSame(paramsColumn, nodes.get(6));
    }

    @Test
    public void testEachPopulate() throws IOException {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/students.yaml"));

        var tableSchema = new TableSchema("students")
                .source(source)
                .nft(
                        new FileNode("File"),
                        new DirectoryNode("Directory"),
                        new PropertyNode("course", new ColumnNode("Course")),
                        new PropertyNode("students", new EachNode(new ListNode(
                                new PropertyNode("studID", new ColumnNode("Student ID")),
                                new PropertyNode("grades", new EachNode(new ColumnNode("Grade"))),
                                new PropertyNode("friends", new EachNode(new ColumnNode("Friend")))
                        )))
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);
        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/students.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(6, table.size());

        List<Value> friendsList = new java.util.ArrayList<>(List.of(Value.of(3), Value.of(2), Value.of(1)));
        friendsList.add(Value.ofNull());

        Assertions.assertEquals(Map.of(
                "File", Stream.of("students.yaml", "students.yaml", "students.yaml", "students.yaml").map(Value::of).collect(Collectors.toList()),
                "Directory", Stream.of("Documents", "Documents", "Documents", "Documents").map(Value::of).collect(Collectors.toList()),
                "Course", Stream.of(7, 7, 7, 7).map(Value::of).collect(Collectors.toList()),
                "Student ID", Stream.of(1, 1, 2, 2).map(Value::of).collect(Collectors.toList()),
                "Grade", Stream.of(1, 2, 3, 4).map(Value::of).collect(Collectors.toList()),
                "Friend", friendsList
        ), table);
    }

    @Test
    public void testFilePath() throws IOException {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/students.yaml"));

        var tableSchema = new TableSchema("students")
                .source(source)
                .nft(
                        new FileNode("File"),
                        new DirectoryNode("Directory"),
                        new PathNode("Path")
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        Path path = Path.of("/home/Documents/students.yaml");
        var table = visitor.populateFromSource(path, rootNode, tableSchema.nft());

        Assertions.assertEquals(3, table.size());
        Assertions.assertEquals(Map.of(
                "File", List.of(Value.of("students.yaml")),
                "Directory", List.of(Value.of("Documents")),
                "Path", List.of(Value.of(path.toString()))
        ), table);
    }

    @Test
    public void testAll() throws IOException {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new AllNode()
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(4, table.size());
        Assertions.assertEquals(Map.of(
                "grades", List.of(Value.of("[1,2]")),
                "friends", List.of(Value.of("[3,2]")),
                "studID", List.of(Value.of(1)),
                "info", List.of(Value.of("{\"name\":\"John\",\"age\":20}"))
        ), table);
    }

    @Test
    public void testAllFormat() throws IOException {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new AllNode("all %s end")
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(4, table.size());
        Assertions.assertEquals(Map.of(
                "all grades end", List.of(Value.of("[1,2]")),
                "all friends end", List.of(Value.of("[3,2]")),
                "all studID end", List.of(Value.of(1)),
                "all info end", List.of(Value.of("{\"name\":\"John\",\"age\":20}"))
        ), table);
    }

    @Test
    public void testAllContainer() throws IOException {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new AllContainerNode()
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(3, table.size());
        Assertions.assertEquals(Map.of(
                "grades", List.of(Value.of("[1,2]")),
                "friends", List.of(Value.of("[3,2]")),
                "info", List.of(Value.of("{\"name\":\"John\",\"age\":20}"))
        ), table);
    }

    @Test
    public void testAllContainerFormat() throws IOException {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new AllContainerNode("all container %s end")
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(3, table.size());
        Assertions.assertEquals(Map.of(
                "all container grades end", List.of(Value.of("[1,2]")),
                "all container friends end", List.of(Value.of("[3,2]")),
                "all container info end", List.of(Value.of("{\"name\":\"John\",\"age\":20}"))
        ), table);
    }

    @Test
    public void testAllValueFormat() throws IOException {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new AllValueNode("all value %s end")
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(1, table.size());
        Assertions.assertEquals(Map.of(
                "all value studID end", List.of(Value.of(1))
        ), table);
    }

    @Test
    public void testAllValue() throws IOException {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new AllValueNode()
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(1, table.size());
        Assertions.assertEquals(Map.of(
                "studID", List.of(Value.of(1))
        ), table);
    }

    @Test
    public void testExceptFormat() throws IOException {
        var source = new YamlSource(
                "all_test",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new ExceptNode(Set.of("grades", "info"), "except %s end")
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(2, table.size());
        Assertions.assertEquals(Map.of(
                "except friends end", List.of(Value.of("[3,2]")),
                "except studID end", List.of(Value.of(1))
        ), table);
    }

    @Test
    public void testExcept() throws IOException {
        var source = new YamlSource(
                "all_test",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new ExceptNode(Set.of("grades", "info"))
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(2, table.size());
        Assertions.assertEquals(Map.of(
                "friends", List.of(Value.of("[3,2]")),
                "studID", List.of(Value.of(1))
        ), table);
    }

    @Test
    public void testIndex() throws IOException {
        var source = new YamlSource(
                "all_test",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new PropertyNode("grades", new IndexNode(0, new ColumnNode("Grade"))),
                        new IndexNode(0, new ColumnNode("Grade")) // should be ignored, as the parent is not an array, and it is yamlj
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(1, table.size());
        Assertions.assertEquals(Map.of(
                "Grade", List.of(Value.of(1))
        ), table);
    }

    @Test
    public void testDuplicateName() throws IOException {
        var source = new YamlSource(
                "all_test",
                List.of("./test/pt/up/fe/els2023/files/yaml/all_test.yaml", "./test/pt/up/fe/els2023/files/yaml/all_test_2.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new AllValueNode(),
                        new PropertyNode("studID", new NullNode()),
                        new PropertyNode("friends", new IndexNode(0, new ColumnNode("studID")))
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/all_test.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(3, table.size());

        Assertions.assertEquals(Map.of(
                "studID", List.of(Value.of(1)),
                "studID_1", List.of(Value.of(1)),
                "studID_2", List.of(Value.of(3))
        ), table);
    }

    @Test
    public void testDoubleBoolNull() throws IOException {
        var source = new YamlSource(
                "all_test",
                List.of("./test/pt/up/fe/els2023/files/yaml/double_bool_null.yaml"));

        var tableSchema = new TableSchema("all_test")
                .source(source)
                .nft(
                        new PropertyNode("new", new ColumnNode("bool")),
                        new PropertyNode("old", new ColumnNode("false")),
                        new PropertyNode("null-val", new ColumnNode("null")),
                        new PropertyNode("float", new NullNode())
                );

        var fileReader = new FileReader(source.getFiles().get(0));
        var reader = new BufferedReader(fileReader);

        var rootNode = tableSchema.source().getResourceParser().parse(reader);

        PopulateVisitor visitor = new PopulateVisitor();
        var table = visitor.populateFromSource(Path.of("/home/Documents/double_bool_null.yaml"), rootNode, tableSchema.nft());

        Assertions.assertEquals(4, table.size());
        Assertions.assertEquals(Map.of(
                "bool", List.of(Value.of(true)),
                "false", List.of(Value.of(false)),
                "null", List.of(Value.ofNull()),
                "float", List.of(Value.of(1.2))
        ), table);
    }
}
