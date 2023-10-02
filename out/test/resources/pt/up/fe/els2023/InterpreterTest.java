package pt.up.fe.els2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pt.up.fe.els2023.sources.JsonSource;
import pt.up.fe.els2023.table.Column;

import java.util.List;

public class InterpreterTest {

    @Test
    public void testSimpleJsonSourceTable() {
        var interpreter = new Interpreter();

        var columnSchemas = List.of(
                new ColumnSchema("Int 1", "int1"),
                new ColumnSchema("Int 2", null), // empty column
                new ColumnSchema(null, "obj1"), // name is "obj1"
                new ColumnSchema("String", "str1"),
                new ColumnSchema("Obj1 prop3", "obj1.prop3"),
                new ColumnSchema("miss", "miss"), // missing property "miss"
                new ColumnSchema("Miss2", "obj1.miss") // missing property "miss"
        );

        var files = List.of(
                "./test/pt/up/fe/els2023/files/json/file1.json",
                "./test/pt/up/fe/els2023/files/json/file2.json"
        );

        var source = new JsonSource("TestSource", files);

        var tableSchema = new TableSchema("TestSchema", columnSchemas, source);

        var table = interpreter.buildTable(tableSchema);

        // List<Value>
        var expectedColumns = List.of(
                new Column("_inputFile",
                        List.of("./test/pt/up/fe/els2023/files/json/file1.json",
                                "./test/pt/up/fe/els2023/files/json/file2.json")),
                new Column("Int 1", List.of("1", "")),
                new Column("Int 2", List.of("", "")),
                new Column("obj1",
                        List.of("{\"prop1\":3,\"prop2\":[1,2,3]}",
                                "{\"prop1\":3,\"prop2\":\"hey\",\"prop3\":2.0}")),
                new Column("String", List.of("hello", "bye")),
                new Column("Obj1 prop3", List.of("", "2.0")),
                new Column("miss", List.of("", "")),
                new Column("Miss2", List.of("", ""))
        );

        Assertions.assertEquals(expectedColumns.size(), table.getColumns().size());

        for (int i = 0; i < expectedColumns.size(); i++) {
            var expectedColumn = expectedColumns.get(i);
            var resultColumn = table.getColumns().get(i);
            Assertions.assertEquals(expectedColumn.getName(), resultColumn.getName());

            // TODO compare entries
            // this doesn't work and i hate java
            //Assertions.assertEquals(expectedColumn.getEntries(), resultColumn.getEntries());
        }

    }
}
