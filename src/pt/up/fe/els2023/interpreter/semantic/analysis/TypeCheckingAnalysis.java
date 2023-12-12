package pt.up.fe.els2023.interpreter.semantic.analysis;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.symboltable.Symbol;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.racoons.impl.*;

public class TypeCheckingAnalysis extends PreorderSemanticAnalysis {
    public TypeCheckingAnalysis() {
        super();

        addVisit(AssignmentImpl.class.getName(), this::visitAssignment);
        addVisit(LogicalOrImpl.class.getName(), this::visitBinaryOp);
        addVisit(LogicalAndImpl.class.getName(), this::visitBinaryOp);
        addVisit(EqualsAndNEqualsImpl.class.getName(), this::visitComparison);
        addVisit(ComparisonImpl.class.getName(), this::visitComparison);
        addVisit(AddAndSubImpl.class.getName(), this::visitMathOp);
        addVisit(MultAndDivImpl.class.getName(), this::visitMathOp);
        addVisit(UnaryPreOpImpl.class.getName(), this::visitUnaryPreOp);
        addVisit(TableCascadeImpl.class.getName(), this::visitTableCascade);
    }

    private boolean containsError = false;

    private Void visitAssignment(EObject node, SymbolTable symbolTable) {
        containsError = false;

        return null;
    }

    private Void visitBinaryOp(EObject node, SymbolTable symbolTable) {
        var leftType = getType(((LogicalOrImpl) node).getLeft(), symbolTable);
        var rightType = getType(((LogicalOrImpl) node).getRight(), symbolTable);

        if ((leftType != rightType || leftType != Symbol.Type.BOOLEAN) && !containsError) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types '" + leftType + "' and '" + rightType + "' in line " + NodeModelUtils.getNode(node).getStartLine()));
            containsError = true;
        }
        return null;
    }

    private Void visitComparison(EObject node, SymbolTable symbolTable) {
        var leftType = getType(((LogicalOrImpl) node).getLeft(), symbolTable);
        var rightType = getType(((LogicalOrImpl) node).getRight(), symbolTable);

        if ((leftType != rightType || (leftType != Symbol.Type.BOOLEAN && leftType != Symbol.Type.NUMBER && leftType != Symbol.Type.STRING)) && !containsError) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types '" + leftType + "' and '" + rightType + "' in line " + NodeModelUtils.getNode(node).getStartLine()));
            containsError = true;
        }
        return null;
    }

    private Void visitMathOp(EObject node, SymbolTable symbolTable) {
        var leftType = getType(((LogicalOrImpl) node).getLeft(), symbolTable);
        var rightType = getType(((LogicalOrImpl) node).getRight(), symbolTable);

        if ((leftType != rightType || leftType != Symbol.Type.NUMBER) && !containsError) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types '" + leftType + "' and '" + rightType + "' in line " + NodeModelUtils.getNode(node).getStartLine()));
            containsError = true;
        }
        return null;
    }

    private Void visitUnaryPreOp(EObject node, SymbolTable symbolTable) {
        var unaryPreOpType = getType(node, symbolTable);
        var subExpression = ((UnaryPreOpImpl) node).getSubExpression();

        var subExpressionType = getType(subExpression, symbolTable);

        if (subExpressionType != unaryPreOpType && !containsError) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types '" + subExpressionType + "' and '" + unaryPreOpType + "' in line " + NodeModelUtils.getNode(node).getStartLine()));
            containsError = true;
        }

        return null;
    }

    private Void visitTableCascade(EObject node, SymbolTable symbolTable) {
        var rightType = getType(((TableCascadeImpl) node).getRight(), symbolTable);

        if (rightType != Symbol.Type.TABLE && rightType != Symbol.Type.MAP && rightType != Symbol.Type.NUMBER && !containsError) {
            addError(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types in line " + NodeModelUtils.getNode(node).getStartLine()));
            containsError = true;
        }
        return null;
    }

    private Symbol.Type getType(EObject expression, SymbolTable symbolTable) {
        var expressionType = expression.getClass().getName();

        if (expressionType.equals(LogicalAndImpl.class.getName()) || expressionType.equals(LogicalOrImpl.class.getName())
                || expressionType.equals(EqualsAndNEqualsImpl.class.getName()) || expressionType.equals(ComparisonImpl.class.getName())) {
            return Symbol.Type.BOOLEAN;

        } else if (expressionType.equals(AddAndSubImpl.class.getName()) || expressionType.equals(MultAndDivImpl.class.getName())) {
            return Symbol.Type.NUMBER;

        } else if (expressionType.equals(UnaryPreOpImpl.class.getName())) {
            var op = ((UnaryPreOpImpl) expression).getOp();
            switch (op) {
                case "++":
                case "--":
                    return Symbol.Type.NUMBER;
                case "!":
                    return Symbol.Type.BOOLEAN;
            }
        } else if (expressionType.equals(TableCascadeImpl.class.getName())) {
            return getType(((TableCascadeImpl) expression).getRight(), symbolTable);
        } else {
            if (expressionType.equals(ParenthesisImpl.class.getName())) {
                return getType(((ParenthesisImpl) expression).getElement().getExpression(), symbolTable);
            } else if (expressionType.equals(PresenceOpImpl.class.getName())) {
                return Symbol.Type.BOOLEAN;
            } else if (expressionType.equals(IntLiteralImpl.class.getName())
                    || expressionType.equals(DoubleLiteralImpl.class.getName())) {
                return Symbol.Type.NUMBER;
            } else if (expressionType.equals(OperationCallImpl.class.getName())) {
                var operationCall = (OperationCallImpl) expression;
                switch (operationCall.getName()) {
                    case "count", "max", "min", "mean", "std", "var", "sum":
                        if (operationCall.getParameters().size() > 1) {
                            return Symbol.Type.MAP;
                        } else {
                            return Symbol.Type.NUMBER;
                        }
                    default:
                        return Symbol.Type.TABLE;
                }
            } else if (expressionType.equals(StringLiteralImpl.class.getName())) {
                return Symbol.Type.STRING;
            } else if (expressionType.equals(BooleanLiteralImpl.class.getName())) {
                return Symbol.Type.BOOLEAN;
            } else if (expressionType.equals(IdentifierImpl.class.getName())) {
                var idName = ((IdentifierImpl) expression).getId();

                if (symbolTable.getTableSchema(idName) != null) {
                    return Symbol.Type.TABLE;
                } else if (symbolTable.getExporter(idName) != null) {
                    return Symbol.Type.EXPORTER;
                } else if (symbolTable.getSource(idName) != null) {
                    return Symbol.Type.SOURCE;
                } else if (symbolTable.getRawSymbol(idName) != null) {
                    return symbolTable.getRawSymbol(idName).type();
                }
            }
        }

        return null;
    }
}
