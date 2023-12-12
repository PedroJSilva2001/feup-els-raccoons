package pt.up.fe.els2023.interpreter.semantic;

import org.eclipse.emf.ecore.EObject;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.diagnostic.Reportable;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;

import java.util.List;

public record SemanticAnalysisResult(
        SymbolTable symbolTable,
        List<Diagnostic> infos,
        List<Diagnostic> warnings,
        List<Diagnostic> errors) implements Reportable {
}
