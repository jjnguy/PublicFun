package domain.mstest;

import xmlcomponents.autoparse.annotation.XmlProperty;
import xmlcomponents.autoparse.annotation.XmlProperty.XmlPropertyType;

public class Message {

    @XmlProperty(type=XmlPropertyType.TEXT_NODE)
    private String value;
    
}
