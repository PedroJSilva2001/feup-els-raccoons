package pt.up.fe.els2023.interpreter.diagnostic;

import java.util.List;

public interface Reportable {
    List<Diagnostic> infos();

    List<Diagnostic> warnings();

    List<Diagnostic> errors();

    default boolean hasErrors() {
        return !errors().isEmpty();
    }
}
