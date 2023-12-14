package pt.up.fe.els2023.interpreter.semantic;

import pt.up.fe.els2023.interpreter.syntactic.SyntacticAnalysisResult;

import java.util.Map;

public interface SemanticAnalysis {
    SemanticAnalysisResult analyse(SyntacticAnalysisResult result, Map<String, String> config);
}
