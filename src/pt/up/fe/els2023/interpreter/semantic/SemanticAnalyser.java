package pt.up.fe.els2023.interpreter.semantic;

import org.eclipse.emf.ecore.EObject;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.semantic.analysis.*;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTableFiller;
import pt.up.fe.els2023.interpreter.syntactic.SyntacticAnalysisResult;
import pt.up.fe.els2023.racoons.*;
import pt.up.fe.els2023.racoons.impl.*;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SemanticAnalyser implements SemanticAnalysis {

    private SymbolTable symbolTable;

    private List<Diagnostic> infos;

    private List<Diagnostic> warnings;

    private List<Diagnostic> errors;

    public SemanticAnalyser() {

    }

    @Override
    public SemanticAnalysisResult analyse(SyntacticAnalysisResult result, Map<String, String> config) {
        this.infos = new ArrayList<>();
        this.warnings = new ArrayList<Diagnostic>();
        this.errors = new ArrayList<Diagnostic>();

        fillSymbolTable(result.root(), result.racoonsConfigFilename());

        if (!errors.isEmpty()) {
            return new SemanticAnalysisResult(symbolTable, infos, warnings, errors);
        }

        analyseExpressionsAndAssignments(result.root());

        return new SemanticAnalysisResult(symbolTable, infos, warnings, errors);
    }

    private void fillSymbolTable(EObject root, String configFilename) {
        var tableFiller = new SymbolTableFiller();

        tableFiller.fill(root, configFilename);

        this.symbolTable = tableFiller.getSymbolTable();

        errors.addAll(tableFiller.getErrors());
    }

    private void analyseExpressionsAndAssignments(EObject root) {
        var statements = ((RacoonsImpl)root).getStatements();

        FunctionClassMap<EObject, Void> map = new FunctionClassMap<>();

        map.put(SourceDecl.class, a -> null);
        map.put(NftDecl.class, a -> null);
        map.put(ExporterDecl.class, a -> null);
        map.put(Expression.class, this::analyseExpression);
        map.put(Assignment.class, this::analyseAssignment);

        for (var statement : statements) {
            map.apply(statement);
        }
    }

    private Void analyseExpression(Expression expression) {
        var analysis = List.of(
                new VariableExistenceAnalysis(),
                new ExpressionResultNotUsedAnalysis(),
                new TableCascadeStartAnalysis());


        for (var analysisStep : analysis) {
            analysisStep.visit(expression, symbolTable);

            var newErrors = analysisStep.errors();

            if (!newErrors.isEmpty()) {
                errors.addAll(newErrors);
                return null;
            }
        }

        /*var variableUseAnalysis = new VariableExistenceAnalysis();
        variableUseAnalysis.visit(expression, symbolTable);

        var newErrors = variableUseAnalysis.errors();
        if (!newErrors.isEmpty()) {
            errors.addAll(newErrors);
            return null;
        }

        var tableCascadeStartAnalysis = new TableCascadeStartAnalysis();

        tableCascadeStartAnalysis.visit(expression, symbolTable);

        newErrors = tableCascadeStartAnalysis.errors();

        if (!newErrors.isEmpty()) {
            errors.addAll(newErrors);
            return null;
        }*/

        return null;
    }

    private Void analyseAssignment(Assignment assignment) {
        var analysis = List.of(
                new VariableExistenceAnalysis(),
                new TableCascadeStartAnalysis(),
                new AssignedVariableTypeInferAnalysis());

        for (var analysisStep : analysis) {
            analysisStep.visit(assignment, symbolTable);

            var newErrors = analysisStep.errors();

            if (!newErrors.isEmpty()) {
                errors.addAll(newErrors);
                return null;
            }
        }

        /*var variableUseAnalysis = new VariableExistenceAnalysis();

        variableUseAnalysis.visit(assignment, symbolTable);

        var newErrors = variableUseAnalysis.errors();
        if (!newErrors.isEmpty()) {
            errors.addAll(newErrors);
            return null;
        }

        var tableCascadeStartAnalysis = new TableCascadeStartAnalysis();

        tableCascadeStartAnalysis.visit(assignment, symbolTable);

        newErrors = tableCascadeStartAnalysis.errors();

        if (!newErrors.isEmpty()) {
            errors.addAll(newErrors);
            return null;
        }

        var assignedVariableAnalysis = new AssignedVariableTypeInferAnalysis();

        assignedVariableAnalysis.visit(assignment, symbolTable);*/

                //var newErrors = assignedVariableAnalysis.getErrors();

        //errors.addAll(newErrors);

        return null;
    }
}
