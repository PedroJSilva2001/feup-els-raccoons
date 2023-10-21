package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;

import java.util.ArrayList;
import java.util.List;


public class TableOperationsTest {

    private ITable table;

    @BeforeEach
    public void setup() {
        table = new Table();
        table.addColumn("Col1");
        table.addColumn("Col2");

        table.addRow(List.of("", 1L, "hello"));
        table.addRow(List.of("", 2L, "bye"));
        table.addRow(List.of("", 3L, ""));

        var r1 = new ArrayList<>();
        r1.add(""); r1.add(4L); r1.add(null);
        table.addRow(r1);

        var r2 = new ArrayList<>();
        r2.add(""); r2.add(6L); r2.add(null);
        table.addRow(r2);
    }

    @Test
    public void testWhere() {
        var newTable = table.btc().where(
                (row) -> row.get("Col1").equals(2L)
        ).get();

        Assertions.assertEquals(1, newTable.getRows().size());

        Assertions.assertEquals(2L, Long.parseLong(newTable.getRows().get(0).get(1).value().toString()));
    }
}
