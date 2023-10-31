package pt.up.fe.els2023.table;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Comparator;
import java.util.function.BinaryOperator;

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

        public static Type mostGeneralNumberRep(Type t1, Type t2) {
            if (t1 == Value.Type.NULL || t2 == Value.Type.NULL ||
                    t1 == Value.Type.BOOLEAN || t2 == Value.Type.BOOLEAN ||
                    t1 == Value.Type.STRING || t2 == Value.Type.STRING) {
                return null;
            }

            if (t1 == t2) {
                return t1;
            }

            if ((t1 == Value.Type.BIG_INTEGER && t2 == Value.Type.LONG) ||
                    (t1 == Value.Type.LONG && t2 == Value.Type.BIG_INTEGER)) {
                return Value.Type.BIG_INTEGER;

            } else if ((t1 == Value.Type.BIG_DECIMAL && t2 == Value.Type.DOUBLE) ||
                    (t1 == Value.Type.DOUBLE && t2 == Value.Type.BIG_DECIMAL)) {
                return Value.Type.BIG_DECIMAL;

            }

            // the following conversions:
            //      big integer <-> big decimal
            //      big integer <-> double
            //      long <-> big decimal
            //      long <-> double
            // default to big decimal
            return Value.Type.BIG_DECIMAL;

        }
    }

    private final Object value;
    private final Type type;

    private Value(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    public static Value ofNull() {
        return new Value(null, Type.NULL);
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
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Value other)) {
            return false;
        }

        if (this.value == null) {
            return other.value == null;
        }

        return this.value.equals(other.value);
    }

    // TODO refactor ops
    public static Value addS(Value v1, Value v2) throws IllegalArgumentException {
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
        return addS(this, v2);
    }

    public static Value divideS(Value nume, Value denom) {
        var commonNumberRep = Type.mostGeneralNumberRep(nume.type, denom.type);

        if (commonNumberRep == null) {
            return Value.ofNull();
        }

        var thisCasted = commonNumberRep.cast(nume);
        var denomCasted = commonNumberRep.cast(denom);


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
                            (BigDecimal) denomCasted.getValue(), new MathContext(1000)));
        };
    }

    public Value divide(Value v2) {
        return divideS(this, v2);
    }
}