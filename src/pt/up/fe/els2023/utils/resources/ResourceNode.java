package pt.up.fe.els2023.utils.resources;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public interface ResourceNode extends Iterable<ResourceNode> {
    String getName();

    boolean has(String property);

    boolean has(int index);

    boolean isArray();

    boolean isNull();

    boolean isBoolean();

    boolean isInteger();

    boolean isDouble();

    boolean isText();

    boolean isLong();

    boolean isBigDecimal();

    boolean isBigInteger();

    boolean isObject();

    boolean isValue();

    boolean isContainer();

    ResourceNode get(String property);

    ResourceNode get(int index);

    Iterator<ResourceNode> iterator();

    List<ResourceNode> getChildren();

    String getNested(String propertyPath);

    Boolean asBoolean();

    Long asLong();

    BigDecimal asBigDecimal();

    BigInteger asBigInteger();

    Double asDouble();

    String asText();
}
