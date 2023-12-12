package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.model.operations.*;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;


public class TableOperationsTest {

    private Table table1;

    private Table table2;

    private Table table3;

    private Table table4;

    private Table table5;

    private Table table6;

    @BeforeEach
    public void setup() {
        table1 = new RacoonTable();
        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(1L), Value.of("hello")));
        table1.addRow(List.of(Value.of(2L), Value.of("bye")));
        table1.addRow(List.of(Value.of(3L), Value.of("")));
        table1.addRow(List.of(Value.of("not int"), Value.of(55)));
        table1.addRow(List.of(Value.of("not int"), Value.of(56)));
        table1.addRow(List.of(Value.of("not int"), Value.of(55)));
        table1.addRow(List.of(Value.of("not int"), Value.of(56)));
        table1.addRow(List.of(Value.of(242), Value.of("")));
        table1.addRow(List.of(Value.of(221.12), Value.of("")));
        table1.addRow(List.of(Value.of(false), Value.of("")));
        table1.addRow(List.of(Value.of(4L), Value.ofNull()));
        table1.addRow(List.of(Value.of(5L), Value.ofNull()));

        table2 = new RacoonTable();
        table2.addColumn("Col1");
        table2.addColumn("Col2");
        table2.addColumn("Col3");

        table2.addRow(List.of(Value.of(25L), Value.of("hello"), Value.of(new BigInteger("123"))));
        table2.addRow(List.of(Value.of(0L), Value.of("hello"), Value.of(1221L)));
        table2.addRow(List.of(Value.of(12), Value.ofNull(), Value.of("hey")));
        table2.addRow(List.of(Value.of(12), Value.ofNull(), Value.of(false)));
        table2.addRow(List.of(Value.of(332), Value.of(true), Value.of(true)));
        table2.addRow(List.of(Value.of(12), Value.of(false), Value.of(new BigInteger("12"))));


        table3 = new RacoonTable();
        table3.addColumn("Col1");
        table3.addColumn("Col2");
        table3.addRow(List.of(Value.of(1L), Value.of("yes")));
        table3.addRow(List.of(Value.of(2L), Value.of("no")));
        table3.addRow(List.of(Value.ofNull(), Value.of("maybe")));
        table3.addRow(List.of(Value.of(4L), Value.ofNull()));


        table4 = new RacoonTable();
        table4.addColumn("Col1");
        table4.addColumn("Col1_1");
        table4.addRow(List.of(Value.of(false), Value.of(true)));
        table4.addRow(List.of(Value.of(true), Value.of(false)));


        table5 = new RacoonTable();
        table5.addColumn("ColA");
        table5.addColumn("ColB");
        table5.addRow(List.of(Value.of("ada"), Value.of(false)));
        table5.addRow(List.of(Value.of(2L), Value.of(true)));


        table6 = new RacoonTable();
        table6.addColumn("Col1");
        table6.addColumn("Col1_1");
        table6.addRow(List.of(Value.of(false), Value.of(true)));
        table6.addRow(List.of(Value.of(true), Value.of(false)));
    }

    @Test
    public void testColumnSum() throws ColumnNotFoundException, ImproperTerminalOperationException {
        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");

        expectedTable.addRow(List.of(Value.of(393L)));

        var sumResult = new ColumnSum(List.of("Col1")).execute(table2);

        Assertions.assertEquals(expectedTable, sumResult.getTable());
    }

    @Test
    public void testColumnMean() throws ColumnNotFoundException, ImproperTerminalOperationException {
        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");

        expectedTable.addRow(List.of(Value.of((double) 393L/6)));

        var meanResult = new ColumnMean(List.of("Col1")).execute(table2);

        Assertions.assertEquals(expectedTable, meanResult.getTable());
    }


    @Test
    public void testLimit() throws ColumnNotFoundException, ImproperTerminalOperationException {
        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of(1L), Value.of("hello")));
        expectedTable.addRow(List.of(Value.of(2L), Value.of("bye")));

        var limitResult = new LimitOperation(2).execute(table1);

        Assertions.assertEquals(expectedTable, limitResult.getTable());
    }

    @Test
    public void testGroupBy() throws ColumnNotFoundException, ImproperTerminalOperationException {
        var table = new RacoonTable();
        table.addColumn("Path");
        table.addColumn("Size");
        table.addColumn("Type");

        table.addRow(List.of(Value.of("/home/user"), Value.of(1024L), Value.of("dir")));
        table.addRow(List.of(Value.of("/home/user"), Value.of(10L), Value.of("png")));
        table.addRow(List.of(Value.of("/home/user"), Value.of(11L), Value.of("jpg")));
        table.addRow(List.of(Value.of("/etc"), Value.of(10L), Value.of("png")));
        table.addRow(List.of(Value.of("/etc"), Value.of(11L), Value.of("jpg")));
        table.addRow(List.of(Value.of("/etc"), Value.of(12L), Value.of("jpg")));
        table.addRow(List.of(Value.of("/var"), Value.of(13L), Value.of("jpg")));
        table.addRow(List.of(Value.of("/var"), Value.of(14L), Value.of("jpg")));

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Path");
        expectedTable.addColumn("Size");
        expectedTable.addColumn("Type");

        expectedTable.addRow(List.of(Value.of("/home/user"), Value.of(1024L), Value.of("dir")));
        expectedTable.addRow(List.of(Value.of("/etc"), Value.of(10L), Value.of("png")));
        expectedTable.addRow(List.of(Value.of("/var"), Value.of(13L), Value.of("jpg")));

//        var groupByResult = new GroupByOperation("Path", null).execute(table);

//        Assertions.assertEquals(expectedTable, groupByResult.getTable());

        expectedTable = new RacoonTable();

        expectedTable.addColumn("Path");
        expectedTable.addColumn("Size");
        expectedTable.addColumn("Type");

        expectedTable.addRow(List.of(Value.of("/home/user"), Value.of(1024L), Value.of("dir")));
        expectedTable.addRow(List.of(Value.of("/etc"), Value.of(12L), Value.of("jpg")));
        expectedTable.addRow(List.of(Value.of("/var"), Value.of(14L), Value.of("jpg")));

//        groupByResult = new GroupByOperation("Path", new MaxOperation(List.of("Size"))).execute(table);

//        Assertions.assertEquals(expectedTable, groupByResult.getTable());
    }

    @Test
    public void testJoin() throws ColumnNotFoundException {
        var table1 = new RacoonTable();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(1L), Value.of("hello")));
        table1.addRow(List.of(Value.of(1L), Value.of("hello again")));
        table1.addRow(List.of(Value.of(2L), Value.of("bye")));
        table1.addRow(List.of(Value.of(3L), Value.of("")));
        table1.addRow(List.of(Value.of(5L), Value.of("")));

        var table2 = new RacoonTable();

        table2.addColumn("Col1");
        table2.addColumn("Col2");

        table2.addRow(List.of(Value.of(1L), Value.of("other")));
        table2.addRow(List.of(Value.of(2L), Value.of("other stuff")));
        table2.addRow(List.of(Value.of(2L), Value.of("other stuff again")));
        table2.addRow(List.of(Value.of(3L), Value.of("")));
        table2.addRow(List.of(Value.of(4L), Value.of("")));

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addColumn("Col2_1");

        expectedTable.addRow(List.of(Value.of(1L), Value.of("hello"), Value.of("other")));
        expectedTable.addRow(List.of(Value.of(1L), Value.of("hello again"), Value.of("other")));
        expectedTable.addRow(List.of(Value.of(2L), Value.of("bye"), Value.of("other stuff")));
        expectedTable.addRow(List.of(Value.of(2L), Value.of("bye"), Value.of("other stuff again")));
        expectedTable.addRow(List.of(Value.of(3L), Value.of(""), Value.of("")));

        var joinResult = new JoinOperation(table2, "Col1").execute(table1);
        Assertions.assertEquals(expectedTable, joinResult.getTable());
    }

    @Test
    public void testWhere() throws ColumnNotFoundException {
        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(2L), Value.of("bye")));

        var newTable = new WhereOperation(
                (row) -> row.getObject("Col1").equals(2L)
        ).execute(table1).getTable();

        Assertions.assertEquals(expectedTable, newTable);


        newTable = new WhereOperation(
                (row) -> row.getObject("Col1").equals(2L) ||
                (row.getObject("Col2") != null && row.getObject("Col2").equals("bye")))
                .execute(table1).getTable();


        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(2L), Value.of("bye")));

        Assertions.assertEquals(expectedTable, newTable);


        newTable = new WhereOperation(
                (row) -> row.getObject("Col1").equals("not int")
        ).execute(table1).getTable();


        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(56)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(56)));

        Assertions.assertEquals(expectedTable, newTable);


        newTable = new WhereOperation(
                (row) -> row.getObject("Col1").equals("not int") &&
                        row.getObject("Col2").equals(55L) // TODO remove integer overload ?
        ).execute(table1).getTable();


        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55)));

        Assertions.assertEquals(expectedTable, newTable);


        newTable = new WhereOperation(
                (row) -> row.get("Col2").isNull() || row.get("Col1").isBoolean() || row.get("Col1").isDouble()
        ).execute(table1).getTable();


        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(221.12), Value.of("")));
        expectedTable.addRow(List.of(Value.of(false), Value.of("")));
        expectedTable.addRow(List.of(Value.of(4L), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(5L), Value.ofNull()));

        Assertions.assertEquals(expectedTable, newTable);


        newTable = new WhereOperation(
                (row) -> row.get("Col3") == null
        ).execute(table1).getTable();

        Assertions.assertEquals(table1, newTable);
    }

    @Test
    public void testCount() throws ColumnNotFoundException {
        var countResult = new CountOperation(List.of("Col1")).execute(table1);
        Assertions.assertEquals(Value.of(12), countResult.getValue());


        countResult = new CountOperation(List.of("Col2")).execute(table1);
        Assertions.assertEquals(Value.of(10), countResult.getValue());


        Assertions.assertThrows(ColumnNotFoundException.class, () -> new CountOperation(List.of("Col3")).execute(table1));


        var counts = new HashMap<String, Value>();
        counts.put("Col1", Value.of(12L));
        counts.put("Col2", Value.of(10L));
        countResult = new CountOperation(List.of("Col1", "Col2")).execute(table1);
        Assertions.assertEquals(counts, countResult.getValueMap());
    }

    @Test
    public void testMax() throws ColumnNotFoundException {
        var countResult = new MaxOperation(List.of("Col1")).execute(table1);
        Assertions.assertEquals(Value.of(242.0), countResult.getValue());


        countResult = new MaxOperation(List.of("Col2")).execute(table1);
        Assertions.assertEquals(Value.of(56L), countResult.getValue());


        Assertions.assertThrows(ColumnNotFoundException.class, () -> new MaxOperation(List.of("Col3")).execute(table1));


        countResult = new MaxOperation(List.of("Col1")).execute(table2);
        Assertions.assertEquals(Value.of(332L), countResult.getValue());


        countResult = new MaxOperation(List.of("Col2")).execute(table2);
        Assertions.assertNull(countResult.getValue());


        countResult = new MaxOperation(List.of("Col3")).execute(table2);

        Assertions.assertEquals(Value.of(new BigInteger("1221")), countResult.getValue());

        var maxes = new HashMap<String, Value>();
        maxes.put("Col1", Value.of(242.0));
        maxes.put("Col2", Value.of(56L));

        countResult = new MaxOperation(List.of("Col1", "Col2")).execute(table1);
        Assertions.assertEquals(maxes, countResult.getValueMap());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new MaxOperation(List.of("Col1", "Col2", "Col3")).execute(table1));
    }

    @Test
    public void testMin() throws ColumnNotFoundException {
        var countResult = new MinOperation(List.of("Col1")).execute(table1);
        Assertions.assertEquals(Value.of(1.0), countResult.getValue());

        countResult = new MinOperation(List.of("Col2")).execute(table1);
        Assertions.assertEquals(Value.of(55L), countResult.getValue());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new MinOperation(List.of("Col3")).execute(table1));

        countResult = new MinOperation(List.of("Col1")).execute(table2);
        Assertions.assertEquals(Value.of(0L), countResult.getValue());

        countResult = new MinOperation(List.of("Col2")).execute(table2);
        Assertions.assertNull(countResult.getValue());

        countResult = new MinOperation(List.of("Col3")).execute(table2);
        Assertions.assertEquals(Value.of(new BigInteger("12")), countResult.getValue());

        var mins = new HashMap<String, Value>();
        mins.put("Col1", Value.of(1.0));
        mins.put("Col2", Value.of(55L));

        countResult = new MinOperation(List.of("Col1", "Col2")).execute(table1);
        Assertions.assertEquals(mins, countResult.getValueMap());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new MinOperation(List.of("Col1", "Col2", "Col3")).execute(table1));
    }

    @Test
    public void testSelect() throws ColumnNotFoundException {
        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addColumn("Col3");

        expectedTable.addRow(List.of(Value.of(25L), Value.of("hello"), Value.of(new BigInteger("123"))));
        expectedTable.addRow(List.of(Value.of(0L), Value.of("hello"), Value.of(1221L)));
        expectedTable.addRow(List.of(Value.of(12), Value.ofNull(), Value.of("hey")));
        expectedTable.addRow(List.of(Value.of(12), Value.ofNull(), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(332), Value.of(true), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(12), Value.of(false), Value.of(new BigInteger("12"))));


        var tableResult = new SelectOperation(List.of("Col1", "Col2", "Col3")).execute(table2);
        Assertions.assertEquals(expectedTable, tableResult.getTable());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new SelectOperation(List.of("Col1", "Col2", "Col3", "Col4")).execute(table2));


        expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col3");

        expectedTable.addRow(List.of(Value.of(25L), Value.of(new BigInteger("123"))));
        expectedTable.addRow(List.of(Value.of(0L), Value.of(1221L)));
        expectedTable.addRow(List.of(Value.of(12), Value.of("hey")));
        expectedTable.addRow(List.of(Value.of(12), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(332), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(12), Value.of(new BigInteger("12"))));

        tableResult = new SelectOperation(List.of("Col1", "Col3")).execute(table2);
        Assertions.assertEquals(expectedTable, tableResult.getTable());


        expectedTable = new RacoonTable();

        tableResult = new SelectOperation(List.of()).execute(table2);
        Assertions.assertEquals(expectedTable, tableResult.getTable());


        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col3");
        expectedTable.addColumn("Col1");

        expectedTable.addRow(List.of(Value.of(new BigInteger("123")), Value.of(25L)));
        expectedTable.addRow(List.of(Value.of(1221L), Value.of(0L)));
        expectedTable.addRow(List.of(Value.of("hey"), Value.of(12)));
        expectedTable.addRow(List.of(Value.of(false), Value.of(12)));
        expectedTable.addRow(List.of(Value.of(true), Value.of(332)));
        expectedTable.addRow(List.of(Value.of(new BigInteger("12")), Value.of(12)));

        tableResult = new SelectOperation(List.of("Col3", "Col1")).execute(table2);
        Assertions.assertEquals(expectedTable, tableResult.getTable());
    }

    @Test
    public void testReject() throws ColumnNotFoundException {
        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col3");

        expectedTable.addRow(List.of(Value.of(25L), Value.of(new BigInteger("123"))));
        expectedTable.addRow(List.of(Value.of(0L), Value.of(1221L)));
        expectedTable.addRow(List.of(Value.of(12), Value.of("hey")));
        expectedTable.addRow(List.of(Value.of(12), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(332), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(12), Value.of(new BigInteger("12"))));

        var tableResult = new RejectOperation(List.of("Col2")).execute(table2);
        Assertions.assertEquals(expectedTable, tableResult.getTable());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new RejectOperation(List.of("Col1", "Col2", "Col3", "Col25")).execute(table2));


        tableResult = new RejectOperation(List.of()).execute(table2);
        Assertions.assertEquals(table2, tableResult.getTable());
    }

    @Test
    public void testConcatVertical() throws ColumnNotFoundException {
        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addColumn("Col1_1");

        expectedTable.addRow(List.of(Value.of(1L), Value.of("yes"), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(2L), Value.of("no"), Value.ofNull()));
        expectedTable.addRow(List.of(Value.ofNull(), Value.of("maybe"), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(4L), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(false), Value.ofNull(), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(true), Value.ofNull(), Value.of(false)));

        var tableResult = new ConcatVerticalOperation(List.of(table4)).execute(table3);
        Assertions.assertEquals(expectedTable, tableResult.getTable());


        expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of(false), Value.of(true), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(true), Value.of(false), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(1L), Value.ofNull(), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(2L), Value.ofNull(), Value.of("no")));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(4L), Value.ofNull(), Value.ofNull()));


        tableResult = new ConcatVerticalOperation(List.of(table3)).execute(table4);
        Assertions.assertEquals(expectedTable, tableResult.getTable());


        expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("ColA");
        expectedTable.addColumn("ColB");

        expectedTable.addRow(List.of(Value.of(false), Value.of(true), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(true), Value.of(false), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of("ada"), Value.of(false)));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of(2L), Value.of(true)));

        tableResult = new ConcatVerticalOperation(List.of(table5)).execute(table6);
        Assertions.assertEquals(expectedTable, tableResult.getTable());


        expectedTable = new RacoonTable(false);

        expectedTable.addColumn("ColA");
        expectedTable.addColumn("ColB");
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col1_1");

        expectedTable.addRow(List.of(Value.of("ada"), Value.of(false), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(2L), Value.of(true), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of(false), Value.of(true)));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of(true), Value.of(false)));

        tableResult = new ConcatVerticalOperation(List.of(table6)).execute(table5);
        Assertions.assertEquals(expectedTable, tableResult.getTable());
    }

    @Test
    public void testConcatHorizontal() throws ColumnNotFoundException {
        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("Col1_1_1");

        expectedTable.addRow(List.of(Value.of(1L), Value.of("yes"), Value.of(false), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(2L), Value.of("no"), Value.of(true), Value.of(false)));
        expectedTable.addRow(List.of(Value.ofNull(), Value.of("maybe"), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(4L), Value.ofNull(), Value.ofNull(), Value.ofNull()));

        var tableResult = new ConcatHorizontalOperation(List.of(table4)).execute(table3);

        Assertions.assertEquals(expectedTable, tableResult.getTable());

        expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("Col1_2");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of(false), Value.of(true), Value.of(1L), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(true), Value.of(false), Value.of(2L), Value.of("no")));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.ofNull(), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of(4L), Value.ofNull()));

        tableResult = new ConcatHorizontalOperation(List.of(table3)).execute(table4);


        Assertions.assertEquals(expectedTable, tableResult.getTable());
    }

    @Test
    public void testSum() throws ColumnNotFoundException {
        var sumResult = new SumOperation(List.of("Col1")).execute(table1);
        Assertions.assertEquals(Value.of(478.12), sumResult.getValue());

        sumResult = new SumOperation(List.of("Col2")).execute(table1);
        Assertions.assertEquals(Value.of(222), sumResult.getValue());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new SumOperation(List.of("Col3")).execute(table1));

        sumResult = new SumOperation(List.of("Col1")).execute(table2);
        Assertions.assertEquals(Value.of(393L), sumResult.getValue());

        sumResult = new SumOperation(List.of("Col2")).execute(table2);
        Assertions.assertNull(sumResult.getValue());

        sumResult = new SumOperation(List.of("Col3")).execute(table2);
        Assertions.assertEquals(Value.of(new BigInteger("1356")), sumResult.getValue());


        var maxes = new HashMap<String, Value>();
        maxes.put("Col1", Value.of(478.12));
        maxes.put("Col2", Value.of(222));

        sumResult = new SumOperation(List.of("Col1", "Col2")).execute(table1);
        Assertions.assertEquals(maxes, sumResult.getValueMap());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new SumOperation(List.of("Col1", "Col2", "Col3")).execute(table1));
    }

    @Test
    public void testMean() throws ColumnNotFoundException {
        var meanResult = new MeanOperation(List.of("Col1")).execute(table1);
        Assertions.assertEquals(Value.of(68.30285714285715), meanResult.getValue());

        meanResult = new MeanOperation(List.of("Col2")).execute(table1);
        Assertions.assertEquals(Value.of(55.5), meanResult.getValue());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new MeanOperation(List.of("Col3")).execute(table1));


        meanResult = new MeanOperation(List.of("Col1")).execute(table2);
        Assertions.assertEquals(Value.of((double) 393L / 6), meanResult.getValue());

        meanResult = new MeanOperation(List.of("Col2")).execute(table2);
        Assertions.assertNull(meanResult.getValue());

        meanResult = new MeanOperation(List.of("Col3")).execute(table2);
        Assertions.assertEquals(Value.of(new BigInteger("1356").divide(new BigInteger("3"))), meanResult.getValue());

        var means = new HashMap<String, Value>();
        means.put("Col1", Value.of(68.30285714285715));
        means.put("Col2", Value.of(55.5));
        meanResult = new MeanOperation(List.of("Col1", "Col2")).execute(table1);
        Assertions.assertEquals(means, meanResult.getValueMap());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new MeanOperation(List.of("Col1", "Col2", "Col3")).execute(table1));

    }

    @Test
    public void testVar() throws ColumnNotFoundException {
        var varResult = new VarOperation(List.of("Col1")).execute(table1);
        Assertions.assertEquals(Value.of(10693.727477551021),
                varResult.getValue());

        varResult = new VarOperation(List.of("Col2")).execute(table1);
        Assertions.assertEquals(Value.of(0.25), varResult.getValue());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new VarOperation(List.of("Col3")).execute(table1));

        varResult = new VarOperation(List.of("Col1")).execute(table2);
        Assertions.assertEquals(Value.of((double) 171079L / 12), varResult.getValue());

        varResult = new VarOperation(List.of("Col2")).execute(table2);
        Assertions.assertNull(varResult.getValue());

        varResult = new VarOperation(List.of("Col3")).execute(table2);
        Assertions.assertEquals(Value.of(297734.0), varResult.getValue());

        var vars = new HashMap<String, Value>();
        vars.put("Col1", Value.of(10693.727477551021));
        vars.put("Col2", Value.of(0.25));
        varResult = new VarOperation(List.of("Col1", "Col2")).execute(table1);
        Assertions.assertEquals(vars, varResult.getValueMap());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new VarOperation(List.of("Col1", "Col2", "Col3")).execute(table1));
    }

    @Test
    public void testStd() throws ColumnNotFoundException {
        var stdResult = new StdOperation(List.of("Col1")).execute(table1);
        Assertions.assertEquals(Value.of(103.41048050149956),
                stdResult.getValue());

        stdResult = new StdOperation(List.of("Col2")).execute(table1);
        Assertions.assertEquals(Value.of(0.5), stdResult.getValue());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new StdOperation(List.of("Col3")).execute(table1));

        stdResult = new StdOperation(List.of("Col1")).execute(table2);
        Assertions.assertEquals(Value.of((double) 171079L / 12).sqrt(), stdResult.getValue());

        stdResult = new VarOperation(List.of("Col2")).execute(table2);
        Assertions.assertNull(stdResult.getValue());

        stdResult = new StdOperation(List.of("Col3")).execute(table2);
        Assertions.assertEquals(Value.of(545.6500710162146), stdResult.getValue());

        var stds = new HashMap<String, Value>();
        stds.put("Col1", Value.of(103.41048050149956));
        stds.put("Col2", Value.of(0.5));
        stdResult = new StdOperation(List.of("Col1", "Col2")).execute(table1);
        Assertions.assertEquals(stds, stdResult.getValueMap());

        Assertions.assertThrows(ColumnNotFoundException.class, () -> new StdOperation(List.of("Col1", "Col2", "Col3")).execute(table1));
    }

    @Test
    public void testLongSort() throws ColumnNotFoundException {
        var table1 = new RacoonTable();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(1L), Value.of("yes")));
        table1.addRow(List.of(Value.of(4L), Value.of("no")));
        table1.addRow(List.of(Value.of(2L), Value.of("maybe")));
        table1.addRow(List.of(Value.of(3L), Value.of("yes")));
        table1.addRow(List.of(Value.of(5L), Value.of("no")));
        table1.addRow(List.of(Value.of(0L), Value.of("maybe")));

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of(0L), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(1L), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(2L), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(3L), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(4L), Value.of("no")));
        expectedTable.addRow(List.of(Value.of(5L), Value.of("no")));

        var tableResult = new SortOperation("Col1", true).execute(table1);
        Assertions.assertEquals(expectedTable, tableResult.getTable());


        var expectedTable2 = new RacoonTable();

        expectedTable2.addColumn("Col1");
        expectedTable2.addColumn("Col2");

        expectedTable2.addRow(List.of(Value.of(5L), Value.of("no")));
        expectedTable2.addRow(List.of(Value.of(4L), Value.of("no")));
        expectedTable2.addRow(List.of(Value.of(3L), Value.of("yes")));
        expectedTable2.addRow(List.of(Value.of(2L), Value.of("maybe")));
        expectedTable2.addRow(List.of(Value.of(1L), Value.of("yes")));
        expectedTable2.addRow(List.of(Value.of(0L), Value.of("maybe")));

        tableResult = new SortOperation("Col1", false).execute(table1);
        Assertions.assertEquals(expectedTable2, tableResult.getTable());

        var expectedTable3 = new RacoonTable();

        expectedTable3.addColumn("Col1");
        expectedTable3.addColumn("Col2");

        expectedTable3.addRow(List.of(Value.of(2L), Value.of("maybe")));
        expectedTable3.addRow(List.of(Value.of(0L), Value.of("maybe")));
        expectedTable3.addRow(List.of(Value.of(4L), Value.of("no")));
        expectedTable3.addRow(List.of(Value.of(5L), Value.of("no")));
        expectedTable3.addRow(List.of(Value.of(1L), Value.of("yes")));
        expectedTable3.addRow(List.of(Value.of(3L), Value.of("yes")));

        tableResult = new SortOperation("Col2", true).execute(table1);
        Assertions.assertEquals(expectedTable3, tableResult.getTable());
    }

    @Test
    public void testDoubleSort() throws ColumnNotFoundException {
        var table1 = new RacoonTable();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(1.1), Value.of("yes")));
        table1.addRow(List.of(Value.of(4.2), Value.of("no")));
        table1.addRow(List.of(Value.of(2.3), Value.of("maybe")));
        table1.addRow(List.of(Value.of(3.4), Value.of("yes")));
        table1.addRow(List.of(Value.of(3.3), Value.of("no")));
        table1.addRow(List.of(Value.of(0.6), Value.of("maybe")));

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of(0.6), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(1.1), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(2.3), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(3.3), Value.of("no")));
        expectedTable.addRow(List.of(Value.of(3.4), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(4.2), Value.of("no")));

        var tableResult = new SortOperation("Col1", true).execute(table1);
        Assertions.assertEquals(expectedTable, tableResult.getTable());
    }

    @Test
    public void testBooleanSort() throws ColumnNotFoundException {
        var table1 = new RacoonTable();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(true), Value.of("yes")));
        table1.addRow(List.of(Value.of(false), Value.of("no")));
        table1.addRow(List.of(Value.of(true), Value.of("maybe")));
        table1.addRow(List.of(Value.of(false), Value.of("yes")));
        table1.addRow(List.of(Value.of(false), Value.of("no")));
        table1.addRow(List.of(Value.of(true), Value.of("maybe")));

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of(false), Value.of("no")));
        expectedTable.addRow(List.of(Value.of(false), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(false), Value.of("no")));
        expectedTable.addRow(List.of(Value.of(true), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(true), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(true), Value.of("maybe")));

        var tableResult = new SortOperation("Col1", true).execute(table1);
        Assertions.assertEquals(expectedTable, tableResult.getTable());
    }

    @Test
    public void testDoubleLongSort() throws ColumnNotFoundException {
        var table1 = new RacoonTable();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(1.1), Value.of("yes")));
        table1.addRow(List.of(Value.of(4L), Value.of("no")));
        table1.addRow(List.of(Value.of(2.3), Value.of("maybe")));
        table1.addRow(List.of(Value.of(3L), Value.of("yes")));
        table1.addRow(List.of(Value.of(3.3), Value.of("no")));
        table1.addRow(List.of(Value.of(0L), Value.of("maybe")));

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of(0L), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(1.1), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(2.3), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(3L), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(3.3), Value.of("no")));
        expectedTable.addRow(List.of(Value.of(4L), Value.of("no")));

        var tableResult = new SortOperation("Col1", true).execute(table1);
        Assertions.assertEquals(expectedTable, tableResult.getTable());
    }

    @Test
    public void testBooleanDoubleSort() throws ColumnNotFoundException {
        var table1 = new RacoonTable();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(true), Value.of("yes")));
        table1.addRow(List.of(Value.of(false), Value.of("no")));
        table1.addRow(List.of(Value.of(true), Value.of("maybe")));
        table1.addRow(List.of(Value.of(false), Value.of("yes")));
        table1.addRow(List.of(Value.of(0.3), Value.of("no")));
        table1.addRow(List.of(Value.of(3L), Value.of("maybe")));

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of(false), Value.of("no")));
        expectedTable.addRow(List.of(Value.of(false), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(0.3), Value.of("no")));
        expectedTable.addRow(List.of(Value.of(true), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(true), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(3L), Value.of("maybe")));

        var tableResult = new SortOperation("Col1", true).execute(table1);
        Assertions.assertEquals(expectedTable, tableResult.getTable());
    }

    @Test
    public void testStringBooleanSort() throws ColumnNotFoundException {
        var table1 = new RacoonTable();

        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(true), Value.of("yes")));
        table1.addRow(List.of(Value.of(false), Value.of("no")));
        table1.addRow(List.of(Value.of(true), Value.of("maybe")));
        table1.addRow(List.of(Value.of("test"), Value.of("yes")));
        table1.addRow(List.of(Value.of("house"), Value.of("no")));
        table1.addRow(List.of(Value.of("fact"), Value.of("maybe")));

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of("fact"), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.of(false), Value.of("no")));
        expectedTable.addRow(List.of(Value.of("house"), Value.of("no")));
        expectedTable.addRow(List.of(Value.of("test"), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(true), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(true), Value.of("maybe")));

        var tableResult = new SortOperation("Col1", true).execute(table1);
        Assertions.assertEquals(expectedTable, tableResult.getTable());
    }

    @Test
    public void testCompositeOperation() throws ColumnNotFoundException, ImproperTerminalOperationException {
        var compositeOperation = new CompositeOperation();
        var result = compositeOperation.execute(table1);
        Assertions.assertEquals(table1, result.getTable());

        compositeOperation.addOperation(new CountOperation(List.of("Col1")));
        result = compositeOperation.execute(table1);
        Assertions.assertEquals(Value.of(12), result.getValue());


        Assertions.assertThrows(ImproperTerminalOperationException.class,
                () -> new CompositeOperation(List.of(
                        new CountOperation(List.of("Col1")),
                        new WhereOperation((row) -> row.get("Col1").equals(Value.of(2L)))
                )).execute(table1));

        Assertions.assertEquals(Value.of(1),
                new CompositeOperation(List.of(
                    new WhereOperation((row) -> row.get("Col1").equals(Value.of(2L))),
                    new CountOperation(List.of("Col1"))
                )).execute(table1).getValue());

        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55L)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55L)));

        Assertions.assertEquals(expectedTable,
                new CompositeOperation(List.of(
                        new WhereOperation((row) -> row.get("Col1").equals(Value.of("not int"))),
                        new WhereOperation((row) -> row.get("Col2").equals(Value.of(55L)))
                )).execute(table1).getTable());
    }
}
