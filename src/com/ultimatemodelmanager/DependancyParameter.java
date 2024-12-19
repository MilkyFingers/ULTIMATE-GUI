package com.ultimatemodelmanager;

public class DependancyParameter {
    private String name;
    private String modelID;
    private String definition;
    
    public DependancyParameter(String name, String modelID, String definition) {
        this.name = name;
        this.modelID = modelID;
        this.definition = definition;
    }
    
    // GETTER METHODS
    
    public String getName() {
    	return this.name;	
    }
    
    public String getModelID() {
    	return this.modelID;
    }
    
    public String getDefinition() {
    	return this.definition;
    }
    
    // SETTER METHODS
    
    public void setName(String newName) {
    	this.name = newName;
    }
    
    public void setModelID(String newModelID) {
    	this.modelID = newModelID;
    }
    
    public void setDefinition(String newDefinition) {
    	this.definition = newDefinition;
    }
    
    public String toString() {
    	return getName() + ", " + getModelID() + ", " + getDefinition();
    }
}
