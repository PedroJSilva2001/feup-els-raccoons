package pt.up.fe.els2023.interpreter.diagnostic;

public record Diagnostic(String file, Level level, int line, int column, String message) {

    public enum Level {
        INFO,
        WARNING,
        ERROR
    }

    public static Diagnostic info(String file, int line, int column, String message) {
        return new Diagnostic(file, Level.INFO, line, column, message);
    }

    public static Diagnostic info(String file, String message) {
        return new Diagnostic(file, Level.INFO, -1, -1, message);
    }

    public static Diagnostic warning(String file, int line, int column, String message) {
        return new Diagnostic(file, Level.WARNING, line, column, message);
    }

    public static Diagnostic warning(String file, String message) {
        return new Diagnostic(file, Level.WARNING, -1, -1, message);
    }

    public static Diagnostic error(String file, int line, int column, String message) {
        return new Diagnostic(file, Level.ERROR, line, column, message);
    }

    public static Diagnostic error(String file, String message) {
        return new Diagnostic(file, Level.ERROR, -1, -1, message);
    }

    @Override
    public String toString() {
        return String.format("<%s>:%d:%d: %s: %s", file, line, column, level, message);
    }
}
