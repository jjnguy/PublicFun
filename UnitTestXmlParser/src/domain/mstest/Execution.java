package domain.mstest;

import xmlcomponents.autoparse.annotation.XmlProperty;

public class Execution {
    @XmlProperty(optional = true)
    private long timeOut;
    @XmlProperty(optional = true)
    private String id;
}
