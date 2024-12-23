package com.parameters;

public class UndefinedParameter extends Parameter {
	private String parameter;
	
	public UndefinedParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public String getType() {
		return "u";
	}
	public String getName() {
		return this.parameter;
	}
	
	public String toString() {
		return getName();
	}
}