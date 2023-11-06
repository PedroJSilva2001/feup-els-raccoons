package pt.up.fe.els2023.exceptions;

public class ImproperTerminalOperationException extends Exception {
    public ImproperTerminalOperationException(String operationName) {
        super(String.format("Operation %s is a terminal but was used in the middle of a pipeline", operationName));
    }
}
