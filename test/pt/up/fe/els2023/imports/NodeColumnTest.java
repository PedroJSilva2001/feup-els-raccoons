package pt.up.fe.els2023.imports;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.table.schema.SchemaNode;

import java.util.ArrayList;
import java.util.List;

import static pt.up.fe.els2023.table.schema.AllNode.all;
import static pt.up.fe.els2023.table.schema.ColumnNode.column;
import static pt.up.fe.els2023.table.schema.NullNode.nullNode;

public class NodeColumnTest {
    @Test
    public void testAddNode() {
        SchemaNode node = all();
        SchemaNode node2 = nullNode();

        NodeColumnMap map = new NodeColumnMap();

        Assertions.assertEquals("test", map.add(node, "test"));
        Assertions.assertEquals("test2", map.add(node, "test2"));
        Assertions.assertEquals("test_1", map.add(node2, "test"));
        Assertions.assertEquals("test_1", map.add(node2, "test"));
    }

    @Test
    public void testGetOrderedColumns() {
        SchemaNode node = column("test");
        SchemaNode node2 = nullNode();
        SchemaNode node3 = all();
        SchemaNode node4 = column("test");

        NodeColumnMap map = new NodeColumnMap();
        List<SchemaNode> nodeOrder = new ArrayList<>();

        Assertions.assertEquals("test", map.add(node, "test"));
        Assertions.assertEquals(List.of(), map.getOrderedColumnNames(nodeOrder));

        nodeOrder.add(node);

        Assertions.assertEquals(List.of("test"), map.getOrderedColumnNames(nodeOrder));

        nodeOrder.add(node2);

        Assertions.assertEquals(List.of("test"), map.getOrderedColumnNames(nodeOrder));
        Assertions.assertEquals("test_1", map.add(node2, "test"));
        Assertions.assertEquals(List.of("test", "test_1"), map.getOrderedColumnNames(nodeOrder));

        nodeOrder.add(1, node3);

        Assertions.assertEquals("test_2", map.add(node3, "test"));
        Assertions.assertEquals("new_test", map.add(node3, "new_test"));
        Assertions.assertEquals("test_3", map.add(node3, "test_3"));
        Assertions.assertEquals("test_4", map.add(node3, "test_4"));

        Assertions.assertEquals(List.of(
                "test",
                "test_2",
                "new_test",
                "test_3",
                "test_4",
                "test_1"
        ), map.getOrderedColumnNames(nodeOrder));

        nodeOrder.add(0, node4);

        Assertions.assertEquals("final", map.add(node4, "final"));

        Assertions.assertEquals(List.of(
                "final",
                "test",
                "test_2",
                "new_test",
                "test_3",
                "test_4",
                "test_1"
        ), map.getOrderedColumnNames(nodeOrder));
    }
}
