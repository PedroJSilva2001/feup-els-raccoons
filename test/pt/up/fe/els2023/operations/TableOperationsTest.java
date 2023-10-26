package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.ArrayList;
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
        table.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of("")));
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

    }
}
