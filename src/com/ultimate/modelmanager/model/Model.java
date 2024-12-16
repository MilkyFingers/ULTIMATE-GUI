package com.ultimate.modelmanager.model;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private String modelId;
    private String filePath;
    private List<DependancyParameter> dependencyParameters;
    private List<EnvironmentParameter> environmentParameters;
    private List<InternalParameter> internalParameters;
    private List<UndefinedParameter> undefinedParameters;

    public Model(String modelId, String filePath) {
        this.modelId = modelId;
        this.filePath = filePath;
        this.dependencyParameters = new ArrayList<>();
        this.environmentParameters = new ArrayList<>();
        this.internalParameters = new ArrayList<>();
        this.undefinedParameters = new ArrayList<>();
    }

    // Methods to add parameters
    public void addDependencyParameter(String name, String modelId, String definition) {
        dependencyParameters.add(new DependancyParameter(name, modelId, definition));
    }

    public void addEnvironmentParameter(String name, String filePath, String methodName) {
        environmentParameters.add(new EnvironmentParameter(name, filePath, methodName));
    }

    public void addInternalParameter(String name) {
        internalParameters.add(new InternalParameter(name));
    }
    
    public void addUndefinedParameter(String param) {
    	undefinedParameters.add(new UndefinedParameter(param));
    }

    // Getters for the model ID and file path
    public String getModelId() {
        return this.modelId;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setModelId(String id) {
        this.modelId = id;
    }

    public void setFilePath(String filePath2) {
        this.filePath = filePath2;
    }

    // Getter methods for parameter lists
    public List<DependancyParameter> getDependencyParameters() {
        return dependencyParameters;
    }

    public List<EnvironmentParameter> getEnvironmentParameters() {
        return environmentParameters;
    }

    public List<InternalParameter> getInternalParameters() {
        return internalParameters;
    }
    
    public List<UndefinedParameter> getUndefinedParameters() {
    	return undefinedParameters;
    }
}


