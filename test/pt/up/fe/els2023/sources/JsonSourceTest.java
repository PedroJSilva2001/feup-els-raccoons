package pt.up.fe.els2023.sources;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.up.fe.els2023.ColumnSchema;
import pt.up.fe.els2023.TableSchema;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Table;
import pt.up.fe.els2023.utils.json.JsonNode;
import pt.up.fe.els2023.utils.json.JsonParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class JsonSourceTest {
    private JsonSource jsonSource;


    @BeforeEach
    public void setUp() {
        // Initialize a JsonSource with test data
        List<String> testFiles = Arrays.asList(
                "test1.json",
                "test2.json"
        );

        jsonSource = new JsonSource("TestJsonSource", testFiles);
    }

    @Test
    public void testGetPropertyValue() throws JsonProcessingException {
        String jsonData = "{ \"prop1\": 1, \"prop2\": {\"prop3\": \"val3\", \"prop4\": [1,2,3]}, \"prop5\": null }";

        JsonNode rootNode = null;

        try {
            rootNode = JsonParser.parse(new StringReader(jsonData));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Test getting a valid property
        String result = jsonSource.getPropertyValue(rootNode, "prop1");
        Assertions.assertEquals("1", result);

        // Test getting a non-existent property
        result = jsonSource.getPropertyValue(rootNode, "nonexistent");
        Assertions.assertNull(result);

        // Test getting an object value
        result = jsonSource.getPropertyValue(rootNode, "prop2");
        Assertions.assertEquals("{\"prop3\":\"val3\",\"prop4\":[1,2,3]}", result);

        // Test getting a null value
        result = jsonSource.getPropertyValue(rootNode, "prop5");
        Assertions.assertEquals("", result);

        // Test getting a valid property with nesting
        result = jsonSource.getPropertyValue(rootNode, "prop2.prop3");
        Assertions.assertEquals("val3", result);
    }

    @Test
    public void testGetPropertyValueArrayUnsupported() {
        String jsonData1 = "{\"prop1\": [1,2,3]}";

        JsonNode rootNode1 = null;

        String jsonData2 = "[1,2,3]";

        JsonNode rootNode2 = null;

        try {
            rootNode1 = JsonParser.parse(new StringReader(jsonData1));
            rootNode2 = JsonParser.parse(new StringReader(jsonData2));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Test getting a property where an array is encountered (unsupported)
        String result = jsonSource.getPropertyValue(rootNode1, "prop1.item1");
        Assertions.assertNull(result);

        result = jsonSource.getPropertyValue(rootNode2, "item1");
        Assertions.assertNull(result);
    }

    @Test
    public void testPopulateTableFrom() {
        TableSchema schema = new TableSchema("TestTableSchema",
                List.of(new ColumnSchema("col1", "prop1"), new ColumnSchema("col2", "prop2")),
                jsonSource);

        ITable table = new Table("TestTable", jsonSource);

        jsonSource.populateTableFrom(schema, table);

        // Verify that the table is populated correctly
        var rows = table.getRows();
        Assertions.assertEquals(2, rows.size());
        Assertions.assertEquals(Arrays.asList("value1", "value2"), rows.get(0));
        Assertions.assertEquals(Arrays.asList("value3", "value4"), rows.get(1));
    }
}