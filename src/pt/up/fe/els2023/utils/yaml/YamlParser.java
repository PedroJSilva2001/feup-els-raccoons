package pt.up.fe.els2023.utils.yaml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

public class YamlParser {
    public static YamlNode parse(Reader reader) throws IOException {
        /*var objectMapper = new ObjectMapper(new YAMLFactory());
        var yamlParser = new YAMLFactory().createParser(reader);

        return new YamlNode(objectMapper.readValue(yamlParser, new TypeReference<>() {}));*/

        var objectMapper = new ObjectMapper(new YAMLFactory());

        return new YamlNode(objectMapper.readTree(reader));
    }
}
