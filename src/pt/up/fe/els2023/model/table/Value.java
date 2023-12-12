package pt.up.fe.els2023.model.table;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Comparator;
import java.util.Objects;

public class Value {
    public enum Type {
        NULL {
            @Override
            public Value cast(Value value) {
                return Value.ofNull();
            }

            @Override
            public Comparator<Value> comparator() {
                return Comparator.comparing(Value::isNull);
            }

            @Override
            public Value additiveIdentity() {
                return Value.ofNull();
            }
        },
        BOOLEAN {
            @Override
            public Value cast(Value value) {
                return switch (value.getType()) {
                    case NULL -> Value.ofNull();
                    case BOOLEAN -> Value.of((Boolean) value.getValue());
                    case LONG -> Value.of(((Long) value.getValue()) != 0);
                    case DOUBLE -> Value.of(((Double) value.getValue()) != 0);
                    case STRING -> Value.of(Boolean.parseBoolean((String) value.getValue()));
                    case BIG_INTEGER -> Value.of(((BigInteger) value.getValue()).compareTo(BigInteger.ZERO) != 0);
                    case BIG_DECIMAL -> Value.of(((BigDecimal) value.getValue()).compareTo(BigDecimal.ZERO) != 0);
                };
            }

            @Override
            public Comparator<Value> comparator() {
                return Comparator.comparing(Value::isBoolean).thenComparing(v -> (Boolean) v.getValue());
            }

            @Override
            public Value additiveIdentity() {
                return Value.of(false);
            }
        },
        LONG {
            @Override
            public Value cast(Value value) {
                return switch (value.getType()) {
                    case NULL -> Value.ofNull();
                    case BOOLEAN -> Value.of((Boolean) value.getValue() ? 1L : 0L);
                    case LONG -> Value.of((Long) value.getValue());
                    case DOUBLE -> Value.of(((Double) value.getValue()).longValue());
                    case STRING -> {
                        try {
                            yield Value.of(Long.parseLong((String) value.getValue()));
                        } catch (NumberFormatException e) {
                            yield Value.ofNull();
                        }
                    }
                    case BIG_INTEGER -> Value.of(((BigInteger) value.getValue()).longValue());
                    case BIG_DECIMAL -> Value.of(((BigDecimal) value.getValue()).longValue());
                };
            }

            @Override
            public Comparator<Value> comparator() {
                return Comparator.comparing(Value::isLong).thenComparing(v -> (Long) v.getValue());
            }

            @Override
            public Value additiveIdentity() {
                return Value.of(0L);
            }
        },
        DOUBLE {
            @Override
            public Value cast(Value value) {
                return switch (value.getType()) {
                    case NULL -> Value.ofNull();
                    case BOOLEAN -> Value.of((Boolean) value.getValue() ? 1.0 : 0.0);
                    case LONG -> Value.of(((Long) value.getValue()).doubleValue());
                    case DOUBLE -> Value.of((Double) value.getValue());
                    case STRING -> {
                        try {
                            yield Value.of(Double.parseDouble((String) value.getValue()));
                        } catch (NumberFormatException e) {
                            yield Value.ofNull();
                        }
                    }
                    case BIG_INTEGER -> Value.of(((BigInteger) value.getValue()).doubleValue());
                    case BIG_DECIMAL -> Value.of(((BigDecimal) value.getValue()).doubleValue());
                };
            }

            @Override
            public Comparator<Value> comparator() {
                return Comparator.comparing(Value::isDouble).thenComparing(v -> (Double) v.getValue());
            }

            @Override
            public Value additiveIdentity() {
                return Value.of(0.0);
            }
        },
        STRING {
            @Override
            public Value cast(Value value) {
                return switch (value.getType()) {
                    case NULL -> Value.of("");
                    case BOOLEAN, LONG, DOUBLE, BIG_INTEGER, BIG_DECIMAL -> Value.of(value.getValue().toString());
                    case STRING -> Value.of((String) value.getValue());
                };
            }

            @Override
            public Comparator<Value> comparator() {
                return Comparator.comparing(Value::isString).thenComparing(v -> (String) v.getValue());
            }

            @Override
            public Value additiveIdentity() {
                return Value.of("");
            }
        },
        BIG_INTEGER {
            @Override
            public Value cast(Value value) {
                return switch (value.getType()) {
                    case NULL -> Value.ofNull();
                    case BOOLEAN -> Value.of((Boolean) value.getValue() ? BigInteger.ONE : BigInteger.ZERO);
                    case LONG -> Value.of(BigInteger.valueOf((Long) value.getValue()));
                    case DOUBLE -> Value.of(BigDecimal.valueOf((Double) value.getValue()).toBigInteger());
                    case STRING -> {
                        try {
                            yield Value.of(new BigInteger((String) value.getValue()));
                        } catch (NumberFormatException e) {
                            yield Value.ofNull();
                        }
                    }
                    case BIG_INTEGER -> Value.of((BigInteger) value.getValue());
                    case BIG_DECIMAL -> Value.of(((BigDecimal) value.getValue()).toBigInteger());
                };
            }

            @Override
            public Comparator<Value> comparator() {
                return Comparator.comparing(Value::isBigInteger).thenComparing(v -> (BigInteger) v.getValue());
            }

            @Override
            public Value additiveIdentity() {
                return Value.of(BigInteger.ZERO);
            }
        },
        BIG_DECIMAL {
            @Override
            public Value cast(Value value) {
                return switch (value.getType()) {
                    case NULL -> Value.ofNull();
                    case BOOLEAN -> Value.of((Boolean) value.getValue() ? BigDecimal.ONE : BigDecimal.ZERO);
                    case LONG -> Value.of(BigDecimal.valueOf((Long) value.getValue()));
                    case DOUBLE -> Value.of(BigDecimal.valueOf((Double) value.getValue()));
                    case STRING -> {
                        try {
                            yield Value.of(new BigDecimal((String) value.getValue()));
                        } catch (NumberFormatException e) {
                            yield Value.ofNull();
                        }
                    }
                    case BIG_INTEGER -> Value.of(new BigDecimal((BigInteger) value.getValue()));
                    case BIG_DECIMAL -> Value.of((BigDecimal) value.getValue());
                };
            }

            @Override
            public Comparator<Value> comparator() {
                return Comparator.comparing(Value::isBigDecimal).thenComparing(v -> (BigDecimal) v.getValue());
            }

            @Override
            public Value additiveIdentity() {
                return Value.of(BigDecimal.ZERO);
            }
        };

        public abstract Value cast(Value value);

        public abstract Comparator<Value> comparator();

        public abstract Value additiveIdentity();

        public static Type mostGeneralRep(Type t1, Type t2) {
            if (t1 == Type.STRING || t2 == Type.STRING || t1 == Type.NULL || t2 == Type.NULL) {
                return Type.STRING;
            }

            if (t1 == Type.BIG_DECIMAL || t2 == Type.BIG_DECIMAL) {
                return Type.BIG_DECIMAL;
            }

            if (t1 == Type.DOUBLE || t2 == Type.DOUBLE) {
                return Type.DOUBLE;
            }

            if (t1 == Type.BIG_INTEGER || t2 == Type.BIG_INTEGER) {
                return Type.BIG_INTEGER;
            }

            if (t1 == Type.LONG || t2 == Type.LONG) {
                return Type.LONG;
            }

            if (t1 == Type.BOOLEAN || t2 == Type.BOOLEAN) {
                return Type.BOOLEAN;
            }

            return Type.NULL;
        }

        public static Type mostGeneralNumberRep(Type t1, Type t2) {
            if (t1 == Type.NULL || t2 == Type.NULL || t1 == Type.STRING || t2 == Type.STRING || t1 == Type.BOOLEAN || t2 == Type.BOOLEAN) {
                return null;
            }

            if (t1 == Type.BIG_DECIMAL || t2 == Type.BIG_DECIMAL) {
                return Type.BIG_DECIMAL;
            }

            if (t1 == Type.DOUBLE || t2 == Type.DOUBLE) {
                return Type.DOUBLE;
            }

            if (t1 == Type.BIG_INTEGER || t2 == Type.BIG_INTEGER) {
                return Type.BIG_INTEGER;
            }

            if (t1 == Type.LONG || t2 == Type.LONG) {
                return Type.LONG;
            }

            return null;
        }
    }

    private final Object value;
    private final Type type;

    private static final MathContext defaultMathContext = new MathContext(1000);

    private Value(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    public static Value ofNull() {
        return new Value(null, Type.NULL);
    }

    public static Value ofObject(Object value) {
        if (value == null) {
            return ofNull();
        }

        if (value instanceof Boolean) {
            return of((Boolean) value);
        }

        if (value instanceof Integer) {
            return of((Integer) value);
        }

        if (value instanceof Long) {
            return of((Long) value);
        }

        if (value instanceof Double) {
            return of((Double) value);
        }

        if (value instanceof String) {
            return of((String) value);
        }

        if (value instanceof BigInteger) {
            return of((BigInteger) value);
        }

        if (value instanceof BigDecimal) {
            return of((BigDecimal) value);
        }

        throw new IllegalArgumentException("Unknown type: " + value.getClass());
    }

    public static Value of(Boolean value) {
        return new Value(value, Type.BOOLEAN);
    }

    public static Value of(Integer value) {
        return new Value(value.longValue(), Type.LONG);
    }

    public static Value of(Long value) {
        return new Value(value, Type.LONG);
    }

    public static Value of(Double value) {
        return new Value(value, Type.DOUBLE);
    }

    public static Value of(String value) {
        return new Value(value, Type.STRING);
    }

    public static Value of(BigInteger value) {
        return new Value(value, Type.BIG_INTEGER);
    }

    public static Value of(BigDecimal value) {
        return new Value(value, Type.BIG_DECIMAL);
    }

    public boolean isNull() {
        return type == Type.NULL;
    }

    public boolean isBoolean() {
        return type == Type.BOOLEAN;
    }

    public boolean isLong() {
        return type == Type.LONG;
    }

    public boolean isDouble() {
        return type == Type.DOUBLE;
    }

    public boolean isString() {
        return type == Type.STRING;
    }

    public boolean isBigInteger() {
        return type == Type.BIG_INTEGER;
    }

    public boolean isBigDecimal() {
        return type == Type.BIG_DECIMAL;
    }

    public boolean isNumber() {
        return isLong() || isDouble() || isBigInteger() || isBigDecimal();
    }

    public Object getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        if (this.value == null) {
            return "";
        }

        return this.value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value value1)) return false;
        return Objects.equals(value, value1.value) && type == value1.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    public boolean equals(Value obj) {
        var commonNumberRep = Type.mostGeneralNumberRep(this.type, obj.type);

        if (commonNumberRep == null) {
            if (obj.type == Type.NULL || this.type == Type.NULL) {
                return false;
            }

            return this.value.equals(obj.value);
        }

        var thisCasted = commonNumberRep.cast(this);
        var objCasted = commonNumberRep.cast(obj);

        return switch (commonNumberRep) {
            case NULL, BOOLEAN, STRING -> false;
            case LONG -> ((Long)thisCasted.getValue()).equals((Long)objCasted.getValue());
            case DOUBLE -> ((Double)thisCasted.getValue()).equals((Double)objCasted.getValue());
            case BIG_INTEGER -> ((BigInteger)thisCasted.getValue()).equals((BigInteger)objCasted.getValue());
            case BIG_DECIMAL -> ((BigDecimal)thisCasted.getValue()).equals((BigDecimal)objCasted.getValue());
        };
    }

    public static Value add(Value v1, Value v2) throws IllegalArgumentException {
        if (!v1.isNumber() || !v2.isNumber()) {
            throw new IllegalArgumentException("Cannot add non-numbers");
        }

        var generalNumberRep = Type.mostGeneralNumberRep(v1.type, v2.type);

        assert generalNumberRep != null;

        var v1Casted = generalNumberRep.cast(v1);
        var v2Casted = generalNumberRep.cast(v2);

        return switch (generalNumberRep) {
            case NULL, BOOLEAN, STRING -> null;
            case LONG -> Value.of(((Long)v1Casted.getValue()) + (Long)v2Casted.getValue());
            case DOUBLE -> Value.of(((Double)v1Casted.getValue()) + (Double)v2Casted.getValue());
            case BIG_INTEGER -> Value.of(
                    ((BigInteger)v1Casted.getValue())
                            .add((BigInteger) v2Casted.getValue()));
            case BIG_DECIMAL -> Value.of(
                    ((BigDecimal)v1Casted.getValue())
                            .add((BigDecimal) v2Casted.getValue()));
        };
    }

    public Value add(Value v2) throws IllegalArgumentException {
        return add(this, v2);
    }

    public Value negate() throws IllegalArgumentException {
        if (!this.isNumber()) {
            throw new IllegalArgumentException("Cannot negate non-numbers");
        }

        return switch (this.type) {
            case NULL, BOOLEAN, STRING -> null;
            case LONG -> Value.of(-((Long) this.getValue()));
            case DOUBLE -> Value.of(-((Double) this.getValue()));
            case BIG_INTEGER -> Value.of(((BigInteger) this.getValue()).negate());
            case BIG_DECIMAL -> Value.of(((BigDecimal) this.getValue()).negate());
        };
    }

    public Value subtract(Value v2) throws IllegalArgumentException {
        return add(this, v2.negate());
    }

    public Value multiply(Value right) {
        return multiply(this, right);
    }

    public static Value multiply(Value left, Value right) {
        var commonNumberRep = Type.mostGeneralNumberRep(left.type, right.type);

        if (commonNumberRep == null) {
            return Value.ofNull();
        }

        var leftCasted = commonNumberRep.cast(left);
        var rightCasted = commonNumberRep.cast(right);

        return switch (commonNumberRep) {
            case NULL, BOOLEAN, STRING -> null;
            case LONG -> Value.of(
                    ((Long)leftCasted.getValue()) * (Long)rightCasted.getValue());
            case DOUBLE -> Value.of(
                    ((Double)leftCasted.getValue()) * (Double)rightCasted.getValue());
            case BIG_INTEGER -> Value.of(
                    ((BigInteger)leftCasted.getValue())
                            .multiply((BigInteger) rightCasted.getValue()));
            case BIG_DECIMAL -> Value.of(
                    ((BigDecimal)leftCasted.getValue())
                            .multiply((BigDecimal) rightCasted.getValue()));
        };
    }

    public static Value divide(Value nume, Value denom, MathContext ctx) {
        var commonNumberRep = Type.mostGeneralNumberRep(nume.type, denom.type);

        if (commonNumberRep == null) {
            return Value.ofNull();
        }

        Value thisCasted;
        Value denomCasted;

        if (commonNumberRep == Type.LONG) {
            thisCasted = Type.DOUBLE.cast(nume);
            denomCasted = Type.DOUBLE.cast(denom);
            commonNumberRep = Type.DOUBLE;
        } else {
            thisCasted = commonNumberRep.cast(nume);
            denomCasted = commonNumberRep.cast(denom);
        }

        return switch (commonNumberRep) {
            case NULL, BOOLEAN, STRING -> null;
            case LONG -> Value.of(
                    ((Long)thisCasted.getValue()) / (Long)denomCasted.getValue());
            case DOUBLE -> Value.of(
                    ((Double)thisCasted.getValue()) / (Double)denomCasted.getValue());
            case BIG_INTEGER -> Value.of(
                    ((BigInteger)thisCasted.getValue())
                            .divide(
                                    (BigInteger) denomCasted.getValue()));
            case BIG_DECIMAL -> Value.of(((BigDecimal)thisCasted.getValue())
                    .divide(
                            (BigDecimal) denomCasted.getValue(), ctx));
        };
    }

    public Value divide(Value v2, MathContext ctx) {
        return divide(this, v2, ctx);
    }

    public Value divide(Value v2) {
        return divide(this, v2, defaultMathContext);
    }

    public static Value pow(Value base, Value exponent, MathContext ctx) {
        var commonNumberRep = Type.mostGeneralNumberRep(base.type, exponent.type);

        if (commonNumberRep == null) {
            return Value.ofNull();
        }

        var baseCasted = commonNumberRep.cast(base);
        var exponentCasted = commonNumberRep.cast(exponent);

        return switch (commonNumberRep) {
            case NULL, BOOLEAN, STRING -> null;
            case LONG -> Value.of((long) Math.pow((Long) baseCasted.getValue(), (Long) exponentCasted.getValue()));
            case DOUBLE -> Value.of(Math.pow((Double) baseCasted.getValue(), (Double) exponentCasted.getValue()));
            case BIG_INTEGER -> Value.of(((BigInteger) baseCasted.getValue())
                    .pow(((BigInteger) exponentCasted.getValue()).intValue()));
            case BIG_DECIMAL -> Value.of(((BigDecimal) baseCasted.getValue())
                    .pow(((BigDecimal) exponentCasted.getValue()).intValue(), ctx));
        };
    }

    public Value pow(Value v2) {
        return pow(this, v2, defaultMathContext);
    }

    public static Value sqrt(Value value, MathContext ctx) {
        var commonNumberRep = value.type;

        Value valueCasted;

        if (commonNumberRep == Type.LONG) {
            valueCasted = Type.DOUBLE.cast(value);
            commonNumberRep = Type.DOUBLE;
        } else {
            valueCasted = value;
        }

        return switch (commonNumberRep) {
            case NULL, BOOLEAN, STRING -> null;
            case LONG -> Value.of((long) Math.sqrt((Long) valueCasted.getValue()));
            case DOUBLE -> Value.of(Math.sqrt((Double) valueCasted.getValue()));
            case BIG_INTEGER -> Value.of(((BigInteger) valueCasted.getValue()).sqrt());
            case BIG_DECIMAL -> Value.of(((BigDecimal) valueCasted.getValue()).sqrt(ctx));
        };
    }

    public Value sqrt() {
        return sqrt(this, defaultMathContext);
    }

    public boolean lessThan(Value obj) {
        var commonNumberRep = Type.mostGeneralNumberRep(this.type, obj.type);

        if (commonNumberRep == null) {
            return false;
        }

        var thisCasted = commonNumberRep.cast(this);
        var objCasted = commonNumberRep.cast(obj);

        return switch (commonNumberRep) {
            case NULL, BOOLEAN, STRING -> false;
            case LONG -> ((Long)thisCasted.getValue()) < (Long)objCasted.getValue();
            case DOUBLE -> ((Double)thisCasted.getValue()) < (Double)objCasted.getValue();
            case BIG_INTEGER -> ((BigInteger)thisCasted.getValue()).compareTo((BigInteger)objCasted.getValue()) < 0;
            case BIG_DECIMAL -> ((BigDecimal)thisCasted.getValue()).compareTo((BigDecimal)objCasted.getValue()) < 0;
        };
    }

    public boolean greaterThan(Value obj) {
        var commonNumberRep = Type.mostGeneralNumberRep(this.type, obj.type);

        if (commonNumberRep == null) {
            return false;
        }

        var thisCasted = commonNumberRep.cast(this);
        var objCasted = commonNumberRep.cast(obj);

        return switch (commonNumberRep) {
            case NULL, BOOLEAN, STRING -> false;
            case LONG -> ((Long)thisCasted.getValue()) > (Long)objCasted.getValue();
            case DOUBLE -> ((Double)thisCasted.getValue()) > (Double)objCasted.getValue();
            case BIG_INTEGER -> ((BigInteger)thisCasted.getValue()).compareTo((BigInteger)objCasted.getValue()) > 0;
            case BIG_DECIMAL -> ((BigDecimal)thisCasted.getValue()).compareTo((BigDecimal)objCasted.getValue()) > 0;
        };
    }

    public boolean greaterThanOrEqual(Value obj) {
        return this.greaterThan(obj) || this.equals(obj);
    }

    public boolean lessThanOrEqual(Value obj) {
        return this.lessThan(obj) || this.equals(obj);
    }
}