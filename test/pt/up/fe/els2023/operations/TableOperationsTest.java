package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TableOperationsTest {

    private ITable table;

    @BeforeEach
    public void setup() {
        table = new Table();
        table.addColumn("Col1");
        table.addColumn("Col2");

        table.addRow(List.of(Value.of(""), Value.of(1L), Value.of("hello")));
        table.addRow(List.of(Value.of(""),  Value.of(2L),  Value.of("bye")));
        table.addRow(List.of(Value.of(""),  Value.of(3L), Value.of("")));
        table.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of(55)));
        table.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of(56)));
        table.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of(55)));
        table.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of(56)));
        table.addRow(List.of(Value.of(""),  Value.of(242), Value.of("")));
        table.addRow(List.of(Value.of(""),  Value.of(221.12), Value.of("")));
        table.addRow(List.of(Value.of(""),  Value.of(false), Value.of("")));
        table.addRow(Stream.of(Value.of(""), Value.of(4L), Value.ofNull()).collect(Collectors.toList()));
        table.addRow(Stream.of(Value.of(6L), Value.of(5L), Value.ofNull()).collect(Collectors.toList()));
    }

    @Test
    public void testWhere() {
        var newTable = table.btc().where(
                (row) -> row.getObject("Col1").equals(2L)
        ).get();

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of(2L), Value.of("bye")));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table.btc().where(
                (row) -> row.getObject("Col1").equals(2L) ||
                        (row.getObject("Col2") != null && row.getObject("Col2").equals("bye"))
        ).get();

        expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of(2L), Value.of("bye")));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table.btc().where(
                (row) -> row.getObject("Col1").equals("not int")
        ).get();

        expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of(""), Value.of("not int"), Value.of(56)));
        expectedTable.addRow(List.of(Value.of(""), Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of(""), Value.of("not int"), Value.of(56)));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table.btc().where(
                (row) -> row.getObject("Col1").equals("not int") &&
                        row.getObject("Col2").equals(55L) // TODO remove integer overload ?
        ).get();

        expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of(""), Value.of("not int"), Value.of(55)));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table.btc().where(
                (row) -> row.get("Col2").isNull() || row.get("Col1").isBoolean() || row.get("Col1").isDouble()
        ).get();

        expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""),  Value.of(221.12), Value.of("")));
        expectedTable.addRow(List.of(Value.of(""),  Value.of(false), Value.of("")));
        expectedTable.addRow(Stream.of(Value.of(""), Value.of(4L), Value.ofNull()).collect(Collectors.toList()));
        expectedTable.addRow(Stream.of(Value.of(6L), Value.of(5L), Value.ofNull()).collect(Collectors.toList()));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table.btc().where(
                (row) -> row.get("Col3") == null
        ).get();

        Assertions.assertEquals(table, newTable);
    }

    @Test
    public void testCount() throws ColumnNotFoundException {
        Assertions.assertEquals(12, table.btc().count("Col1"));
        Assertions.assertEquals(10, table.btc().count("Col2"));
        Assertions.assertThrows(ColumnNotFoundException.class, () -> table.btc().count("Col3"));
    }
}
