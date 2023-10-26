package pt.up.fe.els2023.table;

public record Value(Object value) {

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
