package pt.up.fe.els2023.table;

public record Value(Object value) implements Cloneable {

    @Override
    public String toString() {
        if (this.value == null) {
            return "";
        }

        return this.value.toString();
    }
}
