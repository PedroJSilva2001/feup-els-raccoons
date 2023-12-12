package pt.up.fe.els2023.exceptions;

import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;

public class RacoonsRuntimeException extends RuntimeException{
    private final Diagnostic diagnostic;

    public RacoonsRuntimeException(Diagnostic diagnostic) {
        super(diagnostic.toString());
        this.diagnostic = diagnostic;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }
}
