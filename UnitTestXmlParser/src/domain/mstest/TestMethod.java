package domain.mstest;

import xmlcomponents.autoparse.annotation.XmlProperty;

public class TestMethod {
    @XmlProperty(valueName = "codeBase")
    private String codebase;
    private String adapterTypeName;
    private String className;
    private String name;
}
