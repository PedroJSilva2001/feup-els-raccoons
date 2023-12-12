package pt.up.fe.els2023.interpreter.semantic.analysis;

import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.utils.visitor.AVisitor;

import java.util.List;

public class VisitorSemanticAnalysis extends AVisitor<SymbolTable, Void> implements Analysis {
    private final List<Diagnostic> errors;

    protected VisitorSemanticAnalysis() {
        this.errors = new java.util.ArrayList<>();
    }

    @Override
    public List<Diagnostic> errors() {
        return errors;
    }

    public void addError(Diagnostic error) {
        errors.add(error);
    }

    @Override
    public List<Diagnostic> infos() {
        return List.of();
    }

    @Override
    public List<Diagnostic> warnings() {
        return List.of();
    }
}
