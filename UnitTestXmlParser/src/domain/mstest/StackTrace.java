package domain.mstest;

import xmlcomponents.autoparse.annotation.XmlProperty;
import xmlcomponents.autoparse.annotation.XmlProperty.XmlPropertyType;

public class StackTrace {
    @XmlProperty(type=XmlPropertyType.TEXT_NODE)
    private String value;
}
