package pt.up.fe.els2023.interpreter.symboltable;

public record Symbol<T>(
        String name,
        Type type,
        T value,
        int declarationLine
) {
    public enum Type {
        TABLE,
        BOOLEAN,
        NUMBER,
        INTEGER,
        DOUBLE,
        STRING,
        NULL,
        MAP,
        SOURCE,
        TABLE_SCHEMA,
        EXPORTER,
        NOT_RESOLVED
    }

    public static Symbol<Void> ofVoid(String name, int declarationLine) {
        return new Symbol<>(name, Type.NOT_RESOLVED, null, declarationLine);
    }

    public static <E> Symbol<E> of(String name, Type type, E value, int declarationLine) {
        return new Symbol<>(name, type, value, declarationLine);
    }
}
