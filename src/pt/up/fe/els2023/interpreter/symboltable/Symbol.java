package pt.up.fe.els2023.interpreter.symboltable;

public class Symbol {
    public enum Type {
        TABLE,
        VALUE,
        NOT_DEFINED
    }

    public class SymbolValue {
        private Object symbolValue;

        public SymbolValue(Object symbolValue) {
            this.symbolValue = symbolValue;
        }

        public Object getSymbolValue() {
            return symbolValue;
        }

        public void setSymbolValue(Object symbolValue) {
            this.symbolValue = symbolValue;
        }

    }

    private final String name;

    private Type type;

    private final SymbolValue value;

    public Symbol(String name) {
        this.name = name;
        this.type = Type.NOT_DEFINED;
        this.value = new SymbolValue(null);
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public SymbolValue getValue() {
        return value;
    }

    public void setValue(Type type, Object value) {
        this.type = type;
        this.value.setSymbolValue(value);
    }
}
