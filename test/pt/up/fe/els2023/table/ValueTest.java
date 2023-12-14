package pt.up.fe.els2023.table;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.els2023.model.table.Value;

class ValueTest {
    @Test
    public void testGetMostGeneralType() {
        var stringValue = Value.of("string");
        var longValue = Value.of(1L);
        var doubleValue = Value.of(1.0);
        var booleanValue = Value.of(true);

        Assertions.assertEquals(Value.Type.STRING, Value.Type.mostGeneralRep(stringValue.getType(), longValue.getType()));
        Assertions.assertEquals(Value.Type.STRING, Value.Type.mostGeneralRep(stringValue.getType(), doubleValue.getType()));
        Assertions.assertEquals(Value.Type.STRING, Value.Type.mostGeneralRep(stringValue.getType(), booleanValue.getType()));

        Assertions.assertEquals(Value.Type.DOUBLE, Value.Type.mostGeneralRep(doubleValue.getType(), longValue.getType()));
        Assertions.assertEquals(Value.Type.DOUBLE, Value.Type.mostGeneralRep(doubleValue.getType(), booleanValue.getType()));

        Assertions.assertEquals(Value.Type.LONG, Value.Type.mostGeneralRep(longValue.getType(), booleanValue.getType()));

        Assertions.assertEquals(Value.Type.BOOLEAN, Value.Type.mostGeneralRep(booleanValue.getType(), booleanValue.getType()));
    }
}