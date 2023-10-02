package pt.up.fe.els2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.util.SpecsIo;


public class ExampleTest {

    @Test
    public void exampleTest() {

        // Reads a resource and tests contents
        Assertions.assertEquals("Expected text", SpecsIo.getResource("pt/up/fe/els2023/resource.txt"));
    }
}