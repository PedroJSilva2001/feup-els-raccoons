package pt.up.fe.els2023.interpreter.syntactic;

import org.eclipse.emf.ecore.EObject;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.diagnostic.Reportable;

import java.util.List;

public record SyntacticAnalysisResult (
        String racoonsConfigFilename,
        EObject root,
        List<Diagnostic> warnings,
        List<Diagnostic> errors) implements Reportable {

    public List<Diagnostic> infos() {
        return List.of();
    }
}
