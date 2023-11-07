package pt.up.fe.els2023.table.schema;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.sources.YamlSource;
import pt.up.fe.els2023.table.RacoonTable;
import pt.up.fe.els2023.table.Value;
import pt.up.fe.els2023.table.schema.TableSchema;

import java.util.List;

import static pt.up.fe.els2023.table.schema.AllNode.all;
import static pt.up.fe.els2023.table.schema.DirectoryNode.directory;
import static pt.up.fe.els2023.table.schema.EachNode.each;
import static pt.up.fe.els2023.table.schema.FileNode.file;
import static pt.up.fe.els2023.table.schema.PropertyNode.property;

public class TableSchemaTest {
    @Test
    public void testWithEach() {
        var source = new YamlSource(
                "students",
                List.of("./test/pt/up/fe/els2023/files/yaml/students*.yaml"));

        var tableSchema = new TableSchema("students")
                .source(source)
                .nft(
                        file("File"),
                        directory("Directory"),
                        property("course", "Course"),
                        property("students", each(
                                property("studID", "Student ID"),
                                property("grades", each("Grade")),
                                property("friends", each("Friend"))
                        ))
                );

        var table = tableSchema.collect();

        Assertions.assertEquals(7, table.getRowNumber());
        Assertions.assertEquals(6, table.getColumnNumber());

        RacoonTable expected = new RacoonTable("students");
        expected.addColumn("File");
        expected.addColumn("Directory");
        expected.addColumn("Course");
        expected.addColumn("Student ID");
        expected.addColumn("Grade");
        expected.addColumn("Friend");

        expected.addRow(List.of(
                Value.of("students.yaml"),
                Value.of("yaml"),
                Value.of(7),
                Value.of(1),
                Value.of(1),
                Value.of(3)
        ));
        expected.addRow(List.of(
                Value.of("students.yaml"),
                Value.of("yaml"),
                Value.of(7),
                Value.of(1),
                Value.of(2),
                Value.of(2)
        ));
        expected.addRow(List.of(
                Value.of("students.yaml"),
                Value.of("yaml"),
                Value.of(7),
                Value.of(2),
                Value.of(3),
                Value.of(1)
        ));
        expected.addRow(List.of(
                Value.of("students.yaml"),
                Value.of("yaml"),
                Value.of(7),
                Value.of(2),
                Value.of(4),
                Value.ofNull()
        ));
        expected.addRow(List.of(
                Value.of("students_2.yaml"),
                Value.of("yaml"),
                Value.of(3),
                Value.of(3),
                Value.of(1),
                Value.of(2)
        ));
        expected.addRow(List.of(
                Value.of("students_2.yaml"),
                Value.of("yaml"),
                Value.of(3),
                Value.of(4),
                Value.of(3),
                Value.of(1)
        ));
        expected.addRow(List.of(
                Value.of("students_2.yaml"),
                Value.of("yaml"),
                Value.of(3),
                Value.of(4),
                Value.of(4),
                Value.of(4)
        ));

        Assertions.assertEquals(expected, table);
    }

    @Test
    public void testMissingProperty() {
        var source = new YamlSource("missing_source",
                List.of("./test/pt/up/fe/els2023/files/yaml/missing_prop_1.yaml",
                        "./test/pt/up/fe/els2023/files/yaml/missing_prop_2.yaml"));

        var tableSchema = new TableSchema("missing_table")
                .source(source)
                .nft(
                        property("params",
                                property("this", "First"),
                                property("missing", "Second")
                        )
                );

        var table = tableSchema.collect();

        Assertions.assertEquals(1, table.getRowNumber());

        RacoonTable expected = new RacoonTable("missing_table");

        expected.addColumn("First");
        expected.addColumn("Second");

        expected.addRow(List.of(
                Value.of("was"),
                Value.of("in")
        ));

        Assertions.assertEquals(expected, table);
    }

    @Test
    public void testAddedNewColumns() {
        var source = new YamlSource("missing_prop", List.of(
                "./test/pt/up/fe/els2023/files/yaml/missing_prop_*.yaml"
        ));

        var tableSchema = new TableSchema("missing_table")
                .source(source)
                .nft(
                        file(),
                        property("params",
                                all()
                        ),
                        property("outside")
                );

        var table = tableSchema.collect();

        Assertions.assertEquals(2, table.getRowNumber());

        RacoonTable expected = new RacoonTable("missing_table");

        expected.addColumn("$file");
        expected.addColumn("test");
        expected.addColumn("prop");
        expected.addColumn("this");
        expected.addColumn("missing");
        expected.addColumn("outside");

        expected.addRow(List.of(
                Value.of("missing_prop_1.yaml"),
                Value.of(1),
                Value.of("stuff"),
                Value.ofNull(),
                Value.ofNull(),
                Value.of(true)
        ));
        expected.addRow(List.of(
                Value.of("missing_prop_2.yaml"),
                Value.of(3.2),
                Value.ofNull(),
                Value.of("was"),
                Value.of("in"),
                Value.of(false)
        ));

        Assertions.assertEquals(expected, table);
    }

    @Test
    public void testMissingOne() {
        var source = new YamlSource("missing_one", List.of(
                "./test/pt/up/fe/els2023/files/yaml/missing_prop_2.yaml",
                "./test/pt/up/fe/els2023/files/yaml/missing_prop_1.yaml"
        ));

        var tableSchema = new TableSchema("missing_table")
                .source(source)
                .nft(
                        property("outside"),
                        property("params", property("this", "airst"))
                );

        var table = tableSchema.collect();

        Assertions.assertEquals(2, table.getRowNumber());

        RacoonTable expected = new RacoonTable("missing_table");

        expected.addColumn("outside");
        expected.addColumn("airst");

        expected.addRow(List.of(
                Value.of(false),
                Value.of("was")
        ));
        expected.addRow(List.of(
                Value.of(true),
                Value.ofNull()
        ));

        Assertions.assertEquals(expected, table);
    }
}
