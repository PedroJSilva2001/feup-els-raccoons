package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.ArrayList;
import java.util.List;


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

        var r1 = new ArrayList<Value>();
        r1.add(Value.of("")); r1.add(Value.of(4L)); r1.add(Value.ofNull());
        table.addRow(r1);

        var r2 = new ArrayList<Value>();
        r2.add(Value.of("")); r2.add(Value.of(6L)); r2.add(Value.ofNull());
        table.addRow(r2);
    }

    @Test
    public void testWhere() {
        var newTable = table.btc().where(
                (row) -> row.get("Col1").equals(2L)
        ).get();

        Assertions.assertEquals(1, newTable.getRows().size());

        Assertions.assertEquals(2L, Long.parseLong(newTable.getRows().get(0).get(1).getValue().toString()));
    }
}
