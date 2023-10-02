package pt.up.fe.els2023.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;

public class JsonParser {
    public static JsonNode parse(Reader reader) throws IOException {
        var objectMapper = new ObjectMapper();

        return new JsonNode(objectMapper.readTree(reader));
    }
}
