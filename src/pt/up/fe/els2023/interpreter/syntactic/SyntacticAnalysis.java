package pt.up.fe.els2023.interpreter.syntactic;

import java.util.Map;

public interface SyntacticAnalysis {
    SyntacticAnalysisResult analyse(String racoonsConfigFilepath, Map<String, String> config);
}
