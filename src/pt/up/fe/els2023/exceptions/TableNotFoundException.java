package pt.up.fe.els2023.exceptions;

public class TableNotFoundException extends Exception {
    public TableNotFoundException(String tableName) {
        super(String.format("Table %s was not found", tableName));
    }
}
