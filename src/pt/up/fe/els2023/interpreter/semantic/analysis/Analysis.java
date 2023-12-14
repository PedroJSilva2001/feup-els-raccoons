package pt.up.fe.els2023.interpreter.semantic.analysis;

import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;

import java.util.List;

public interface Analysis {
    List<Diagnostic> infos();
    List<Diagnostic> warnings();
    List<Diagnostic> errors();
}
