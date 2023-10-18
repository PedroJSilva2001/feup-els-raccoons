package pt.up.fe.els2023.imports;

import java.util.Set;

public class ColumnUtils {
    public static String makeUnique(String columnName, Set<String> columnNames) {
        String newColumnName = columnName;
        if (columnNames.contains(newColumnName)) {
            int count = 1;
            while (columnNames.contains(newColumnName + '_' + count)) {
                count++;
            }

            newColumnName += '_' + count;
        }

        return newColumnName;
    }
}
