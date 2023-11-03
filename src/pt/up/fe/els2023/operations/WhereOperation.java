package pt.up.fe.els2023.operations;

import pt.up.fe.els2023.table.Value;

import java.util.Objects;
import java.util.function.Predicate;

public class WhereOperation implements TableOperation {

    private final String predicate;

    public WhereOperation(String predicate) {
        this.predicate = predicate;
    }

    public void accept(BTCinterpreter btcInterpreter) {
        btcInterpreter.apply(this);
    }

    public BeginTableCascade execute(BeginTableCascade btc) {
        return btc.where(parsePredicate(predicate));
    }

    private Predicate<RowWrapper> parsePredicate(String predicate) {
        String[] parts = predicate.split(" ");

        if (parts.length < 3) {
            System.out.println("Invalid predicate");
            return null;
        }

        Predicate<RowWrapper> result = parseCondition(parts[0] + " " + parts[1] + " " + parts[2]);

        for (int i = 4; i + 3 < parts.length; i+=4) {
            if (parts[i].equals("AND") || parts[i].equals("and") || parts[i].equals("&&")) {
                assert result != null;
                result = result.and(Objects.requireNonNull(parseCondition(parts[i + 1] + " " + parts[i + 2] + " " + parts[i + 3])));
            } else if (parts[i].equals("OR") || parts[i].equals("or") || parts[i].equals("||")) {
                assert result != null;
                result = result.or(Objects.requireNonNull(parseCondition(parts[i + 1] + " " + parts[i + 2] + " " + parts[i + 3])));
            }
        }

        System.out.println(result);

        return result;
    }

    private Predicate<RowWrapper> parseCondition(String condition) {
        String[] parts = condition.split(" ");
        if (parts.length != 3) {
            System.out.println("Invalid condition");
            return null;
        }

        String column = parts[0];
        String operator = parts[1];
        String value = parts[2];

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
