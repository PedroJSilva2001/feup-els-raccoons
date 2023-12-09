package pt.up.fe.els2023.interpreter.symboltable;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import pt.up.fe.els2023.export.*;
import pt.up.fe.els2023.interpreter.diagnostic.Diagnostic;
import pt.up.fe.els2023.model.schema.*;
import pt.up.fe.els2023.racoons.ExporterValue;
import pt.up.fe.els2023.racoons.impl.*;
import pt.up.fe.els2023.sources.JsonSource;
import pt.up.fe.els2023.sources.XmlSource;
import pt.up.fe.els2023.sources.YamlSource;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

import java.util.*;

public class SymbolTableFiller {

    private SymbolTable symbolTable;

    private List<Diagnostic> warnings;

    private List<Diagnostic> errors;


    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<Diagnostic> getErrors() {
        return errors;
    }

    public void fill(EObject root, String racoonsConfigFilename) {
        this.symbolTable = new RacoonsSymbolTable(racoonsConfigFilename);
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();

        fillVersion(root);
        fillStatementSymbols(root);
    }

    private void fillVersion(EObject root) {
        var version = ((RacoonsImpl)root).getVersion();

        symbolTable.addVersion(version.getCode());
    }

    private void fillStatementSymbols(EObject root) {
        var statements = ((RacoonsImpl)root).getStatements();

        FunctionClassMap<EObject, Void> map = new FunctionClassMap<>();

        map.put(SourceDeclImpl.class, this::fillSourceDecl);
        map.put(NftDeclImpl.class, this::fillNftDecl);
        map.put(ExporterDeclImpl.class, this::fillExporterDecl);
        map.put(ExpressionImpl.class, a -> null); // TODO ignore for now
        map.put(AssignmentImpl.class, a -> null); // TODO ignore for now

        for (var statement : statements) {
            map.apply(statement);
        }
    }

    private Void fillSourceDecl(SourceDeclImpl sourceDecl) {
        var name = sourceDecl.getName();
        var paths = sourceDecl.getPathList();

        if (symbolTable.hasSource(name)) {
            errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    "Source with name '" + name + "' already exists"));
            return null;
        }

        var path = paths.get(0);

        var extension = path.substring(path.lastIndexOf('.') + 1);

        var source = switch (extension) {
            case "yaml" -> new YamlSource(name, paths);
            case "json" -> new JsonSource(name, paths);
            case "xml" ->  new XmlSource(name, paths);
            default -> {
                errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        "Could not determine source type for paths of source '" + name + "'"));
                yield null;
            }
        };

        symbolTable.addSource(name, source);

        return null;
    }

    private Void fillNftDecl(NftDeclImpl nftDecl) {
        // TODO
        var name = nftDecl.getName();
        var sourceName = nftDecl.getSource().getName();

        if (symbolTable.hasTableSchema(name)) {
            errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(nftDecl).getStartLine(), -1,
                    "Table schema with name '" + name + "' already exists"));
            return null;
        }

        if (sourceName == null) {
            errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(nftDecl).getStartLine(), -1,
                    "Source specified in table schema '" + name + "' does not exist"));
            return null;
        }

        var nftNodes = nftDecl.getNft().getNodeList();

        var schema = new TableSchema(name)
                .source(symbolTable.getSource(sourceName));

        fillNftNodes(schema, nftNodes);

        return null;
    }

    private void fillNftNodes(TableSchema schema, EList<pt.up.fe.els2023.racoons.SchemaNode> nftNodes) {
        var nodes = new ArrayList<SchemaNode>();

        for (var nftNode : nftNodes) {
            var schemaNode = fillSchemaNode((SchemaNodeImpl) nftNode);

            if (schemaNode != null) {
                nodes.add(schemaNode);
            }
        }
        // todo diagnostics

        symbolTable.addTableSchema(schema.name(), schema.nft(nodes));
    }

    private SchemaNode fillSchemaNode(SchemaNodeImpl schemaNode) {
        FunctionClassMap<EObject, SchemaNode> map = new FunctionClassMap<>();

        map.put(EachNodeImpl.class, this::fillEachNode);
        map.put(ListNodeImpl.class, this::fillListNode);
        map.put(PropertyNodeImpl.class, this::fillPropertyNode);
        map.put(ResourceLocationNodeImpl.class, this::fillResourceLocationNode);
        map.put(AllNodeImpl.class, this::fillAllNode);
        map.put(AllValuesNodeImpl.class, this::fillAllValuesNode);
        map.put(AllContainersNodeImpl.class, this::fillAllContainersNode);
        map.put(ExceptNodeImpl.class, this::fillExceptNode);
        map.put(IndexNodeImpl.class, this::fillIndexNode);

        return map.apply(schemaNode);
    }

    private SchemaNode fillAllNode(AllNodeImpl allNode) {
        var formatNode = allNode.getFormatNode();
        return formatNode == null? new AllNode() : new AllNode(formatNode.getName());
    }

    private SchemaNode fillAllValuesNode(AllValuesNodeImpl allValuesNode) {
        var formatNode = allValuesNode.getFormatNode();

        return formatNode == null? new AllValueNode() : new AllValueNode(formatNode.getName());
    }

    private SchemaNode fillAllContainersNode(AllContainersNodeImpl allContainersNode) {
        return new AllContainerNode();
    }

    private SchemaNode fillExceptNode(ExceptNodeImpl exceptNode) {
        var formatNode = exceptNode.getFormatNode();

        var exceptColumns = new HashSet<>(exceptNode.getElement().getNodeList());

        if (formatNode == null) {
            return new ExceptNode(exceptColumns);
        }

        if (!formatNode.getName().contains("%s")) {
            warnings.add(Diagnostic.warning(symbolTable.getRacoonsConfigFilename(),
                    NodeModelUtils.getNode(formatNode).getStartLine(), -1,
                    "Format string in Except node does not contain '%s'. All columns in the Except node will be ignored"));
        }

        return new ExceptNode(exceptColumns, formatNode.getName());
    }

    private SchemaNode fillResourceLocationNode(ResourceLocationNodeImpl resourceLocationNode) {
        var resourceNodeType = resourceLocationNode.getResourceNode();
        var columnName = resourceLocationNode.getElement() == null? null : resourceLocationNode.getElement().getName();

        return switch (resourceNodeType) {
            case "file" -> columnName == null? new FileNode() : new FileNode(columnName);
            case "filePath" -> columnName == null? new PathNode() : new PathNode(columnName);
            case "directory" -> columnName == null? new DirectoryNode() : new DirectoryNode(columnName);
            default -> throw new IllegalStateException("Unexpected value: " + resourceNodeType);
        };
    }

    private SchemaNode fillPropertyNode(PropertyNodeImpl propertyNode) {
        var name = propertyNode.getName();
        var columnOrNodeElement = propertyNode.getElement();

        if (columnOrNodeElement == null) {
            return new PropertyNode(name, new NullNode());
        }

        FunctionClassMap<EObject, SchemaNode> map = new FunctionClassMap<>();

        map.put(ColumnNodeImpl.class, this::fillColumnNode);
        map.put(SchemaNodeImpl.class, this::fillSchemaNode);

        var modelNode = map.apply(columnOrNodeElement.getNode());

        return new PropertyNode(name, modelNode);
    }

    private SchemaNode fillColumnNode(ColumnNodeImpl columnNode) {
        var columnName = columnNode.getName();

        return new ColumnNode(columnName);
    }

    private SchemaNode fillIndexNode(IndexNodeImpl indexNode) {
        var index = Integer.parseInt(indexNode.getIndex());
        var columnOrNodeElement = indexNode.getElement();

        if (columnOrNodeElement == null) {
            return new IndexNode(index, new NullNode());
        }

        FunctionClassMap<EObject, SchemaNode> map = new FunctionClassMap<>();

        map.put(ColumnNodeImpl.class, this::fillColumnNode);
        map.put(SchemaNodeImpl.class, this::fillSchemaNode);

        var modelNode = map.apply(columnOrNodeElement.getNode());

        return new IndexNode(index, modelNode);
    }

    private SchemaNode fillListNode(ListNodeImpl listNode) {
        FunctionClassMap<EObject, SchemaNode> map = new FunctionClassMap<>();

        map.put(EachNodeImpl.class, this::fillEachNode);
        map.put(ListNodeImpl.class, this::fillListNode);
        map.put(PropertyNodeImpl.class, this::fillPropertyNode);
        map.put(ResourceLocationNodeImpl.class, this::fillResourceLocationNode);
        map.put(AllNodeImpl.class, this::fillAllNode);
        map.put(AllValuesNodeImpl.class, this::fillAllValuesNode);
        map.put(AllContainersNodeImpl.class, this::fillAllContainersNode);
        map.put(ExceptNodeImpl.class, this::fillExceptNode);
        map.put(IndexNodeImpl.class, this::fillIndexNode);

        var modelNodes = new ArrayList<SchemaNode>();

        for (var listElement : listNode.getNodeList()) {
            var schemaNode = map.apply(listElement);

            if (schemaNode != null) {
                modelNodes.add(schemaNode);
            }
        }

        return new ListNode(modelNodes);
    }

    private SchemaNode fillEachNode(EachNodeImpl eachNode) {
        var columnOrNode = eachNode.getElement();

        if (columnOrNode == null) {
            return new EachNode(new NullNode());
        }

        FunctionClassMap<EObject, SchemaNode> map = new FunctionClassMap<>();

        map.put(SchemaNodeImpl.class, this::fillSchemaNode);
        map.put(ColumnNodeImpl.class, this::fillColumnNode);

        var modelNode = map.apply(columnOrNode.getNode());

        return new EachNode(modelNode);
    }

    private Void fillExporterDecl(ExporterDeclImpl exporterDecl) {
        var name = exporterDecl.getName();
        var type = exporterDecl.getType();

        var attrNodes = exporterDecl.getExporterAttrs();

        if (symbolTable.hasExporter(name)) {
            errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                    "Exporter with name '" + name + "' already exists"));
            return null;
        }

        var exporterAttrs = new HashMap<String, ExporterValue>();

        for (var attrNode : attrNodes) {
            var attrName = attrNode.getAttr();
            var attrValueNode = attrNode.getValue();

            exporterAttrs.put(attrName, attrValueNode);
        }

        var exporter = switch (type) {
            case "csv" -> {
                var supportedAttrs = CsvExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new CsvExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("separator")) {
                    builder.setSeparator(exporterAttrs.get("separator").getValue());
                }

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            case "html" -> {
                var supportedAttrs = HtmlExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new HtmlExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("style")) {
                    builder.setStyle(exporterAttrs.get("style").getValue());
                }

                if (exporterAttrs.containsKey("title")) {
                    builder.setTitle(exporterAttrs.get("title").getValue());
                }

                if (exporterAttrs.containsKey("exportFullHtml")) {
                    builder.setExportFullHtml(exporterAttrs.get("exportFullHtml").getValue().equals("true")); // since it's a boolean, only true or false are valid values from the grammar
                }

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            case "tsv" -> {
                var supportedAttrs = TsvExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new TsvExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            case "latex" -> {
                var supportedAttrs = LatexExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new LatexExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            case "markdown" -> {
                var supportedAttrs = MarkdownExporter.getSupportedAttributes();

                if (checkExporterAttributes(name, type, exporterAttrs, supportedAttrs)) {
                    yield null;
                }

                var builder = new MarkdownExporterBuilder(exporterAttrs.get("filename").getValue(),
                        exporterAttrs.get("path").getValue());

                if (exporterAttrs.containsKey("endOfLine")) {
                    builder.setEndOfLine(exporterAttrs.get("endOfLine").getValue());
                }

                yield builder.build();
            }

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        return null;
    }


    // returns true if there are errors
    private boolean checkExporterAttributes(String name,
                                            String exporterType,
                                            Map<String, ExporterValue> exporterAttrs,
                                            Map<String, TableExporter.AttributeValue> supportedAttrs) {
        for (var specifiedAttr : exporterAttrs.keySet()) {
            if (!supportedAttrs.containsKey(specifiedAttr)) {
                errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(), NodeModelUtils.getNode(exporterAttrs.get(specifiedAttr)).getStartLine(), -1, exporterType + " Exporter '" + name + "' does not support attribute '" + specifiedAttr + "'"));
            }
        }

        if (!errors.isEmpty()) {
            return true;
        }

        for (var supportedAttr : supportedAttrs.keySet()) {
            if (!exporterAttrs.containsKey(supportedAttr) && supportedAttrs.get(supportedAttr).required()) {
                errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(), exporterType
                        + " Exporter '" + name + "' does not have mandatory attribute '" + supportedAttr + "'"));
            }
        }

        if (!errors.isEmpty()) {
            return true;
        }

        for (var specifiedAttr : exporterAttrs.keySet()) {
            var attrValueNode = exporterAttrs.get(specifiedAttr);
            var supportedAttr = supportedAttrs.get(specifiedAttr);

            if (!attrValueNode.eClass().getName().equalsIgnoreCase(supportedAttr.type().toString())) {
                errors.add(Diagnostic.error(symbolTable.getRacoonsConfigFilename(),
                        NodeModelUtils.getNode(attrValueNode).getStartLine(), -1,
                        exporterType + " Exporter '" + name + "' attribute '" + specifiedAttr +
                                "' has type '" + attrValueNode.eClass().getName() + "' but should have type '"
                                + StringUtils.capitalize(supportedAttr.type().toString().toLowerCase()) + "'"));
            }
        }

        return !errors.isEmpty();
    }
}
