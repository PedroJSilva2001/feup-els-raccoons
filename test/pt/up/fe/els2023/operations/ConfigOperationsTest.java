package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.util.HashMap;
import java.util.List;


public class ConfigOperationsTest {

    private HashMap<String, ITable> tables;

    @BeforeEach
    public void setup() {
        ITable table1 = new Table();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(""), Value.of(1.0), Value.of("yes")));
        table1.addRow(List.of(Value.of(""), Value.of(2.0), Value.of("no")));
        table1.addRow(List.of(Value.of(""), Value.ofNull(), Value.of("maybe")));
        table1.addRow(List.of(Value.of(""), Value.of(4.0), Value.ofNull()));

        ITable table2 = new Table();

        table2.addColumn("Col1");
        table2.addColumn("Col1_1");

        table2.addRow(List.of(Value.of(""), Value.of(false), Value.of(true)));
        table2.addRow(List.of(Value.of(""), Value.of(true), Value.of(false)));


        tables = new HashMap<>();
        tables.put("table1", table1);
        tables.put("table2", table2);
    }

    @Test
    public void testSelect() {
        var selectPipeline = new Pipeline("table1", "table3", List.of(
                new SelectOperation(List.of("Col2"))
        ));

        var btcInterpreter = selectPipeline.updateBTC(tables, null);
        var resultingTable = btcInterpreter.getBtc().get();
        tables.put(selectPipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table(false);
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of("yes")));
        expectedTable.addRow(List.of(Value.of("no")));
        expectedTable.addRow(List.of(Value.of("maybe")));
        expectedTable.addRow(List.of(Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

    }

    @Test
    public void testReject() {
        var rejectPipeline = new Pipeline("table1", "table3", List.of(
                new RejectOperation(List.of("Col2"))
        ));

        var btcInterpreter = rejectPipeline.updateBTC(tables, null);
        var resultingTable = btcInterpreter.getBtc().get();
        tables.put(rejectPipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");

        expectedTable.addRow(List.of(Value.of(""), Value.of(1.0)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(2.0)));
        expectedTable.addRow(List.of(Value.of(""), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(""), Value.of(4.0)));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

    }

    @Test
    public void testConcatHorizontal() {
        var concatPipeline = new Pipeline("table1", "table3", List.of(
                new ConcatHorizontalOperation(List.of("table2"))
        ));

        var btcInterpreter = concatPipeline.updateBTC(tables, null);
        var resultingTable = btcInterpreter.getBtc().get();
        tables.put(concatPipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addColumn("File_1");
        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("Col1_1_1");
        expectedTable.addRow(List.of(Value.of(""), Value.of(1.0), Value.of("yes"), Value.of(""), Value.of(false), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(2.0), Value.of("no"), Value.of(""), Value.of(true), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(""), Value.ofNull(), Value.of("maybe"), Value.ofNull(), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(""), Value.of(4.0), Value.ofNull(), Value.ofNull(), Value.ofNull(), Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testArgMax() {
        var maxArgPipeline = new Pipeline("table1", "table3", List.of(
                new ArgMaxOperation("Col1")
        ));

        var btcInterpreter = maxArgPipeline.updateBTC(tables, null);
        var resultingTable = btcInterpreter.getBtc().get();
        tables.put(maxArgPipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of(4.0), Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testArgMin() {
        var minArgPipeline = new Pipeline("table1", "table3", List.of(
                new ArgMinOperation("Col1")
        ));

        var btcInterpreter = minArgPipeline.updateBTC(tables, null);
        var resultingTable = btcInterpreter.getBtc().get();
        tables.put(minArgPipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testWhere() {
        var wherePipeline = new Pipeline("table1", "table3", List.of(
                new WhereOperation("Col2 == yes")
        ));

        var btcInterpreter = wherePipeline.updateBTC(tables, null);
        var resultingTable = btcInterpreter.getBtc().get();
        tables.put(wherePipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        wherePipeline = new Pipeline("table1", "table3", List.of(
                new WhereOperation("Col1 < 2")
        ));

        btcInterpreter = wherePipeline.updateBTC(tables, null);
        resultingTable = btcInterpreter.getBtc().get();
        tables.put(wherePipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        var minPipeline = new Pipeline("table1", List.of(
                new MinOperation("Col1")
        ), "min");

        btcInterpreter = minPipeline.updateBTC(tables, null);
        var result = btcInterpreter.getValueResult();

        var map = new HashMap<String, Value>();
        map.put("min", result);

        wherePipeline = new Pipeline("table1", "table3", List.of(
                new WhereOperation("Col1 <= $min")
        ));

        btcInterpreter = wherePipeline.updateBTC(tables, map);
        resultingTable = btcInterpreter.getBtc().get();
        tables.put(wherePipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testDropWhere() {
        var dropWherePipeline = new Pipeline("table1", "table3", List.of(
                new DropWhereOperation("Col2 != yes")
        ));

        var btcInterpreter = dropWherePipeline.updateBTC(tables, null);
        var resultingTable = btcInterpreter.getBtc().get();
        tables.put(dropWherePipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        dropWherePipeline = new Pipeline("table1", "table3", List.of(
                new DropWhereOperation("Col1 >= 2 OR Col2 == maybe")
        ));

        btcInterpreter = dropWherePipeline.updateBTC(tables, null);
        resultingTable = btcInterpreter.getBtc().get();
        tables.put(dropWherePipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        var minPipeline = new Pipeline("table1", List.of(
                new MinOperation("Col1")
        ), "min");

        btcInterpreter = minPipeline.updateBTC(tables, null);
        var result = btcInterpreter.getValueResult();

        var map = new HashMap<String, Value>();
        map.put("min", result);

        dropWherePipeline = new Pipeline("table1", "table3", List.of(
                new DropWhereOperation("Col1 > $min || Col2 == maybe")
        ));

        btcInterpreter = dropWherePipeline.updateBTC(tables, map);
        resultingTable = btcInterpreter.getBtc().get();
        tables.put(dropWherePipeline.getResult(), resultingTable);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testMax() {
        var maxPipeline = new Pipeline("table1", List.of(
                new MaxOperation("Col1")
        ), "max");

        var btcInterpreter = maxPipeline.updateBTC(tables, null);
        var result = btcInterpreter.getValueResult();

        Assertions.assertEquals(result, Value.of(4.0));
    }

    @Test
    public void testMin() {
        var minPipeline = new Pipeline("table1", List.of(
                new MinOperation("Col1")
        ), "min");

        var btcInterpreter = minPipeline.updateBTC(tables, null);
        var result = btcInterpreter.getValueResult();

        Assertions.assertEquals(result, Value.of(1.0));
    }

    @Test
    public void testCount() {
        var countPipeline = new Pipeline("table1", List.of(
                new CountOperation("Col1")
        ), "count");

        var btcInterpreter = countPipeline.updateBTC(tables, null);
        var result = btcInterpreter.getValueResult();

        Assertions.assertEquals(result, Value.of(3));
    }
}
