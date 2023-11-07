package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.TableCascadeInterpreter;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.interpreter.operations.*;
import pt.up.fe.els2023.table.RacoonTable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;


public class InterpreterDataOperationsTest {

    private HashMap<String, Table> tables;
    private HashMap<String, Value> resultVariables;
    private TableCascadeInterpreter btcInterpreter;

    @BeforeEach
    public void setup() {
        Table table1 = new RacoonTable();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(1.0), Value.of("yes")));
        table1.addRow(List.of(Value.of(2.0), Value.of("no")));
        table1.addRow(List.of(Value.ofNull(), Value.of("maybe")));
        table1.addRow(List.of(Value.of(4.0), Value.ofNull()));

        Table table2 = new RacoonTable();

        table2.addColumn("Col1");
        table2.addColumn("Col1_1");

        table2.addRow(List.of(Value.of(false), Value.of(true)));
        table2.addRow(List.of(Value.of(true), Value.of(false)));


        tables = new HashMap<>();
        tables.put("table1", table1);
        tables.put("table2", table2);

        resultVariables = new HashMap<>();

        btcInterpreter = new TableCascadeInterpreter(new VariablesTable(tables, resultVariables));
    }

    @Test
    public void testSelect() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var select = new SelectOperation("table1", "table3", List.of("Col2"));

        btcInterpreter.execute(select);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of("yes")));
        expectedTable.addRow(List.of(Value.of("no")));
        expectedTable.addRow(List.of(Value.of("maybe")));
        expectedTable.addRow(List.of(Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

    }

    @Test
    public void testReject() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var reject = new RejectOperation("table1", "table3", List.of("Col2"));

        btcInterpreter.execute(reject);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");

        expectedTable.addRow(List.of(Value.of(1.0)));
        expectedTable.addRow(List.of(Value.of(2.0)));
        expectedTable.addRow(List.of(Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(4.0)));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

    }

    @Test
    public void testConcatHorizontal() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var concat = new ConcatHorizontalOperation("table1", "table3", List.of("table2"));

        btcInterpreter.execute(concat);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("Col1_1_1");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes"), Value.of(false), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(2.0), Value.of("no"), Value.of(true), Value.of(false)));
        expectedTable.addRow(List.of(Value.ofNull(), Value.of("maybe"), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(4.0), Value.ofNull(), Value.ofNull(), Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testConcatVertical() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var concat = new ConcatVerticalOperation("table1", "table3", List.of("table2"));

        btcInterpreter.execute(concat);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addColumn("Col1_1");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes"), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(2.0), Value.of("no"), Value.ofNull()));
        expectedTable.addRow(List.of(Value.ofNull(), Value.of("maybe"), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(4.0), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(false), Value.ofNull(), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(true), Value.ofNull(), Value.of(false)));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testArgMax() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var maxArg = new ArgMaxOperation("table1", "table3", "Col1");

        btcInterpreter.execute(maxArg);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(4.0), Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testArgMin() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var minArg = new ArgMinOperation("table1", "table3", "Col1");

        btcInterpreter.execute(minArg);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testRename() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {

        var rename = new RenameOperation("table1", "table3", List.of("Col1"), List.of("Col3"));

        btcInterpreter.execute(rename);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col3");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(2.0), Value.of("no")));
        expectedTable.addRow(List.of(Value.ofNull(), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(4.0), Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testWhere() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var where = new WhereOperation("table1", "table3", "Col2 == yes");
        btcInterpreter.execute(where);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        where = new WhereOperation("table1", "table3", "Col1 < 2");

        btcInterpreter.execute(where);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        var min = new MinOperation("table1", "min", "Col1");

        btcInterpreter.execute(min);

        where = new WhereOperation("table1", "table3", "Col1 <= $min");

        btcInterpreter.execute(where);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testDropWhere() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var dropWhere = new DropWhereOperation("table1", "table3", "Col2 != yes");

        btcInterpreter.execute(dropWhere);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        dropWhere = new DropWhereOperation("table1", "table3", "Col1 >= 2 OR Col2 == maybe");

        btcInterpreter.execute(dropWhere);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        var min = new MinOperation("table1", "min", "Col1");

        btcInterpreter.execute(min);

        dropWhere = new DropWhereOperation("table1", "table3", "Col1 > $min || Col2 == maybe");

        btcInterpreter.execute(dropWhere);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testMax() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var max = new MaxOperation("table1", "max", "Col1");

        btcInterpreter.execute(max);

        Assertions.assertEquals(resultVariables.get(max.getResultVariableName()), Value.of(4.0));
    }

    @Test
    public void testMin() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var min = new MinOperation("table1", "min", "Col1");

        btcInterpreter.execute(min);

        Assertions.assertEquals(resultVariables.get(min.getResultVariableName()), Value.of(1.0));
    }

    @Test
    public void testCount() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var count = new CountOperation("table1", "count", "Col1");

        btcInterpreter.execute(count);

        Assertions.assertEquals(resultVariables.get(count.getResultVariableName()), Value.of(3));
    }

    @Test
    public void testSum() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var sum = new SumOperation("table1", "sum", "Col1");

        btcInterpreter.execute(sum);

        Assertions.assertEquals(resultVariables.get(sum.getResultVariableName()), Value.of(7.0));
    }

    @Test
    public void testMean() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var mean = new MeanOperation("table1", "mean", "Col1");

        btcInterpreter.execute(mean);

        Assertions.assertEquals(resultVariables.get(mean.getResultVariableName()), Value.of(BigDecimal.valueOf(7).divide(BigDecimal.valueOf(3), new MathContext(1000))));
    }
}
