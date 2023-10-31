package pt.up.fe.els2023.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.table.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TableOperationsTest {

    private ITable table1;

    private ITable table2;

    private ITable table3;

    private ITable table4;

    @BeforeEach
    public void setup() {
        table1 = new Table();
        table1.addColumn("Col1");
        table1.addColumn("Col2");

        table1.addRow(List.of(Value.of(""), Value.of(1L), Value.of("hello")));
        table1.addRow(List.of(Value.of(""),  Value.of(2L),  Value.of("bye")));
        table1.addRow(List.of(Value.of(""),  Value.of(3L), Value.of("")));
        table1.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of(55)));
        table1.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of(56)));
        table1.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of(55)));
        table1.addRow(List.of(Value.of(""),  Value.of("not int"), Value.of(56)));
        table1.addRow(List.of(Value.of(""),  Value.of(242), Value.of("")));
        table1.addRow(List.of(Value.of(""),  Value.of(221.12), Value.of("")));
        table1.addRow(List.of(Value.of(""),  Value.of(false), Value.of("")));
        table1.addRow(Stream.of(Value.of(""), Value.of(4L), Value.ofNull()).collect(Collectors.toList()));
        table1.addRow(Stream.of(Value.of(6L), Value.of(5L), Value.ofNull()).collect(Collectors.toList()));


        table2 = new Table();
        table2.addColumn("Col1");
        table2.addColumn("Col2");
        table2.addColumn("Col3");

        table2.addRow(List.of(Value.of(""), Value.of(25L), Value.of("hello"), Value.of(new BigInteger("123"))));
        table2.addRow(List.of(Value.of(""), Value.of(0L), Value.of("hello"), Value.of(1221L)));
        table2.addRow(List.of(Value.of(""), Value.of(12), Value.ofNull(), Value.of("hey")));
        table2.addRow(List.of(Value.of(""), Value.of(12), Value.ofNull(), Value.of(false)));
        table2.addRow(List.of(Value.of(""), Value.of(332), Value.of(true), Value.of(true)));
        table2.addRow(List.of(Value.of(""), Value.of(12), Value.of(false), Value.of(new BigInteger("12"))));


        table3 = new Table();

        table3.addColumn("Col1");
        table3.addColumn("Col2");

        table3.addRow(List.of(Value.of(""), Value.of(1L), Value.of("yes")));
        table3.addRow(List.of(Value.of(""), Value.of(2L), Value.of("no")));
        table3.addRow(List.of(Value.of(""), Value.ofNull(), Value.of("maybe")));
        table3.addRow(List.of(Value.of(""), Value.of(4L), Value.ofNull()));



        table4 = new Table();

        table4.addColumn("Col1");
        table4.addColumn("Col1_1");

        table4.addRow(List.of(Value.of(""), Value.of(false), Value.of(true)));
        table4.addRow(List.of(Value.of(""), Value.of(true), Value.of(false)));

    }

    @Test
    public void testWhere() {
        var newTable = table1.btc().where(
                (row) -> row.getObject("Col1").equals(2L)
        ).get();

        var expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of(2L), Value.of("bye")));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table1.btc().where(
                (row) -> row.getObject("Col1").equals(2L) ||
                        (row.getObject("Col2") != null && row.getObject("Col2").equals("bye"))
        ).get();

        expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of(2L), Value.of("bye")));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table1.btc().where(
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



        newTable = table1.btc().where(
                (row) -> row.getObject("Col1").equals("not int") &&
                        row.getObject("Col2").equals(55L) // TODO remove integer overload ?
        ).get();

        expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addRow(List.of(Value.of(""), Value.of("not int"), Value.of(55)));
        expectedTable.addRow(List.of(Value.of(""), Value.of("not int"), Value.of(55)));

        Assertions.assertEquals(expectedTable, newTable);



        newTable = table1.btc().where(
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
    }

    @Test
    public void testMax() throws ColumnNotFoundException {
        Assertions.assertEquals(Value.of(new BigDecimal("242")), table1.btc().max("Col1").get());
        Assertions.assertEquals(Value.of(56L), table1.btc().max("Col2").get());
        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().max("Col3"));

        Assertions.assertEquals(Value.of(332L), table2.btc().max("Col1").get());
        Assertions.assertTrue(table2.btc().max("Col2").isEmpty());
        Assertions.assertEquals(Value.of(new BigInteger("1221")), table2.btc().max("Col3").get());
    }

    @Test
    public void testMin() throws ColumnNotFoundException {
        Assertions.assertEquals(Value.of(new BigDecimal("1")), table1.btc().min("Col1").get());
        Assertions.assertEquals(Value.of(55L), table1.btc().min("Col2").get());
        Assertions.assertThrows(ColumnNotFoundException.class, () -> table1.btc().min("Col3"));

        Assertions.assertEquals(Value.of(0L), table2.btc().min("Col1").get());
        Assertions.assertTrue(table2.btc().min("Col2").isEmpty());
        Assertions.assertEquals(Value.of(new BigInteger("12")), table2.btc().min("Col3").get());
    }

    @Test
    public void testSelect() throws ColumnNotFoundException {
        var expectedTable = new Table();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");
        expectedTable.addColumn("Col3");

        expectedTable.addRow(List.of(Value.of(""), Value.of(25L), Value.of("hello"), Value.of(new BigInteger("123"))));
        expectedTable.addRow(List.of(Value.of(""), Value.of(0L), Value.of("hello"), Value.of(1221L)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(12), Value.ofNull(), Value.of("hey")));
        expectedTable.addRow(List.of(Value.of(""), Value.of(12), Value.ofNull(), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(332), Value.of(true), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(12), Value.of(false), Value.of(new BigInteger("12"))));


        Assertions.assertEquals(expectedTable, table2.btc().select("File", "Col1", "Col2", "Col3").get());


        Assertions.assertThrows(ColumnNotFoundException.class, () -> table2.btc().select("Col1", "Col2", "Col3", "Col4").get());

        expectedTable = new Table();
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col3");

        expectedTable.addRow(List.of(Value.of(25L), Value.of(new BigInteger("123"))));
        expectedTable.addRow(List.of(Value.of(0L), Value.of(1221L)));
        expectedTable.addRow(List.of(Value.of(12), Value.of("hey")));
        expectedTable.addRow(List.of(Value.of(12), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(332), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(12), Value.of(new BigInteger("12"))));

        Assertions.assertEquals(expectedTable, table2.btc().select("Col1", "Col3").get());


        expectedTable = new Table();

        Assertions.assertEquals(expectedTable, table2.btc().select().get());
    }

    @Test
    public void testReject() throws ColumnNotFoundException {
        var expectedTable = new Table();

        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col3");

        expectedTable.addRow(List.of(Value.of(""), Value.of(25L), Value.of(new BigInteger("123"))));
        expectedTable.addRow(List.of(Value.of(""), Value.of(0L), Value.of(1221L)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(12), Value.of("hey")));
        expectedTable.addRow(List.of(Value.of(""), Value.of(12), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(332), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(12), Value.of(new BigInteger("12"))));

        Assertions.assertEquals(expectedTable, table2.btc().reject("Col2").get());


        Assertions.assertThrows(ColumnNotFoundException.class, () -> table2.btc().select("Col1", "Col2", "Col3", "Col25").get());


        Assertions.assertEquals(table2, table2.btc().reject().get());
    }

    @Test
    public void testConcatHorizontal() {
        var expectedTable = new Table(false);

        expectedTable.addColumn("File");
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col2");

        expectedTable.addColumn("File_1");
        expectedTable.addColumn("Col1_1");
        expectedTable.addColumn("Col1_1_1");

        expectedTable.addRow(List.of(Value.of(""), Value.of(1L), Value.of("yes"), Value.of(""), Value.of(false), Value.of(true)));
        expectedTable.addRow(List.of(Value.of(""), Value.of(2L), Value.of("no"), Value.of(""), Value.of(true), Value.of(false)));
        expectedTable.addRow(List.of(Value.of(""), Value.ofNull(), Value.of("maybe"), Value.ofNull(), Value.ofNull(), Value.ofNull()));
        expectedTable.addRow(List.of(Value.of(""), Value.of(4L), Value.ofNull(), Value.ofNull(), Value.ofNull(), Value.ofNull()));

        Assertions.assertEquals(expectedTable, table3.btc().concatHorizontal(table4).get());



        expectedTable = new Table(false);
        expectedTable.addColumn("File");
        expectedTable.addColumn("Col1");
        expectedTable.addColumn("Col1_1");

        expectedTable.addColumn("File_1");
        expectedTable.addColumn("Col1_2");
        expectedTable.addColumn("Col2");


        expectedTable.addRow(List.of(Value.of(""), Value.of(false), Value.of(true), Value.of(""), Value.of(1L), Value.of("yes")));
        expectedTable.addRow(List.of(Value.of(""), Value.of(true), Value.of(false), Value.of(""), Value.of(2L), Value.of("no")));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.ofNull(), Value.of(""), Value.ofNull(), Value.of("maybe")));
        expectedTable.addRow(List.of(Value.ofNull(), Value.ofNull(), Value.ofNull(), Value.of(""), Value.of(4L), Value.ofNull()));

        Assertions.assertEquals(expectedTable, table4.btc().concatHorizontal(table3).get());


    }
}
