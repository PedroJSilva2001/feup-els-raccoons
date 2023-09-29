package pt.up.fe.els2023.table;

public record Value(Object value) {

    @Override
    public String toString() {
        if (this.value == null) {
            return "";
        }

        return this.value.toString();
    }
}
