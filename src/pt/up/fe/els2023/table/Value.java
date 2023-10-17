package pt.up.fe.els2023.table;

public record Value(Object value) implements Cloneable {

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

    @Override
    public String toString() {
        if (this.value == null) {
            return "";
        }

        return this.value.toString();
    }
}
