package pt.up.fe.els2023.interpreter.runtime;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.exceptions.ColumnNotFoundException;
import pt.up.fe.els2023.exceptions.ImproperTerminalOperationException;
import pt.up.fe.els2023.exceptions.RacoonsRuntimeException;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.interpreter.semantic.SemanticAnalysisResult;
import pt.up.fe.els2023.interpreter.signatures.Signatures;
import pt.up.fe.els2023.interpreter.symboltable.SymbolTable;
import pt.up.fe.els2023.interpreter.syntactic.SyntacticAnalysisResult;
import pt.up.fe.els2023.model.operations.OperationResult;
import pt.up.fe.els2023.model.operations.TableOperation;
import pt.up.fe.els2023.model.table.RacoonTable;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.racoons.*;
import pt.up.fe.els2023.racoons.impl.*;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

import java.lang.Boolean;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InterpreterRuntime {
    private SymbolTable symbolTable;

    public void run(SyntacticAnalysisResult syntaticResult, SemanticAnalysisResult semanticResult, Map<String, String> config) {
        var rootNode = syntaticResult.root();
        symbolTable = semanticResult.symbolTable();

        analyseExpressionsAndAssignments(rootNode);
    }

    private void analyseExpressionsAndAssignments(EObject root) throws RacoonsRuntimeException {
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

    private Void analyseExpression(Expression _expression) throws RacoonsRuntimeException {
        var expression = _expression.getExpression();

        analyseLogicalOr(expression);
        return null;
    }

    private Object analyseLogicalOr(LogicalOr expression) throws RacoonsRuntimeException {
        if (expression.getClass() == ParenthesisImpl.class) {
            var parenthesis = (Parenthesis) expression;

            return analyseLogicalOr(parenthesis.getElement().getExpression());
        } else if (expression.getClass() == TableCascadeImpl.class) {
            return analyseTableCascade((TableCascade) expression);
        } else if (expression.getClass() == OperationCallImpl.class) {
            return analyseBaseOperationCall((OperationCall) expression);
        } else if (expression.getClass() == IdentifierImpl.class) {
            return analyseIdentifier((Identifier) expression);
        } else if (expression.getClass() == ColumnAccessImpl.class) {
            // TODO
            return null;
        } else if (expression.getClass() == PresenceOpImpl.class) {
            // TODO
            return null;
        } else if (expression.getClass() == StringLiteralImpl.class) {
            return ((StringLiteral) expression).getValue();
        } else if (expression.getClass() == IntLiteralImpl.class) {
            return Integer.parseInt(((IntLiteral) expression).getValue());
        } else if (expression.getClass() == DoubleLiteralImpl.class) {
            return Double.parseDouble(((DoubleLiteral) expression).getValue());
        } else if (expression.getClass() == BooleanLiteralImpl.class) {
            return Boolean.parseBoolean(((BooleanLiteral) expression).getValue());
        } else if (expression.getClass() == UnaryPreOpImpl.class) {
            return analyseUnaryOp((UnaryPreOp) expression);
        } else if (expression.getClass() == MultAndDivImpl.class) {
            return analyseMultAndDiv((MultAndDiv) expression);
        } else if (expression.getClass() == AddAndSubImpl.class) {
            return analyseAddAndSub((AddAndSub) expression);
        } else if (expression.getClass() == ComparisonImpl.class) {
            return analyseComparison((Comparison) expression);
        } else if (expression.getClass() == EqualsAndNEqualsImpl.class) {
            return analyseEqualsAndNEquals((EqualsAndNEquals) expression);
        } else if (expression.getClass() == LogicalAndImpl.class) {
            return analyseLogicalAnd((LogicalAnd) expression);
        } else if (expression.getClass() == LogicalOrImpl.class) {
           return analyseBaseLogicalOr((LogicalOr) expression);
        }


        throw new AssertionError("Unsupported expression " + expression.getClass().getName() + " in runtime phase");
    }

    private Object analyseBaseLogicalOr(LogicalOr logicalOr) {
        var left = analyseLogicalOr(logicalOr.getLeft());
        var right = analyseLogicalOr(logicalOr.getRight());

        if (left == null || right == null) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(logicalOr).getStartLine(),
                    -1,
                    "Logical or between null values"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }

        var leftType = left.getClass();
        var rightType = right.getClass();

        if (leftType == Boolean.class && rightType == Boolean.class) {
            return ((Boolean) left) || ((Boolean) right);
        } else {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(logicalOr).getStartLine(),
                    -1,
                    "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in logical or operation"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }
    }

    private Object analyseEqualsAndNEquals(EqualsAndNEquals expression) {
        var left = analyseLogicalOr(expression.getLeft());
        var right = analyseLogicalOr(expression.getRight());

        if (left == null || right == null) {
            return false;
        }

        var leftType = left.getClass();
        var rightType = right.getClass();

        if (Objects.equals(expression.getOp(), "==")) {
            if (leftType == Integer.class && rightType == Integer.class) {
                return ((Number) left).intValue() == ((Number) right).intValue();
            } else if (leftType == Integer.class && rightType == Double.class) {
                return ((Number) left).intValue() == ((Number) right).doubleValue();
            } else if (leftType == Double.class && rightType == Integer.class) {
                return ((Number) left).doubleValue() == ((Number) right).intValue();
            } else if (leftType == Double.class && rightType == Double.class) {
                return ((Number) left).doubleValue() == ((Number) right).doubleValue();
            } else {
                return Objects.equals(left, right);
            }
        } else if (Objects.equals(expression.getOp(), "!=")) {
            if (leftType == Integer.class && rightType == Integer.class) {
                return ((Number) left).intValue() != ((Number) right).intValue();
            } else if (leftType == Integer.class && rightType == Double.class) {
                return ((Number) left).intValue() != ((Number) right).doubleValue();
            } else if (leftType == Double.class && rightType == Integer.class) {
                return ((Number) left).doubleValue() != ((Number) right).intValue();
            } else if (leftType == Double.class && rightType == Double.class) {
                return ((Number) left).doubleValue() != ((Number) right).doubleValue();
            } else {
                return !Objects.equals(left, right);
            }
        }

        throw new AssertionError("Unsupported binary operator " + expression.getOp() + " in runtime phase");
    }

    private Object analyseLogicalAnd(LogicalAnd logicalAnd) {
        var left = analyseLogicalOr(logicalAnd.getLeft());
        var right = analyseLogicalOr(logicalAnd.getRight());

        if (left == null || right == null) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(logicalAnd).getStartLine(),
                    -1,
                    "Logical and between null values"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }

        var leftType = left.getClass();
        var rightType = right.getClass();

        if (leftType == Boolean.class && rightType == Boolean.class) {
            return ((Boolean) left) && ((Boolean) right);
        } else {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(logicalAnd).getStartLine(),
                    -1,
                    "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in logical and operation"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }
    }

    private Object analyseComparison(Comparison comparison) throws RacoonsRuntimeException {
        var left = analyseLogicalOr(comparison.getLeft());
        var right = analyseLogicalOr(comparison.getRight());

        if (left == null || right == null) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(comparison).getStartLine(),
                    -1,
                    "Comparison between null values"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }

        switch (comparison.getOp()) {
            case "<" -> {
                var leftType = left.getClass();
                var rightType = right.getClass();

                if (leftType == Integer.class && rightType == Integer.class) {
                    return ((Number) left).intValue() < ((Number) right).intValue();
                } else if (leftType == Integer.class && rightType == Double.class) {
                    return ((Number) left).intValue() < ((Number) right).doubleValue();
                } else if (leftType == Double.class && rightType == Integer.class) {
                    return ((Number) left).doubleValue() < ((Number) right).intValue();
                } else if (leftType == Double.class && rightType == Double.class) {
                    return ((Number) left).doubleValue() < ((Number) right).doubleValue();
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(comparison).getStartLine(),
                            -1,
                            "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in comparison"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
            case ">" -> {
                var leftType = left.getClass();
                var rightType = right.getClass();

                if (leftType == Integer.class && rightType == Integer.class) {
                    return ((Number) left).intValue() > ((Number) right).intValue();
                } else if (leftType == Integer.class && rightType == Double.class) {
                    return ((Number) left).intValue() > ((Number) right).doubleValue();
                } else if (leftType == Double.class && rightType == Integer.class) {
                    return ((Number) left).doubleValue() > ((Number) right).intValue();
                } else if (leftType == Double.class && rightType == Double.class) {
                    return ((Number) left).doubleValue() > ((Number) right).doubleValue();
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(comparison).getStartLine(),
                            -1,
                            "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in comparison"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
            case "<=" -> {
                var leftType = left.getClass();
                var rightType = right.getClass();

                if (leftType == Integer.class && rightType == Integer.class) {
                    return ((Number) left).intValue() <= ((Number) right).intValue();
                } else if (leftType == Integer.class && rightType == Double.class) {
                    return ((Number) left).intValue() <= ((Number) right).doubleValue();
                } else if (leftType == Double.class && rightType == Integer.class) {
                    return ((Number) left).doubleValue() <= ((Number) right).intValue();
                } else if (leftType == Double.class && rightType == Double.class) {
                    return ((Number) left).doubleValue() <= ((Number) right).doubleValue();
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(comparison).getStartLine(),
                            -1,
                            "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in comparison"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
            case ">=" -> {
                var leftType = left.getClass();
                var rightType = right.getClass();

                if (leftType == Integer.class && rightType == Integer.class) {
                    return ((Number) left).intValue() >= ((Number) right).intValue();
                } else if (leftType == Integer.class && rightType == Double.class) {
                    return ((Number) left).intValue() >= ((Number) right).doubleValue();
                } else if (leftType == Double.class && rightType == Integer.class) {
                    return ((Number) left).doubleValue() >= ((Number) right).intValue();
                } else if (leftType == Double.class && rightType == Double.class) {
                    return ((Number) left).doubleValue() >= ((Number) right).doubleValue();
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(comparison).getStartLine(),
                            -1,
                            "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in comparison"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
        }

        throw new AssertionError("Unsupported binary operator " + comparison.getOp() + " in runtime phase");
    }

    private Object analyseAddAndSub(AddAndSub addAndSub) {
        var left = analyseLogicalOr(addAndSub.getLeft());
        var right = analyseLogicalOr(addAndSub.getRight());

        if (left == null || right == null) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(addAndSub).getStartLine(),
                    -1,
                    "Addition or subtraction between null values"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }

        switch (addAndSub.getOp()) {
            case "+" -> {
                var leftType = left.getClass();
                var rightType = right.getClass();

                if (leftType == Integer.class && rightType == Integer.class) {
                    return ((Number) left).intValue() + ((Number) right).intValue();
                } else if (leftType == Integer.class && rightType == Double.class) {
                    return ((Number) left).intValue() + ((Number) right).doubleValue();
                } else if (leftType == Double.class && rightType == Integer.class) {
                    return ((Number) left).doubleValue() + ((Number) right).intValue();
                } else if (leftType == Double.class && rightType == Double.class) {
                    return ((Number) left).doubleValue() + ((Number) right).doubleValue();
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(addAndSub).getStartLine(),
                            -1,
                            "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in addition"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
            case "-" -> {
                var leftType = left.getClass();
                var rightType = right.getClass();

                if (leftType == Integer.class && rightType == Integer.class) {
                    return ((Number) left).intValue() - ((Number) right).intValue();
                } else if (leftType == Integer.class && rightType == Double.class) {
                    return ((Number) left).intValue() - ((Number) right).doubleValue();
                } else if (leftType == Double.class && rightType == Integer.class) {
                    return ((Number) left).doubleValue() - ((Number) right).intValue();
                } else if (leftType == Double.class && rightType == Double.class) {
                    return ((Number) left).doubleValue() - ((Number) right).doubleValue();
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(addAndSub).getStartLine(),
                            -1,
                            "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in subtraction"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
        }

        throw new AssertionError("Unsupported binary operator " + addAndSub.getOp() + " in runtime phase");
    }

    private Object analyseMultAndDiv(MultAndDiv multAndDiv) {
        var left = analyseLogicalOr(multAndDiv.getLeft());
        var right = analyseLogicalOr(multAndDiv.getRight());

        if (left == null || right == null) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(multAndDiv).getStartLine(),
                    -1,
                    "Multiplication or division between null values"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }

        switch (multAndDiv.getOp()) {
            case "*" -> {
                var leftType = left.getClass();
                var rightType = right.getClass();

                if (leftType == Integer.class && rightType == Integer.class) {
                    return ((Number) left).intValue() * ((Number) right).intValue();
                } else if (leftType == Integer.class && rightType == Double.class) {
                    return ((Number) left).intValue() * ((Number) right).doubleValue();
                } else if (leftType == Double.class && rightType == Integer.class) {
                    return ((Number) left).doubleValue() * ((Number) right).intValue();
                } else if (leftType == Double.class && rightType == Double.class) {
                    return ((Number) left).doubleValue() * ((Number) right).doubleValue();
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(multAndDiv).getStartLine(),
                            -1,
                            "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in multiplication"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
            case "/" -> {
                var leftType = left.getClass();
                var rightType = right.getClass();

                if (leftType == Integer.class && rightType == Integer.class) {
                    return ((Number) left).intValue() / ((Number) right).intValue();
                } else if (leftType == Integer.class && rightType == Double.class) {
                    return ((Number) left).intValue() / ((Number) right).doubleValue();
                } else if (leftType == Double.class && rightType == Integer.class) {
                    return ((Number) left).doubleValue() / ((Number) right).intValue();
                } else if (leftType == Double.class && rightType == Double.class) {
                    return ((Number) left).doubleValue() / ((Number) right).doubleValue();
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(multAndDiv).getStartLine(),
                            -1,
                            "Unsupported type " + leftType.getName() + " and " + rightType.getName() + " in division"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
        }

        throw new AssertionError("Unsupported binary operator " + multAndDiv.getOp() + " in runtime phase");
    }

    private Object analyseUnaryOp(UnaryPreOp unaryPreOp) {
        var value = analyseLogicalOr(unaryPreOp.getSubExpression());

        if (value == null) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(unaryPreOp).getStartLine(),
                    -1,
                    "Unary operation on null value"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }

        switch (unaryPreOp.getOp()) {
            case "!" -> {
                if (value.getClass() == Boolean.class) {
                    return !((Boolean) value);
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(unaryPreOp).getStartLine(),
                            -1,
                            "Unsupported type " + value.getClass().getName() + " in logical not operation"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
            case "--" -> {
                if (value.getClass() == Integer.class) {
                    return -((Integer) value);
                } else if (value.getClass() == Double.class) {
                    return -((Double) value);
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(unaryPreOp).getStartLine(),
                            -1,
                            "Unsupported type " + value.getClass().getName() + " in unary minus operation"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
            case "++" -> {
                if (value.getClass() == Integer.class) {
                    return ((Integer) value) + 1;
                } else if (value.getClass() == Double.class) {
                    return ((Double) value) + 1;
                } else {
                    var diagnostic = Diagnostic.error(
                            symbolTable.getRacoonsConfigFilename(),
                            NodeModelUtils.getNode(unaryPreOp).getStartLine(),
                            -1,
                            "Unsupported type " + value.getClass().getName() + " in unary plus operation"
                    );

                    throw new RacoonsRuntimeException(diagnostic);
                }
            }
        }

        throw new AssertionError("Unsupported unary operator " + unaryPreOp.getOp() + " in runtime phase");
    }

    private Object analyseBaseOperationCall(OperationCall operationCall) {
        var name = operationCall.getName();

        if (!Objects.equals(name, "table")) {
            throw new AssertionError("Expected table creation, got " + name);
        }
        var parameters = operationCall.getParameters();

        if (parameters.isEmpty()) {
            return new RacoonTable();
        }

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

        return tableSchema.value().collect();
    }

    private Object analyseTableCascade(TableCascade tableCascade) {
        var left = analyseLogicalOr(tableCascade.getLeft());
        var rightExpression = tableCascade.getRight();

        if (rightExpression.getClass() != OperationCallImpl.class) {
            throw new AssertionError("Expected operation call, got " + rightExpression.getClass().getName());
        }

        if (left == null) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(tableCascade).getStartLine(),
                    -1,
                    "Table cascade on null value"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }

        if (!(left instanceof Table leftTable)) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(tableCascade).getStartLine(),
                    -1,
                    "Table cascade on non-table value"
            );

            throw new RacoonsRuntimeException(diagnostic);
        }

        var operation = analyseOperationCall((OperationCall) rightExpression);

        try {
            if (operation == null)  {
                throw new AssertionError("Unsupported operation " + rightExpression);
            }

            var operationResult = operation.execute(leftTable);

            return switch (operationResult.getType()) {
                case TABLE ->
                    operationResult.getTable();

                case VALUE ->
                    operationResult.getValue();

                case VALUE_MAP ->
                    operationResult.getValueMap();
            };
        } catch (Exception e) {
            var diagnostic = Diagnostic.error(
                    symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(tableCascade).getStartLine(),
                    -1,
                    e.getMessage()
            );

            throw new RacoonsRuntimeException(diagnostic);
        }
    }

    private TableOperation analyseOperationCall(OperationCall operationCall) {
        var name = operationCall.getName();

        if (Objects.equals(name, "where") || Objects.equals(name, "dropWhere")) {
            // TODO: THE PARAMETER IS A LAMBDA
            return null;
        }

        List<Object> parameters = new ArrayList<>();

        for (var expression : operationCall.getParameters()) {
            parameters.add(analyseLogicalOr(expression.getExpression()));
        }

        return Signatures.createOperation(name, parameters);
    }

    private Object analyseIdentifier(Identifier identifier) {
        var id = identifier.getId();
        var symbol = symbolTable.getRawSymbol(id);

        if (symbol != null) {
            return symbol.value();
        }

        var tableSchema = symbolTable.getTableSchema(id);

        if (tableSchema != null) {
            return tableSchema.value();
        }

        var source = symbolTable.getSource(id);

        if (source != null) {
            return source.value();
        }

        var exporter = symbolTable.getExporter(id);

        if (exporter != null) {
            return exporter.value();
        }

        throw new AssertionError("Symbol " + id + " not found");
    }

    private Void analyseAssignment(Assignment assignment) {

        return null;
    }
}