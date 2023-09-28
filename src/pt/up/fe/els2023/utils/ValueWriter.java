package pt.up.fe.els2023.utils;

public class ValueWriter {
    public static String write(Object value) {
        if (value == null) {
            return "";
        }

        return value.toString();
    }
}
