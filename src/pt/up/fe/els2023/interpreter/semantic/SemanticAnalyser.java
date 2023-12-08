package pt.up.fe.els2023.interpreter.semantic;

import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTableFiller;
import pt.up.fe.els2023.interpreter.syntactic.SyntacticAnalysisResult;

import java.util.ArrayList;
import java.util.Map;

public class SemanticAnalyser implements SemanticAnalysis {

    public SemanticAnalyser() {

    }
    @Override
    public SemanticAnalysisResult analyse(SyntacticAnalysisResult result, Map<String, String> config) {
        var infos = new ArrayList<Diagnostic>();
        var warnings = new ArrayList<Diagnostic>();
        var errors = new ArrayList<Diagnostic>();

        var tableFiller = new SymbolTableFiller(result.root(), result.racoonsConfigFilename());

        // TODO: create call fillSymbolTable() and check for errors
        return new SemanticAnalysisResult(null, infos, warnings, errors);
    }
}
