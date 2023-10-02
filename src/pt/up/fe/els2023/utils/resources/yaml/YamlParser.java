package pt.up.fe.els2023.utils.resources.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pt.up.fe.els2023.utils.resources.ResourceParser;

import java.io.IOException;
import java.io.Reader;

public class YamlParser implements ResourceParser {
    public YamlNode parse(Reader reader) throws IOException {
        var objectMapper = new ObjectMapper(new YAMLFactory());

        return new YamlNode(objectMapper.readTree(reader));
    }
}
