package pt.up.fe.els2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.imports.NodeOrderVisitor;
import pt.up.fe.els2023.imports.PopulateVisitor;
import pt.up.fe.els2023.sources.YamlSource;
import pt.up.fe.els2023.table.Value;
import pt.up.fe.els2023.utils.IdentityWrapper;

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
        List<IdentityWrapper<SchemaNode>> nodes = visitor.getNodeOrder(tableSchema.nft());

        Assertions.assertEquals(7, nodes.size());

        Assertions.assertSame(ccpAlphaColumn, nodes.get(0).value());
        Assertions.assertSame(classWeightColumn, nodes.get(1).value());
        Assertions.assertSame(criterionColumn, nodes.get(2).value());
        Assertions.assertSame(minSamplesSplitColumn, nodes.get(3).value());
        Assertions.assertSame(featureImportancesColumn, nodes.get(4).value());
        Assertions.assertSame(nodesColumn, nodes.get(5).value());
        Assertions.assertSame(paramsColumn, nodes.get(6).value());
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
}
