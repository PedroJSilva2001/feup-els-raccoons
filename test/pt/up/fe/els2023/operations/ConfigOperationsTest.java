package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.interpreter.TableCascadeInterpreter;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;


public class ConfigOperationsTest {

    private HashMap<String, ITable> tables;
    private HashMap<String, Value> resultVariables;
    private TableCascadeInterpreter btcInterpreter;

    @BeforeEach
    public void setup() {
        ITable table1 = new Table();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(1.0), Value.of("yes")));
        table1.addRow(List.of(Value.of(2.0), Value.of("no")));
        table1.addRow(List.of(Value.ofNull(), Value.of("maybe")));
        table1.addRow(List.of(Value.of(4.0), Value.ofNull()));

        ITable table2 = new Table();

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
        var selectPipeline = new CompositeOperationBuilder("table1", List.of(
                new SelectOperation(List.of("Col2"))
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(selectPipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of("yes")));
        expectedTable.addRow(List.of(Value.of("no")));
        expectedTable.addRow(List.of(Value.of("maybe")));
        expectedTable.addRow(List.of(Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

    }

    @Test
    public void testReject() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var rejectPipeline = new CompositeOperationBuilder("table1", List.of(
                new RejectOperation(List.of("Col2"))
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(rejectPipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");

        expectedTable.addRow(List.of(Value.of(1.0)));
        expectedTable.addRow(List.of(Value.of(2.0)));
        expectedTable.addRow(List.of(Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(4.0)));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

    }

    @Test
    public void testConcatHorizontal() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var concatPipeline = new CompositeOperationBuilder("table1", List.of(
                new ConcatHorizontalOperation(List.of("table2"))
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(concatPipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
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
        var concatPipeline = new CompositeOperationBuilder("table1", List.of(
                new ConcatVerticalOperation(List.of("table2"))
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(concatPipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
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
        var maxArgPipeline = new CompositeOperationBuilder("table1", List.of(
                new ArgMaxOperation("Col1")
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(maxArgPipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(4.0), Value.ofNull()));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testArgMin() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var minArgPipeline = new CompositeOperationBuilder("table1", List.of(
                new ArgMinOperation("Col1")
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(minArgPipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testRename() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var renamePipeline = new CompositeOperationBuilder("table1", List.of(
                new RenameOperation(List.of("Col1"), List.of("Col3"))
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(renamePipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
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
        var wherePipeline = new CompositeOperationBuilder("table1", List.of(
                new WhereOperation("Col2 == yes")
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(wherePipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        wherePipeline = new CompositeOperationBuilder("table1", List.of(
                new WhereOperation("Col1 < 2")
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(wherePipeline);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        var minPipeline = new CompositeOperationBuilder("table1", List.of(
                new MinOperation("Col1")
        )).setResultVariableName("min").build();

        btcInterpreter.execute(minPipeline);

        wherePipeline = new CompositeOperationBuilder("table1", List.of(
                new WhereOperation("Col1 <= $min")
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(wherePipeline);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testDropWhere() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var dropWherePipeline = new CompositeOperationBuilder("table1", List.of(
                new DropWhereOperation("Col2 != yes")
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(dropWherePipeline);

        Assertions.assertEquals(tables.size(), 3);

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(1.0), Value.of("yes")));

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        dropWherePipeline = new CompositeOperationBuilder("table1", List.of(
                new DropWhereOperation("Col1 >= 2 OR Col2 == maybe")
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(dropWherePipeline);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);

        var minPipeline = new CompositeOperationBuilder("table1", List.of(
                new MinOperation("Col1")
        )).setResultVariableName("min").build();

        btcInterpreter.execute(minPipeline);

        dropWherePipeline = new CompositeOperationBuilder("table1", List.of(
                new DropWhereOperation("Col1 > $min || Col2 == maybe")
        )).setResultVariableName("table3").build();

        btcInterpreter.execute(dropWherePipeline);

        Assertions.assertEquals(tables.size(), 3);

        Assertions.assertEquals(tables.get("table3"), expectedTable);
    }

    @Test
    public void testMax() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var maxPipeline = new CompositeOperationBuilder("table1", List.of(
                new MaxOperation("Col1")
        )).setResultVariableName("max").build();

        btcInterpreter.execute(maxPipeline);

        Assertions.assertEquals(resultVariables.get(maxPipeline.resultVariableName()), Value.of(4.0));
    }

    @Test
    public void testMin() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var minPipeline = new CompositeOperationBuilder("table1", List.of(
                new MinOperation("Col1")
        )).setResultVariableName("min").build();

        btcInterpreter.execute(minPipeline);

        Assertions.assertEquals(resultVariables.get(minPipeline.resultVariableName()), Value.of(1.0));
    }

    @Test
    public void testCount() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var countPipeline = new CompositeOperationBuilder("table1", List.of(
                new CountOperation("Col1")
        )).setResultVariableName("count").build();

        btcInterpreter.execute(countPipeline);

        Assertions.assertEquals(resultVariables.get(countPipeline.resultVariableName()), Value.of(3));
    }

    @Test
    public void testSum() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var sumPipeline = new CompositeOperationBuilder("table1", List.of(
                new SumOperation("Col1")
        )).setResultVariableName("sum").build();

        btcInterpreter.execute(sumPipeline);

        Assertions.assertEquals(resultVariables.get(sumPipeline.resultVariableName()), Value.of(7.0));
    }

    @Test
    public void testMean() throws TableNotFoundException, ColumnNotFoundException, IOException, ImproperTerminalOperationException {
        var meanPipeline = new CompositeOperationBuilder("table1", List.of(
                new MeanOperation("Col1")
        )).setResultVariableName("mean").build();

        btcInterpreter.execute(meanPipeline);

        Assertions.assertEquals(resultVariables.get(meanPipeline.resultVariableName()), Value.of(BigDecimal.valueOf(7).divide(BigDecimal.valueOf(3), new MathContext(1000))));
    }
}
