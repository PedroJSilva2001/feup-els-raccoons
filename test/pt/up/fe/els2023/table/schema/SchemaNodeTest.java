package pt.up.fe.els2023.table.schema;

import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.model.schema.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pt.up.fe.els2023.dsl.SchemaNodeFactory.*;

class SchemaNodeTest {
    @Test
    void testTree() {
        List<SchemaNode> nft = List.of(
                property("params",
                        property("ccp_alpha", "CCP Alpha"),
                        property("min_samples_split")
                ),
                property("feature_importances", each("Feature importances")),
                property("tree_", index(0)),
                property("params", except("ccp_alpha")),
                index(0, "index 0"),
                index(1, column("index 1")),
                index(2, column("index 2"), column("index 3")),
                each(),
                each(nullNode()),
                each(column("each 1"), column("each 2")),
                each(list(column("each 1"), column("each 3"))),
                except("except 1", "except 2"),
                all(),
                allContainer(),
                allValue(),
                allValue("all value %s"),
                allContainer("all container %s"),
                all("all %s"),
                exceptF("except %s", "except 1", "except 2")
        );

        List<SchemaNode> expected = List.of(
                new PropertyNode("params", new ListNode(
                        new PropertyNode("ccp_alpha", new ColumnNode("CCP Alpha")),
                        new PropertyNode("min_samples_split", new NullNode())
                )),
                new PropertyNode("feature_importances", new EachNode(new ColumnNode("Feature importances"))),
                new PropertyNode("tree_", new IndexNode(0, new NullNode())),
                new PropertyNode("params", new ExceptNode(Set.of("ccp_alpha"))),
                new IndexNode(0, new ColumnNode("index 0")),
                new IndexNode(1, new ColumnNode("index 1")),
                new IndexNode(2, new ListNode(
                        new ColumnNode("index 2"),
                        new ColumnNode("index 3")
                )),
                new EachNode(new NullNode()),
                new EachNode(new NullNode()),
                new EachNode(new ListNode(
                        new ColumnNode("each 1"),
                        new ColumnNode("each 2")
                )),
                new EachNode(new ListNode(
                        new ColumnNode("each 1"),
                        new ColumnNode("each 3")
                )),
                new ExceptNode(Set.of("except 1", "except 2")),
                new AllNode(),
                new AllContainerNode(),
                new AllValueNode(),
                new AllValueNode("all value %s"),
                new AllContainerNode("all container %s"),
                new AllNode("all %s"),
                new ExceptNode(Set.of("except 1", "except 2"), "except %s")
        );

        assertEquals(expected, nft);
    }
}