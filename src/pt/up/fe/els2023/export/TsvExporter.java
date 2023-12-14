package pt.up.fe.els2023.export;

import java.util.HashMap;
import java.util.Map;

public class TsvExporter extends CsvExporter {
    public TsvExporter(String filename, String path, String endOfLine) {
        super(filename, path, endOfLine, "\t");
    }

    public static Map<String, AttributeValue> getSupportedAttributes() {
        var attributes = new HashMap<String, AttributeValue>();

        attributes.put("filename", new AttributeValue(AttributeValue.Type.STRING, null, true));
        attributes.put("path", new AttributeValue(AttributeValue.Type.STRING, null, true));
        attributes.put("endOfLine", new AttributeValue(AttributeValue.Type.STRING, System.lineSeparator(), false));

        return attributes;
    }
}
