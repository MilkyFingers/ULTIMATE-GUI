package com.ultimatemodelmanager;

public class UndefinedParameter {
	private String parameter;
	
	public UndefinedParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public String getParameter() {
		return this.parameter;
	}
	
	public String toString() {
		return getParameter();
	}
}