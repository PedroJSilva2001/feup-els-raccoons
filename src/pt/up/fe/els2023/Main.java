package pt.up.fe.els2023;

import pt.up.fe.els2023.interpreter.InterpreterData;
import pt.up.fe.els2023.interpreter.ConfigReader;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.interpreter.VariablesTable;
import pt.up.fe.els2023.interpreter.TableCascadeInterpreter;
import pt.up.fe.els2023.model.table.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar els2023.jar <config_file>");
            return;
        }

        String configFile = args[0];

        ConfigReader configReader = new ConfigReader(configFile);
        try {
            InterpreterData interpreterData = configReader.readConfig();

            Map<String, Table> tables = new HashMap<>();

            for (var tableSchema : interpreterData.tableSchemas()) {
                tables.put(tableSchema.name(), tableSchema.collect());
            }

            var values = new HashMap<String, Value>();

            var operations = interpreterData.operations();

            var variablesTable = new VariablesTable(tables, values);

            var btcInterpreter = new TableCascadeInterpreter(variablesTable);

            for (var cascade : operations) {
                try {
                    btcInterpreter.execute(cascade);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}