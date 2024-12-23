package com.parameters;

public class InternalParameter extends Parameter {
	private String name;
	
	public InternalParameter(String name) {
		this.name = name;
	}
	
    // GETTER METHODS
    
	public String getType() {
		return "i";
	}
	
    public String getName() {
    	return this.name;	
    }
    
    // SETTER METHODS
    
    public void setName(String newName) {
    	this.name = newName;
    }
    public String toString() {
    	return getName();
    }
}
