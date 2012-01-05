package domain.junit;

import xmlcomponents.autoparse.annotation.XmlProperty;
import xmlcomponents.autoparse.annotation.XmlProperty.XmlPropertyType;

public class error {
   @XmlProperty(type = XmlPropertyType.TEXT_NODE)
   private String text;
}
