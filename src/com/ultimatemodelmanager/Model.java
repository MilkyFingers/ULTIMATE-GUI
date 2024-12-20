package com.ultimatemodelmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a model in the system.
 * <p>
 * This class stores information about a model, including its unique ID, file path,
 * and various types of parameters associated with the model (e.g., dependency parameters, environment parameters).
 * </p>
 */
public class Model {
    private String modelId; // Unique identifier for the model
    private String filePath; // Path to the model's file
    private List<DependancyParameter> dependencyParameters; // List of dependency parameters
    private List<EnvironmentParameter> environmentParameters; // List of environment parameters
    private List<InternalParameter> internalParameters; // List of internal parameters
    private List<UndefinedParameter> undefinedParameters; // List of undefined parameters

    /**
     * Constructor to initialize a new Model object.
     *
     * @param modelId the unique identifier for the model
     * @param filePath the file path of the model
     */
    public Model(String modelId, String filePath) {
        this.modelId = modelId;
        this.filePath = filePath;
        this.dependencyParameters = new ArrayList<>();
        this.environmentParameters = new ArrayList<>();
        this.internalParameters = new ArrayList<>();
        this.undefinedParameters = new ArrayList<>();
    }

    // Methods to add various types of parameters to the model

    /**
     * Adds a dependency parameter to the model.
     * 
     * @param name the name of the dependency parameter
     * @param modelId the model ID associated with the dependency
     * @param definition the definition of the dependency parameter
     */
    public void addDependencyParameter(String name, String modelId, String definition) {
        dependencyParameters.add(new DependancyParameter(name, modelId, definition));
    }

    /**
     * Adds an environment parameter to the model.
     * 
     * @param name the name of the environment parameter
     * @param filePath the file path associated with the environment parameter
     * @param methodName the method name related to the environment parameter
     */
    public void addEnvironmentParameter(String name, String filePath, String methodName) {
        environmentParameters.add(new EnvironmentParameter(name, filePath, methodName));
    }

    /**
     * Adds an internal parameter to the model.
     * 
     * @param name the name of the internal parameter
     */
    public void addInternalParameter(String name) {
        internalParameters.add(new InternalParameter(name));
    }
    
    /**
     * Adds an undefined parameter to the model.
     * 
     * @param param the name of the undefined parameter
     */
    public void addUndefinedParameter(String param) {
        undefinedParameters.add(new UndefinedParameter(param));
    }

    // Getter and setter methods for model ID and file path

    /**
     * Gets the model's unique identifier.
     * 
     * @return the model ID
     */
    public String getModelId() {
        return this.modelId;
    }

    /**
     * Gets the model's file path.
     * 
     * @return the file path
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Sets the model's unique identifier.
     * 
     * @param id the new model ID
     */
    public void setModelId(String id) {
        this.modelId = id;
    }

    /**
     * Sets the model's file path.
     * 
     * @param filePath2 the new file path
     */
    public void setFilePath(String filePath2) {
        this.filePath = filePath2;
    }

    // Getter methods for parameter lists

    /**
     * Gets the list of dependency parameters associated with the model.
     * 
     * @return the list of dependency parameters
     */
    public List<DependancyParameter> getDependencyParameters() {
        return dependencyParameters;
    }

    /**
     * Gets the list of environment parameters associated with the model.
     * 
     * @return the list of environment parameters
     */
    public List<EnvironmentParameter> getEnvironmentParameters() {
        return environmentParameters;
    }

    /**
     * Gets the list of internal parameters associated with the model.
     * 
     * @return the list of internal parameters
     */
    public List<InternalParameter> getInternalParameters() {
        return internalParameters;
    }
    
    /**
     * Gets the list of undefined parameters associated with the model.
     * 
     * @return the list of undefined parameters
     */
    public List<UndefinedParameter> getUndefinedParameters() {
        return undefinedParameters;
    }
    
    /**
     * Removes the given <UndefinedParamter> from the models List
     * 
     * @param the <UndefinedParameter> to be removed
     */
    public void removeUndefinedParamter(UndefinedParameter toRemove) {
    	undefinedParameters.remove(toRemove);
    }
}