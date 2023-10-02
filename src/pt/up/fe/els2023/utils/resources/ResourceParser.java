package pt.up.fe.els2023.utils.resources;

import java.io.IOException;
import java.io.Reader;

public interface ResourceParser {
    ResourceNode parse(Reader reader) throws IOException;
}
