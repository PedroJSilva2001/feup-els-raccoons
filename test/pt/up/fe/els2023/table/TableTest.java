package pt.up.fe.els2023.table;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class TableTest {
    @Test
    public void testEqualsWithSameReference() {
        var table1 = new Table();

        var table2 = table1;

        Assertions.assertEquals(table1, table2);
    }

    @Test
    public void testEqualsWithNull() {
        var table1 = new Table();

        Table table2 = null;

        Assertions.assertNotEquals(table1, table2);
    }


    @Test
    public void testEqualsWithSameDefaultTable() {
        var table1 = new Table();

        var table2 = new Table();

        Assertions.assertEquals(table1, table2);
    }

    @Test
    public void testEqualsWithSameEmptyColumns() {
        var table1 = new Table();
        table1.addColumn("Column1");
        table1.addColumn("Column2");
        table1.addColumn("Column3");

        var table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addColumn("Column3");

        Assertions.assertEquals(table1, table2);
    }

    @Test
    public void testEqualsWithDifferentEmptyColumns() {
        var table1 = new Table();
        table1.addColumn("Column1");
        table1.addColumn("Column2");

        var table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addColumn("Column3");

        Assertions.assertNotEquals(table1, table2);


        table1 = new Table();

        table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addColumn("Column3");

        Assertions.assertNotEquals(table1, table2);
    }

    @Test
    public void testEqualsWithSamePopulatedColumns() {
        var table1 = new Table();
        table1.addColumn("Column1");
        table1.addColumn("Column2");
        table1.addColumn("Column3");
        table1.addRow(List.of(Value.of("foo1"), Value.of("bar1"), Value.ofNull()));
        table1.addRow(List.of(Value.of("foo2"), Value.of("bar2"), Value.ofNull()));

        var table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addColumn("Column3");
        table2.addRow(List.of(Value.of("foo1"), Value.of("bar1"), Value.ofNull()));
        table2.addRow(List.of(Value.of("foo2"), Value.of("bar2"), Value.ofNull()));

        Assertions.assertEquals(table1, table2);
    }

    @Test
    public void testEqualsWithDifferentPopulatedColumns() {
        var table1 = new Table();
        table1.addColumn("Column1");
        table1.addColumn("Column2");
        table1.addRow(List.of(Value.of("foo1"), Value.of("bar1")));
        table1.addRow(List.of(Value.of("foo2"), Value.of("bar2")));

        var table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addRow(List.of(Value.of("differentfoo1"), Value.of("differentbar1")));
        table2.addRow(List.of(Value.of("foo2"), Value.of("bar2")));

        Assertions.assertNotEquals(table1, table2);


        table1 = new Table();
        table1.addColumn("Column1");
        table1.addColumn("Column2");
        table1.addRow(List.of(Value.of("foo1"), Value.of("bar1")));
        table1.addRow(List.of(Value.of("foo2"), Value.of("bar2")));

        table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addRow(List.of(Value.of("differentfoo1"), Value.of("differentbar1")));
        table2.addRow(List.of(Value.of("foo2"), Value.of("bar2")));

        Assertions.assertNotEquals(table1, table2);


        table1 = new Table();
        table1.addColumn("Column1");
        table1.addColumn("Column2");
        table1.addRow(List.of(Value.of("foo1"), Value.of("bar1")));
        table1.addRow(List.of(Value.of("foo2"), Value.of("bar2")));

        table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addRow(List.of(Value.of("differentfoo1"), Value.of("differentbar1")));

        Assertions.assertNotEquals(table1, table2);


        table1 = new Table();
        table1.addColumn("Column1");
        table1.addColumn("Column2");
        table1.addRow(List.of(Value.of("foo1"), Value.of("bar1")));

        table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addRow(List.of(Value.of(1212L), Value.of(false)));

        Assertions.assertNotEquals(table1, table2);


        table1 = new Table();
        table1.addColumn("Column2");
        table1.addColumn("Column1");
        table1.addRow(List.of(Value.of("foo1"), Value.of("bar1")));
        table1.addRow(List.of(Value.of("foo2"), Value.of("bar2")));

        table2 = new Table();
        table2.addColumn("Column1");
        table2.addColumn("Column2");
        table2.addRow(List.of(Value.of("foo1"), Value.of("bar1")));
        table2.addRow(List.of(Value.of("foo2"), Value.of("bar2")));
        Assertions.assertNotEquals(table1, table2);
    }

    @Test
    public void testAddColumnWithoutEntries() {
        var table = new Table();

        boolean columnWasAdded = table.addColumn("Column1");

        Assertions.assertEquals(1, table.getColumnNumber());
        Assertions.assertEquals(0, table.getRowNumber());
        Assertions.assertTrue(columnWasAdded);


        columnWasAdded = table.addColumn("Column2");

        Assertions.assertEquals(2, table.getColumnNumber());
        Assertions.assertEquals(0, table.getRowNumber());
        Assertions.assertTrue(columnWasAdded);


        columnWasAdded = table.addColumn("Column1");

        Assertions.assertEquals(2, table.getColumnNumber());
        Assertions.assertEquals(0, table.getRowNumber());
        Assertions.assertFalse(columnWasAdded);


        columnWasAdded = table.addColumn("Column2");

        Assertions.assertEquals(2, table.getColumnNumber());
        Assertions.assertEquals(0, table.getRowNumber());
        Assertions.assertFalse(columnWasAdded);


        columnWasAdded = table.addColumn("Co3");

        Assertions.assertEquals(3, table.getColumnNumber());
        Assertions.assertEquals(0, table.getRowNumber());
        Assertions.assertTrue(columnWasAdded);
    }

    @Test
    public void testAddColumnWithEntries() {
        var table = new Table();

        table.addColumn("Column1");
        table.addColumn("Column2");

        table.addRow(List.of(Value.of("foo1"), Value.of("bar1")));
        table.addRow(List.of(Value.of("foo2"), Value.of("bar2")));

        Assertions.assertEquals(2, table.getColumnNumber());
        Assertions.assertEquals(2, table.getRowNumber());

        table.addColumn("Column3");

        Assertions.assertEquals(3, table.getColumnNumber());
        Assertions.assertEquals(2, table.getRowNumber());

        var expectedTable = new Table();

        expectedTable.addColumn("Column1");
        expectedTable.addColumn("Column2");
        expectedTable.addColumn("Column3");
        expectedTable.addRow(List.of(Value.of("foo1"), Value.of("bar1"), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of("foo2"), Value.of("bar2"), Value.ofNull()));

        Assertions.assertEquals(expectedTable, table);
    }

    @Test
    public void testAddRow() {
        var table = new Table();

        table.addColumn("Column1");
        table.addColumn("Column2");

        boolean rowWasAdded = table.addRow(List.of(Value.of("foo1"), Value.of("bar1")));

        Assertions.assertEquals(2, table.getColumnNumber());
        Assertions.assertEquals(1, table.getRowNumber());
        Assertions.assertTrue(rowWasAdded);

        rowWasAdded = table.addRow(List.of(Value.of("foo2")));

        Assertions.assertEquals(2, table.getColumnNumber());
        Assertions.assertEquals(1, table.getRowNumber());
        Assertions.assertFalse(rowWasAdded);


        var expectedTable = new Table();

        expectedTable.addColumn("Column1");
        expectedTable.addColumn("Column2");
        expectedTable.addRow(List.of(Value.of("foo1"), Value.of("bar1")));

        Assertions.assertEquals(expectedTable, table);
    }

    @Test
    public void testGetColumn() {
        var table = new Table();

        table.addColumn("Column1");
        var col = table.getColumn(0);

        Assertions.assertNotNull(col);
        Assertions.assertEquals("Column1", col.getName());

        col = table.getColumn(1);

        Assertions.assertNull(col);

        col = table.getColumn(-1);

        Assertions.assertNull(col);
    }

    @Test
    public void testGetColumnByName() {
        var table = new Table();

        table.addColumn("Column1");

        var col = table.getColumn("Column1");

        Assertions.assertNotNull(col);

        col = table.getColumn("Column2");

        Assertions.assertNull(col);

        col = table.getColumn("Column3");

        Assertions.assertNull(col);
    }
}