package pt.up.fe.els2023.utils.resources.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import pt.up.fe.els2023.utils.resources.ResourceParser;

import java.io.IOException;
import java.io.Reader;

public class JsonParser implements ResourceParser {
    public JsonNode parse(Reader reader) throws IOException {
        var objectMapper = new ObjectMapper();

        return new JsonNode(objectMapper.readTree(reader));
    }
}
