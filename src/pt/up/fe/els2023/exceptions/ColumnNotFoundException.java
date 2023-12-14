package pt.up.fe.els2023.exceptions;

public class ColumnNotFoundException extends Exception {
    public ColumnNotFoundException(String columnName) {
        super(String.format("Column %s was not found", columnName));
    }
}
