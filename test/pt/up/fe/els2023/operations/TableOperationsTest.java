package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.RacoonTable;
import pt.up.fe.els2023.table.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


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
        table1.addRow(List.of( Value.of(2L),  Value.of("bye")));
        table1.addRow(List.of( Value.of(3L), Value.of("")));
        table1.addRow(List.of( Value.of("not int"), Value.of(55)));
        table1.addRow(List.of( Value.of("not int"), Value.of(56)));
        table1.addRow(List.of( Value.of("not int"), Value.of(55)));
        table1.addRow(List.of( Value.of("not int"), Value.of(56)));
        table1.addRow(List.of( Value.of(242), Value.of("")));
        table1.addRow(List.of( Value.of(221.12), Value.of("")));
        table1.addRow(List.of( Value.of(false), Value.of("")));
        table1.addRow(List.of( Value.of(4L), Value.ofNull()));
        table1.addRow(List.of( Value.of(5L), Value.ofNull()));

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
    public void testWhere() {
        var newTable = table1.btc().where(
                (row) -> row.getObject("Col1").equals(2L)
        ).get();

        var expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(2L), Value.of("bye")));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table1.btc().where(
                (row) -> row.getObject("Col1").equals(2L) ||
                        (row.getObject("Col2") != null && row.getObject("Col2").equals("bye"))
        ).get();

        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(2L), Value.of("bye")));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table1.btc().where(
                (row) -> row.getObject("Col1").equals("not int")
        ).get();

        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(56)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(56)));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table1.btc().where(
                (row) -> row.getObject("Col1").equals("not int") &&
                        row.getObject("Col2").equals(55L) // TODO remove integer overload ?
        ).get();

        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of("not int"), Value.of(55)));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table1.btc().where(
                (row) -> row.get("Col2").isNull() || row.get("Col1").isBoolean() || row.get("Col1").isDouble()
        ).get();

        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(221.12), Value.of("")));
        expectedTable.addRow(List.of(Value.of(false), Value.of("")));
        expectedTable.addRow(List.of(Value.of(4L), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(5L), Value.ofNull()));

        Assertions.assertEquals(expectedTable, newTable);


        newTable = table1.btc().where(
                (row) -> row.get("Col3") == null
        ).get();

        Assertions.assertEquals(table1, newTable);
    }

    @Test
    public void testCount() throws ColumnNotFoundException {
        Assertions.assertEquals(12, table1.btc().count("Col1"));
        Assertions.assertEquals(10, table1.btc().count("Col2"));
        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().count("Col3"));

        var counts = new HashMap<String, Long>();
        counts.put("Col1", 12L);
        counts.put("Col2", 10L);

        Assertions.assertEquals(counts, table1.btc().count("Col1", "Col2"));
    }

    @Test
    public void testMax() throws ColumnNotFoundException {
        Assertions.assertEquals(Value.of(new BigDecimal("242")), table1.btc().max("Col1").get());
        Assertions.assertEquals(Value.of(56L), table1.btc().max("Col2").get());
        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().max("Col3"));

        Assertions.assertEquals(Value.of(332L), table2.btc().max("Col1").get());
        Assertions.assertTrue(table2.btc().max("Col2").isEmpty());
        Assertions.assertEquals(Value.of(new BigInteger("1221")), table2.btc().max("Col3").get());


        var maxes = new HashMap<String, Optional<Value>>();
        maxes.put("Col1", Optional.of(Value.of(new BigDecimal("242"))));
        maxes.put("Col2", Optional.of(Value.of(56L)));

        Assertions.assertEquals(maxes, table1.btc().max("Col1", "Col2"));

        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().max("Col1", "Col2", "Col3"));
    }

    @Test
    public void testMin() throws ColumnNotFoundException {
        Assertions.assertEquals(Value.of(new BigDecimal("1")), table1.btc().min("Col1").get());
        Assertions.assertEquals(Value.of(55L), table1.btc().min("Col2").get());
        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().min("Col3"));

        Assertions.assertEquals(Value.of(0L), table2.btc().min("Col1").get());
        Assertions.assertTrue(table2.btc().min("Col2").isEmpty());
        Assertions.assertEquals(Value.of(new BigInteger("12")), table2.btc().min("Col3").get());

        var maxes = new HashMap<String, Optional<Value>>();
        maxes.put("Col1", Optional.of(Value.of(new BigDecimal("1"))));
        maxes.put("Col2", Optional.of(Value.of(55L)));

        Assertions.assertEquals(maxes, table1.btc().min("Col1", "Col2"));

        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().min("Col1", "Col2", "Col3"));
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


        Assertions.assertEquals(expectedTable, table2.btc().select("Col1", "Col2", "Col3").get());


        Assertions.assertThrows(ColumnNotFoundException.class, () -> table2.btc().select("Col1", "Col2", "Col3", "Col4").get());



        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col3");

        expectedTable.addRow(List.of(Value.of(25L), Value.of(new BigInteger("123"))));
        expectedTable.addRow(List.of(Value.of(0L), Value.of(1221L)));
        expectedTable.addRow(List.of(Value.of(12), Value.of("hey")));
        expectedTable.addRow(List.of(Value.of(12), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(332), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(12), Value.of(new BigInteger("12"))));

        Assertions.assertEquals(expectedTable, table2.btc().select("Col1", "Col3").get());



        expectedTable = new RacoonTable();

        Assertions.assertEquals(expectedTable, table2.btc().select().get());



        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col3");
        expectedTable.addColumn("Col1");

        expectedTable.addRow(List.of(Value.of(new BigInteger("123")), Value.of(25L)));
        expectedTable.addRow(List.of(Value.of(1221L), Value.of(0L)));
        expectedTable.addRow(List.of(Value.of("hey"), Value.of(12)));
        expectedTable.addRow(List.of(Value.of(false), Value.of(12)));
        expectedTable.addRow(List.of(Value.of(true), Value.of(332)));
        expectedTable.addRow(List.of(Value.of(new BigInteger("12")), Value.of(12)));

        Assertions.assertEquals(expectedTable, table2.btc().select("Col3", "Col1").get());
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

        Assertions.assertEquals(expectedTable, table2.btc().reject("Col2").get());


        Assertions.assertThrows(ColumnNotFoundException.class, () -> table2.btc().select("Col1", "Col2", "Col3", "Col25").get());


        Assertions.assertEquals(table2, table2.btc().reject().get());
    }

    @Test
    public void testConcatVertical() {
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

        Assertions.assertEquals(expectedTable, table3.btc().concatVertical(table4).get());


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



        expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("ColA");
        expectedTable.addColumn("ColB");


        expectedTable.addRow(List.of(Value.of(false), Value.of(true), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(true), Value.of(false), Value.ofNull(), Value.ofNull()));

        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of("ada"), Value.of(false)));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of(2L), Value.of(true)));

        Assertions.assertEquals(expectedTable, table6.btc().concatVertical(table5).get());



        expectedTable = new RacoonTable(false);

        expectedTable.addColumn("ColA");
        expectedTable.addColumn("ColB");
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col1_1");


        expectedTable.addRow(List.of(Value.of("ada"), Value.of(false), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(2L), Value.of(true), Value.ofNull(), Value.ofNull()));

        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of(false), Value.of(true)));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of(true), Value.of(false)));

        Assertions.assertEquals(expectedTable, table5.btc().concatVertical(table6).get());
    }

    @Test
    public void testConcatHorizontal() {
        var expectedTable = new RacoonTable();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("Col1_1_1");

        expectedTable.addRow(List.of(Value.of(1L), Value.of("yes"), Value.of(false), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(2L), Value.of("no"), Value.of(true), Value.of(false)));
        expectedTable.addRow(List.of(Value.ofNull(), Value.of("maybe"), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(4L), Value.ofNull(), Value.ofNull(), Value.ofNull()));

        Assertions.assertEquals(expectedTable, table3.btc().concatHorizontal(table4).get());



        expectedTable = new RacoonTable();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col1_1");

        expectedTable.addColumn("Col1_2");
        expectedTable.addColumn("Col2");


        expectedTable.addRow(List.of(Value.of(false), Value.of(true), Value.of(1L), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(true), Value.of(false), Value.of(2L), Value.of("no")));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.ofNull(), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.of(4L), Value.ofNull()));

        Assertions.assertEquals(expectedTable, table4.btc().concatHorizontal(table3).get());
    }

    @Test
    public void testSum() throws ColumnNotFoundException {
        Assertions.assertEquals(Value.of(new BigDecimal("478.12")), table1.btc().sum("Col1").get());
        Assertions.assertEquals(Value.of(222), table1.btc().sum("Col2").get());
        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().sum("Col3"));

        Assertions.assertEquals(Value.of(393L), table2.btc().sum("Col1").get());
        Assertions.assertTrue(table2.btc().sum("Col2").isEmpty());
        Assertions.assertEquals(Value.of(new BigInteger("1356")), table2.btc().sum("Col3").get());

        var maxes = new HashMap<String, Optional<Value>>();
        maxes.put("Col1", Optional.of(Value.of(new BigDecimal("478.12"))));
        maxes.put("Col2", Optional.of(Value.of(222)));

        Assertions.assertEquals(maxes, table1.btc().sum("Col1", "Col2"));

        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().sum("Col1", "Col2", "Col3"));
    }

    @Test
    public void testMean() throws ColumnNotFoundException {
        Assertions.assertEquals(Value.of(new BigDecimal("478.12").divide(new BigDecimal("7"), new MathContext(1000))),
                table1.btc().mean("Col1").get());
        Assertions.assertEquals(Value.of(222/4), table1.btc().mean("Col2").get());
        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().mean("Col3"));

        Assertions.assertEquals(Value.of(393L/6), table2.btc().mean("Col1").get());
        Assertions.assertTrue(table2.btc().mean("Col2").isEmpty());
        Assertions.assertEquals(Value.of(new BigInteger("1356").divide(new BigInteger("3"))), table2.btc().mean("Col3").get());

        var maxes = new HashMap<String, Optional<Value>>();
        maxes.put("Col1", Optional.of(Value.of(new BigDecimal("478.12").divide(new BigDecimal("7"), new MathContext(1000)))));
        maxes.put("Col2", Optional.of(Value.of(222/4)));

        Assertions.assertEquals(maxes, table1.btc().mean("Col1", "Col2"));

        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().mean("Col1", "Col2", "Col3"));
    }
}
