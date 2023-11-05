package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.table.Value;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class WhereOperation implements TableOperation {

    protected final String predicate;

    public WhereOperation(String predicate) {
        this.predicate = predicate;
    }

    public void accept(TableCascadeInterpreter btcInterpreter) {
        btcInterpreter.apply(this);
    }

    public TableCascade execute(TableCascade btc, Map<String, Value> resultVariables) {
        return btc.where(parsePredicate(predicate, resultVariables));
    }

    protected Predicate<RowWrapper> parsePredicate(String predicate, Map<String, Value> resultVariables) {
        String[] parts = predicate.split(" ");

        if (parts.length < 3) {
            System.out.println("Invalid predicate");
            return null;
        }

        Predicate<RowWrapper> result = parseCondition(parts[0] + " " + parts[1] + " " + parts[2], resultVariables);

        for (int i = 3; i + 3 < parts.length; i += 4) {
            if (parts[i].equals("AND") || parts[i].equals("and") || parts[i].equals("&&")) {
                assert result != null;
                result = result.and(Objects.requireNonNull(parseCondition(parts[i + 1] + " " + parts[i + 2] + " " + parts[i + 3], resultVariables)));
            } else if (parts[i].equals("OR") || parts[i].equals("or") || parts[i].equals("||")) {
                assert result != null;
                result = result.or(Objects.requireNonNull(parseCondition(parts[i + 1] + " " + parts[i + 2] + " " + parts[i + 3], resultVariables)));
            }
        }

        System.out.println(result);

        return result;
    }

    private Predicate<RowWrapper> parseCondition(String condition, Map<String, Value> resultVariables) {
        String[] parts = condition.split(" ");
        if (parts.length != 3) {
            System.out.println("Invalid condition");
            return null;
        }

        String column = parts[0];
        String operator = parts[1];
        String value;
        if (parts[2].charAt(0) == '$') {
            value = resultVariables.get(parts[2].substring(1)).toString();
        } else {
            value = parts[2];
        }

        return switch (operator) {
            case "==" -> row -> row.get(column).equals(Value.of(value));
            case "!=" -> row -> !row.get(column).equals(Value.of(value));
            case ">" -> row -> row.get(column).greaterThan(Value.of(Double.parseDouble(value)));
            case ">=" -> row -> row.get(column).greaterThanOrEqual(Value.of(Double.parseDouble(value)));
            case "<" -> row -> row.get(column).lessThan(Value.of(Double.parseDouble(value)));
            case "<=" -> row -> row.get(column).lessThanOrEqual(Value.of(Double.parseDouble(value)));
            default -> null;
        };
    }
}
