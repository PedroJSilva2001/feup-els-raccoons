package pt.up.fe.els2023.interpreter.runtime;

import org.eclipse.emf.ecore.EObject;
import pt.up.fe.els2023.interpreter.semantic.SemanticAnalysisResult;
import pt.up.fe.els2023.interpreter.signatures.Signatures;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.interpreter.syntactic.SyntacticAnalysisResult;
import pt.up.fe.els2023.model.operations.OperationResult;
import pt.up.fe.els2023.model.operations.TableOperation;
import pt.up.fe.els2023.model.table.Value;
import pt.up.fe.els2023.racoons.*;
import pt.up.fe.els2023.racoons.impl.*;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InterpreterRuntime {
    private final List<TableOperation> tableOperations = new ArrayList<>();
    private SymbolTable symbolTable;

    public void run(SyntacticAnalysisResult syntaticResult, SemanticAnalysisResult semanticResult, Map<String, String> config) {
        var rootNode = syntaticResult.root();
        symbolTable = semanticResult.symbolTable();

        analyseExpressionsAndAssignments(rootNode, symbolTable);
    }

    private void analyseExpressionsAndAssignments(EObject root, SymbolTable symbolTable) {
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

    private Void analyseExpression(Expression _expression) {
        var expression = _expression.getExpression();

        analyseLogicalOr(expression);
        return null;
    }

    private OperationResult analyseLogicalOr(LogicalOr expression) {
        if (expression.getClass() == ParenthesisImpl.class) {
            var parenthesis = (Parenthesis) expression;

            return analyseLogicalOr(parenthesis.getElement().getExpression());
        } else if (expression.getClass() == TableCascadeImpl.class) {
            return analyseTableCascade((TableCascade) expression);
        } else if (expression.getClass() == OperationCallImpl.class) {
            return analyseOperationCall((OperationCall) expression);
        } // else if identifier, return value from symbol table

        throw new AssertionError("Unsupported expression " + expression.getClass().getName() + " in runtime phase");
    }

    private OperationResult analyseOperationCall(OperationCall operationCall) {
        var name = operationCall.getName();

        if (!Objects.equals(name, "table")) {
            throw new AssertionError("Expected table creation, got " + name);
        }
        var parameters = operationCall.getParameters();
        var firstParameter = parameters.get(0).getExpression();

        if (firstParameter.getClass() != IdentifierImpl.class) {
            throw new AssertionError("Expected identifier, got " + firstParameter.getClass().getName());
        }
        var identifier = (Identifier) firstParameter;
        var nftName = identifier.getId();

        var tableSchema = symbolTable.getTableSchema(nftName);

        if (tableSchema == null) {
            throw new AssertionError("Table schema " + nftName + " not found");
        }

        var table = tableSchema.value().collect();
        return OperationResult.ofTable(table);
    }

    private OperationResult analyseTableCascade(TableCascade tableCascade) {
        //var left = tableCascade.getLeft();
        //var leftResult = analyseLogicalOr(left);

        //if (leftResult.getType() != OperationResult.Type.TABLE) {
        //    throw new AssertionError("Expected table, got " + leftResult.getType());
        //}

        return null;
    }

    private Void analyseAssignment(Assignment assignment) {

        return null;
    }
}