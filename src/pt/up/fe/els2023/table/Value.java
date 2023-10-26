package pt.up.fe.els2023.table;

import java.math.BigDecimal;
import java.math.BigInteger;
public class Value {
    private final Object value;

    private Value(Object value) {
        this.value = value;
    }

    public static Value ofNull() {
        return new Value(null);
    }

    public static Value of(Boolean value) {
        return new Value(value);
    }

    public static Value of(Integer value) {
        return new Value(value.longValue());
    }

    public static Value of(Long value) {
        return new Value(value);
    }

    public static Value of(Double value) {
        return new Value(value);
    }

    public static Value of(String value) {
        return new Value(value);
    }

    public static Value of(BigInteger value) {
        return new Value(value);
    }

    public static Value of(BigDecimal value) {
        return new Value(value);
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean isBoolean() {
        return !isNull() && value instanceof Boolean;
    }

    public boolean isLong() {
        return !isNull() && value instanceof Long;
    }

    public boolean isDouble() {
        return !isNull() && value instanceof Double;
    }

    public boolean isString() {
        return !isNull() && value instanceof String;
    }
    public boolean isBigInteger() {
        return !isNull() && value instanceof BigInteger;
    }

    public boolean isBigDecimal() {
        return !isNull() && value instanceof BigDecimal;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (this.value == null) {
            return "";
        }

        return this.value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Value other)) {
            return false;
        }

        if (this.value == null) {
            return other.value == null;
        }

        return this.value.equals(other.value);
    }
}