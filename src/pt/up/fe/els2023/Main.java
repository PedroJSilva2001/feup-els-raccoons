package pt.up.fe.els2023;

import pt.up.fe.els2023.config.Config;
import pt.up.fe.els2023.exceptions.TableNotFoundException;
import pt.up.fe.els2023.table.ITable;
import pt.up.fe.els2023.table.Value;

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
            Config config = configReader.readConfig();

            Map<String, ITable> tables = new HashMap<>();

            for (var tableSchema : config.tableSchemas()) {
                tables.put(tableSchema.name(), tableSchema.collect());
            }

            var resultVariables = new HashMap<String, Value>();
            var operations = config.operations();
            for (var pipeline : operations) {
                var btcInterpreter = pipeline.updateBTC(tables, resultVariables);
                if (pipeline.getResult() != null) {
                    tables.put(pipeline.getResult(), btcInterpreter.getBtc().get());
                }
                if (pipeline.getResultVariable() != null) {
                    resultVariables.put(pipeline.getResultVariable(), btcInterpreter.getValueResult());
                }
            }

            config.exporters().forEach((exporter) -> {
                try {
                    exporter.export(tables);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TableNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}