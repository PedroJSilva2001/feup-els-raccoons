package pt.up.fe.els2023.exceptions;

public class TableCascadeAlreadyConsumedException extends Exception {
    public TableCascadeAlreadyConsumedException(String tableName) {
        super(String.format("Table cascade for table %s has already been consumed", tableName));
    }
}
