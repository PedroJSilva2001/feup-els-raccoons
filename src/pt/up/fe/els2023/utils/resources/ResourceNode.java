package pt.up.fe.els2023.utils.resources;

import java.util.Iterator;
import java.util.Map;

public interface ResourceNode extends Iterable<ResourceNode> {
    boolean has(String property);

    boolean has(int index);

    boolean isArray();

    boolean isObject();

    boolean isValue();

    boolean isContainer();

    ResourceNode get(String property);

    ResourceNode get(int index);

    Iterator<ResourceNode> iterator();

    Map<String, ResourceNode> getChildren();

    String getNested(String propertyPath);

    String asText();
}
