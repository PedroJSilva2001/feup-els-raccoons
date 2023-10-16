package pt.up.fe.els2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.config.*;
import pt.up.fe.els2023.sources.YamlSource;

import java.util.List;
import java.util.Set;

public class InterpreterTest {

    @Test
    public void testSimpleJsonSourceTable() {
        var interpreter = new Interpreter();

        var source = new YamlSource(
                "decision_tree",
                List.of("./test/pt/up/fe/els2023/files/yaml/decision_tree_*.yaml"));

        var tableSchema = new TableSchema("decision_tree")
                .source(source)
                .nft(
                        new ChildNode("params", new ListNode(
                                new ChildNode("ccp_alpha", new ColumnNode("CCP Alpha")),
                                new ChildNode("class_weight", new ColumnNode("Class weight")),
                                new ChildNode("criterion", new ColumnNode("Criterion")),
                                new ChildNode("min_samples_split", new NullNode())
                        )),
                        new ChildNode("feature_importances_",
                                new EachNode(new ColumnNode("Feature importances"))
                        ),
                        new ChildNode("tree_", new ChildNode("nodes",
                                new IndexNode(0, new NullNode()))
                        ),
                        new ChildNode("params", new ExceptNode(Set.of("ccp_alpha")))
                );

        var columnNames = interpreter.columnNames(tableSchema);

        Assertions.assertEquals(18, columnNames.size());
        Assertions.assertEquals("CCP Alpha", columnNames.get(0));
        Assertions.assertEquals("Class weight", columnNames.get(1));
        Assertions.assertEquals("Criterion", columnNames.get(2));
        Assertions.assertEquals("min_samples_split", columnNames.get(3));
        Assertions.assertEquals("Feature importances", columnNames.get(4));
        Assertions.assertEquals("nodes[0]", columnNames.get(5));
        Assertions.assertEquals("criterion", columnNames.get(6));
        Assertions.assertEquals("min_impurity_split", columnNames.get(7));
        Assertions.assertEquals("max_depth", columnNames.get(8));
        Assertions.assertEquals("min_samples_split", columnNames.get(9));
        Assertions.assertEquals("min_impurity_decrease", columnNames.get(10));
        Assertions.assertEquals("min_weight_fraction_leaf", columnNames.get(11));
        Assertions.assertEquals("random_state", columnNames.get(12));
        Assertions.assertEquals("splitter", columnNames.get(13));
        Assertions.assertEquals("min_samples_leaf", columnNames.get(14));
        Assertions.assertEquals("max_features", columnNames.get(15));
        Assertions.assertEquals("max_leaf_nodes", columnNames.get(16));
        Assertions.assertEquals("class_weight", columnNames.get(17));

//        Assertions

//        var table = interpreter.buildTable(tableSchema);
//
//        // List<Value>
//        var expectedColumns = List.of(
//                new Column("_inputFile",
//                        List.of("./test/pt/up/fe/els2023/files/json/file1.json",
//                                "./test/pt/up/fe/els2023/files/json/file2.json")),
//                new Column("Int 1", List.of("1", "")),
//                new Column("Int 2", List.of("", "")),
//                new Column("obj1",
//                        List.of("{\"prop1\":3,\"prop2\":[1,2,3]}",
//                                "{\"prop1\":3,\"prop2\":\"hey\",\"prop3\":2.0}")),
//                new Column("String", List.of("hello", "bye")),
//                new Column("Obj1 prop3", List.of("", "2.0")),
//                new Column("miss", List.of("", "")),
//                new Column("Miss2", List.of("", ""))
//        );
//
//        Assertions.assertEquals(expectedColumns.size(), table.getColumns().size());
//
//        for (int i = 0; i < expectedColumns.size(); i++) {
//            var expectedColumn = expectedColumns.get(i);
//            var resultColumn = table.getColumns().get(i);
//            Assertions.assertEquals(expectedColumn.getName(), resultColumn.getName());
//            Assertions.assertEquals(expectedColumn.getEntries().toString(), resultColumn.getEntries().toString());
//        }

    }
}
