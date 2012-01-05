package domain.junit;

import xmlcomponents.autoparse.annotation.XmlProperty;

public class testcase {

	private String classname;

	public String name;

	private double time;

	@XmlProperty(optional=true)
	private failure failure;
	
	public boolean failed(){
	   return failure != null;
	}
}
