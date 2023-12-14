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

        addVisit(ExpressionImpl.class.getName(), this::visitExpression);
    }

    private Void visitExpression(EObject node, SymbolTable symbolTable) {
        Diagnostic error = null;
        Diagnostic newError = null;

        var it = node.eAllContents();

        EObject expression;

        while (it.hasNext()) {
            expression = it.next();

            if (expression.getClass().getName().equals(LogicalOrImpl.class.getName()) || expression.getClass().getName().equals(LogicalAndImpl.class.getName())) {
                newError = visitBinaryOp(expression, symbolTable);
            } else if (expression.getClass().getName().equals(EqualsAndNEqualsImpl.class.getName()) || expression.getClass().getName().equals(ComparisonImpl.class.getName())) {
                newError = visitComparison(expression, symbolTable);
            } else if (expression.getClass().getName().equals(AddAndSubImpl.class.getName()) || expression.getClass().getName().equals(MultAndDivImpl.class.getName())) {
                newError = visitMathOp(expression, symbolTable);
            } else if (expression.getClass().getName().equals(UnaryPreOpImpl.class.getName())) {
                newError = visitUnaryPreOp(expression, symbolTable);
            } else if (expression.getClass().getName().equals(TableCascadeImpl.class.getName())) {
                newError = visitTableCascade(expression, symbolTable);
            }

            if (newError != null) {
                error = newError;
            }
        }

        if (error != null) {
            addError(error);
        }

        return null;
    }

    private Diagnostic visitBinaryOp(EObject node, SymbolTable symbolTable) {
        var leftType = getType(((LogicalOrImpl) node).getLeft(), symbolTable);
        var rightType = getType(((LogicalOrImpl) node).getRight(), symbolTable);

        if (leftType != rightType || leftType != Symbol.Type.BOOLEAN) {
            return Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types '" + leftType + "' and '" + rightType + "' in line " + NodeModelUtils.getNode(node).getStartLine());
        }
        return null;
    }

    private Diagnostic visitComparison(EObject node, SymbolTable symbolTable) {
        var leftType = getType(((LogicalOrImpl) node).getLeft(), symbolTable);
        var rightType = getType(((LogicalOrImpl) node).getRight(), symbolTable);

        if ((leftType != rightType || (leftType != Symbol.Type.BOOLEAN && leftType != Symbol.Type.NUMBER && leftType != Symbol.Type.STRING)) && (leftType != Symbol.Type.NOT_RESOLVED && rightType != Symbol.Type.NOT_RESOLVED)) {
            return Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types '" + leftType + "' and '" + rightType + "' in line " + NodeModelUtils.getNode(node).getStartLine());
        }
        return null;
    }

    private Diagnostic visitMathOp(EObject node, SymbolTable symbolTable) {
        var leftType = getType(((LogicalOrImpl) node).getLeft(), symbolTable);
        var rightType = getType(((LogicalOrImpl) node).getRight(), symbolTable);

        if ((leftType != rightType || leftType != Symbol.Type.NUMBER) && (leftType != Symbol.Type.NOT_RESOLVED && rightType != Symbol.Type.NOT_RESOLVED)) {
            return Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types '" + leftType + "' and '" + rightType + "' in line " + NodeModelUtils.getNode(node).getStartLine());
        }
        return null;
    }

    private Diagnostic visitUnaryPreOp(EObject node, SymbolTable symbolTable) {
        var unaryPreOpType = getType(node, symbolTable);
        var subExpression = ((UnaryPreOpImpl) node).getSubExpression();

        var subExpressionType = getType(subExpression, symbolTable);

        if (subExpressionType != unaryPreOpType) {
            return Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types '" + subExpressionType + "' and '" + unaryPreOpType + "' in line " + NodeModelUtils.getNode(node).getStartLine());
        }
        return null;
    }

    private Diagnostic visitTableCascade(EObject node, SymbolTable symbolTable) {
        var rightType = getType(((TableCascadeImpl) node).getRight(), symbolTable);

        if (rightType != Symbol.Type.TABLE && rightType != Symbol.Type.MAP && rightType != Symbol.Type.NUMBER) {
            return Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(node).getStartLine(), -1,
                    "Incompatible types in line " + NodeModelUtils.getNode(node).getStartLine());
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
            } else if (expressionType.equals(PresenceOpImpl.class.getName()) || expressionType.equals(ColumnAccessImpl.class.getName()) || expressionType.equals(NullCheckImpl.class.getName()) || expressionType.equals(MapGetImpl.class.getName())) {
                return Symbol.Type.NOT_RESOLVED;
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
                    var type = symbolTable.getRawSymbol(idName).type();

                    if (type == Symbol.Type.INTEGER || type == Symbol.Type.DOUBLE) {
                        return Symbol.Type.NUMBER;
                    }
                    return type;
                }
            }
        }

        return null;
    }
}
