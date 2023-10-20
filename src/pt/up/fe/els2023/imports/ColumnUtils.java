package pt.up.fe.els2023.imports;

import java.util.Set;

public class ColumnUtils {
    /**
     * Given a column name and a set of column names, returns a column name that is not in the set by appending a
     * number to the original column name.
     * <p></p>
     * <b>Example</b>: if the column name is "id" and the set contains "id", the method will return "id_1". If the set
     * contains "id" and "id_1", the method will return "id_2".
     *
     * @param columnName  The original column name.
     * @param columnNames The set of column names.
     * @return A column name that is not in the set.
     */
    public static String makeUnique(String columnName, Set<String> columnNames) {
        // TODO: TEST
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
