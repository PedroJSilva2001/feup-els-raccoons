# ELS Project - Racoons Domain-Specific Language
Authors:
- Gustavo Santos [up201907397](mailto:up201907397@edu.fe.up.pt)
- Nuno Alves [up201908250](mailto:up201908250@edu.fe.up.pt)
- Pedro Silva [up201907523](mailto:up201907523@edu.fe.up.pt)

Racoons is a domain-specific language for the creation of tables. It allows for the creation of tables with a simple syntax, that can be exported to different formats, such as HTML, LaTeX, and CSV.

## Language Design
### Inspiration
We took inspiration from the pandas package for Python and from SQL. Especially for our operations:
- Table join.
- Table creation.
- Column creation.
- Column deletion.

### Table Creation
- We created the concept of "source" which is a batch of structured files of the same type (i.e JSON, YAML, XML, CSV and TSV).
- A table can be populated with only one source.
    - In the future the source will be optional (the user might want just an empty table).
- The user can specify one or more sources in a single configuration file. 
    - They are declared in the corresponding "sources" section at the beginning of the config file.
    - This way multiple tables can be populated.
- The user specifies the schemas of all tables he wants to use.
    - A table schema corresponds to the schema of each column it is composed of.
    - Specifying a column schema involves declaring the column name and the property of the source files where to get the values for that column.
    - If the column name is not specified, we adopt the source property's name.
    - If the source property is not specified in a column schema, we create an empty column.
    - A source file missing the property specified in the column schema results in a null value for the corresponding column.
    - The first column of a table contains the filenames of which the entries originated.
- The declaration order of column schemas determines column order in the table.
- Exports have their own section on the configuration file.
    - In the future they will be an operation to allow exporting at any given time, not just in the end.
    - The user can export to HTML, LaTeX, CSV, TSV, and Markdown.
    - Exports have optional parameters based on the format type. For example, all formats have an optional line ending parameter, and the CSV format has a separator parameter.
- Operation order is taken into account.

## Semantic Model
### Table Modelling
![Table Modelling](docs/table_model.svg)

### Source Modelling
![Source Modelling](docs/source_model.svg)

### Exporter Modelling
![Exporter Modelling](docs/export_model.svg)

### Operation Modelling
![Operation Modelling](docs/operation_model.svg)

### Interpreter Modelling
![Interpreter Modelling](docs/interpreter_model.svg)

## Configuration File
### High-level Structure
```yaml
sources:
      ...
    
tables:
      ...

operations:
      ...

exports:
      ...
```

### Sources
```yaml
sources:
  - name: decision_tree
    type: yaml
    path:
      - "files/yaml/decision_tree_1.yaml"
      - "files/yaml/decision_tree_2.yaml"
      - "files/yaml/decision_tree_3.yaml"
```

### Tables
```yaml
tables:
  - name: decision_tree
    source: "decision_tree"
    columns:
      - name: Criterion
        from: params.criterion
      - from: "params.min_samples_split"
      - name: "Zau 2"
  - name: table2
    source: "file2"
    columns:
      - name: "Zau master"
        from: "prop1.prop2"
      - from: "prop3"
```

### Operations
```yaml
operations:
  - operation: dropColumns
    table: table1
    columns:
      - "Zau"
```

### Exports
```yaml
export:
  - name: decision_tree
    path: "files/"
    filename: "decision_tree.html"
    exportFullHtml: true
    format: html
```

## What's Next
- Implementing the operations.
    - Table joins (i.e inner join).
    - Table mutation (i.e adding and deleting columns/rows).
    - Table selection, grouping, and sorting.
    - Apply (arbitrary) functions to a column (i.e multiplication, or division by a scalar).
- Aggregate statistics (i.e max, sum, count, ...).
- Tracking and specifying the data types in the columns.
- The user will be able to specify the name of the File column using a special keyword.
- Finishing the sources (CSV and XML).
- Add more customization to exporters.
- We want to replace Yaml config files with a syntax of our own.
    - Introduce specific keywords (i.e "join", "deleteCol", etc).
    - Introduce the pipeline operator |> for easier table processing.
- Continuing with a declarative paradigm (SQL inspiration), and even possibly a functional one.
- Pattern matching for source files, such as using wildcard operators or even RegEx.
- Specify folders instead of specific files when declaring a source.
- Possibly creating a runtime.

## Project Instructions

For this project, you need to [install Gradle](https://gradle.org/install/)

### Project setup

Copy your source files to the ``src`` folder, and your JUnit test files to the ``test`` folder.

### Compile and Running

To compile and install the program, run ``gradle installDist``. This will compile your classes and create a launcher script in the folder ``build/install/els2023-2/bin``. For convenience, there are two script files, one for Windows (``els2023-2.bat``) and another for Linux (``els2023-2``), in the root of the repository, that call these scripts.

After compilation, tests will be automatically executed, if any test fails, the build stops. If you want to ignore the tests and build the program even if some tests fail, execute Gradle with flags "-x test".

When creating a Java executable, it is necessary to specify which class that contains a ``main()`` method should be entry point of the application. This can be configured in the Gradle script with the property ``mainClassName``, which by default has the value ``pt.up.fe.els2023.Main``.

### Test

To test the program, run ``gradle test``. This will execute the build, and run the JUnit tests in the ``test`` folder. If you want to see output printed during the tests, use the flag ``-i`` (i.e., ``gradle test -i``).
You can also see a test report by opening ``build/reports/tests/test/index.html``.

