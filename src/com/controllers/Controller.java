package com.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.parameters.DependancyParameter;
import com.parameters.EnvironmentParameter;
import com.parameters.InternalParameter;
import com.parameters.Model;
import com.parameters.Parameter;
import com.parameters.UndefinedParameter;
import com.ultimate.modelmanager.utils.PrismFileParser;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
	
	// Initialised in Main -> the list containing models and the root window
	private final ObservableList<Model> models;
	private final Stage mainStage;
	
    // The location of the last save
	File saveFile;
	// The name of the project
	String projectName = "untitled";
	
	// Define FXML-linked fields for UI components
    @FXML private MenuItem loadItem;
    @FXML private MenuItem saveItem;
    @FXML private MenuItem saveAsItem;
    @FXML private MenuItem quitItem;

    @FXML private Button addModelButton;
    @FXML private Button upButton;
    @FXML private Button downButton;
    @FXML private Button addEnvironmentParamButton;
    @FXML private Button addDependencyParamButton;
    @FXML private Button addInternalParamButton;

    @FXML private ListView<Model> modelListView;
    @FXML private Label modelDetails;

    @FXML private ListView<EnvironmentParameter> eParamList;
    @FXML private ListView<DependancyParameter> dParamList;
    @FXML private ListView<InternalParameter> iParamList;
    @FXML private ListView<UndefinedParameter> uParamList;
    
    public Controller(Stage mainStage, ObservableList<Model> models) {
    	this.mainStage = mainStage;
    	this.models = models;
    }
    
    // Initialisation method
    @FXML
    private void initialize() {
        
    	// Set up initial behaviour and bindings:
    	setUpMenuItems();
    	setUpButtons();
    	setUpModelListView();
        
    	// TODO: pull these out into separate method/class
        // Behaviour and bindings for eParamList
        eParamList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(EnvironmentParameter item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Create a VBox for the layout
                    VBox vbox = createCellLayout(item);
                    setGraphic(vbox);
                    setText(null);
                }
            }
        });
        
        // Behaviour and bindings for dParamList
        dParamList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(DependancyParameter item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Create a VBox for the layout
                    VBox vbox = createCellLayout(item);
                    setGraphic(vbox);
                    setText(null);
                }
            }
        });
        
        // Behaviour and bindings for iParamList
        iParamList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(InternalParameter item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Create a VBox for the layout
                    VBox vbox = createCellLayout(item);
                    setGraphic(vbox);
                    setText(null);
                }
            }
        });
    }
    
    private void setUpButtons() {
        addModelButton.setOnAction(e -> {
			try {
				handleAddModel(mainStage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        upButton.setOnAction(e -> handleUpButton());
        downButton.setOnAction(e -> handleDownButton());
        addEnvironmentParamButton.setOnAction(e -> handleAddEParam(mainStage, getCurrentModel()));
        addDependencyParamButton.setOnAction(e -> handleAddDParam());
        addInternalParamButton.setOnAction(e -> handleAddIParam());
    }
    
    private void setUpModelListView() {
    	// Behaviour and bindings for model list
    	modelListView.setItems(models);
        modelListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Model item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getModelId());
            }
        });
        // Add a listener to handle selection changes to model list
        modelListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateModelDetails(newValue); // Call method with the newly selected model
            }
        });
    }
    
    private Model getCurrentModel() {
    	return modelListView.getSelectionModel().getSelectedItem();
    }
    
    private void setUpMenuItems() {
        loadItem.setOnAction(e -> handleLoad(mainStage));
        saveItem.setOnAction(e -> handleSave());
        saveAsItem.setOnAction(e -> handleSaveAs());
        quitItem.setOnAction(e -> handleQuit());
    }
    
    private Stage createEditorStage(Stage owner, String stageName) {
    	Stage editorStage = new Stage();
    	editorStage.setTitle(stageName);
    	editorStage.initOwner(owner);
    	return editorStage;
    }
    
    private void handleAddModel(Stage mainStage) throws IOException {
        Stage editorStage = createEditorStage(mainStage, "Add New Model");
        
        // create a AddModelController instance to handle dialog
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ultimatemodelmanager/AddModelDialog.fxml"));
        loader.setController(new AddModelController(mainStage, editorStage, models));
        
        // loading root element of AddModelDialog.fxml
        VBox editorLayout = loader.load();

        // Set up the scene
        Scene scene = new Scene(editorLayout);
        editorStage.setScene(scene);
        editorStage.showAndWait();
    }
    
    private void handleUpButton() {
        // Handle the "Up" button action
        int selectedIndex = modelListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            // Select the item just above the current one
            modelListView.getSelectionModel().select(selectedIndex - 1);
        }
    }
    
    private void handleDownButton() {
        // Handle the "Down" button action
        int selectedIndex = modelListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < models.size() - 1) {
            // Select the item just below the current one
            modelListView.getSelectionModel().select(selectedIndex + 1);
        }
    }
    
    // TODO: re-factor (create file dialog method)
    private void handleLoad(Stage mainStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Models");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(mainStage);

        if (file != null) {
            try {
                // Update title
                projectName = file.getName().replaceAll(".json", "");
                mainStage.setTitle(projectName);
                String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
                JSONObject root = new JSONObject(content);

                models.clear();

                // Get the 'models' object (it's a JSONObject, not a JSONArray)
                JSONObject modelsObject = root.getJSONObject("models");

                // Create a parser instance
                PrismFileParser parser = new PrismFileParser();

                // Iterate over each model in the models object
                modelsObject.keySet().forEach(modelId -> {
                    JSONObject modelJson = modelsObject.getJSONObject(modelId);
                    String id = modelJson.getString("id");
                    String filePath = modelJson.getString("fileName");

                    // Create a new Model object
                    Model model = new Model(id, filePath);

                    // Deserialize parameters if needed
                    JSONObject parametersObject = modelJson.getJSONObject("parameters");

                    // Deserialize dependency parameters
                    JSONObject dependencyObject = parametersObject.optJSONObject("dependency");
                    Set<String> dependencyNames = new HashSet<>();
                    if (dependencyObject != null) {
                        dependencyObject.keySet().forEach(depName -> {
                            JSONObject depObj = dependencyObject.getJSONObject(depName);
                            String depId = depObj.getString("modelId");
                            String depDefinition = depObj.getString("property");
                            model.addDependencyParameter(depName, depId, depDefinition);
                            dependencyNames.add(depName);  // Track names in the dependency category
                        });
                    }

                    // Deserialize environment parameters
                    JSONObject environmentObject = parametersObject.optJSONObject("environment");
                    Set<String> environmentNames = new HashSet<>();
                    if (environmentObject != null) {
                        environmentObject.keySet().forEach(envName -> {
                            JSONObject envObj = environmentObject.getJSONObject(envName);
                            String filePathEnv = envObj.getString("dataFile");
                            String calculation = envObj.getString("type");
                            model.addEnvironmentParameter(envName, filePathEnv, calculation);
                            environmentNames.add(envName);  // Track names in the environment category
                        });
                    }

                    // Deserialize internal parameters
                    JSONObject internalObject = parametersObject.optJSONObject("internal");
                    Set<String> internalNames = new HashSet<>();
                    if (internalObject != null) {
                        internalObject.keySet().forEach(internalName -> {
                            model.addInternalParameter(internalName);
                            internalNames.add(internalName);  // Track names in the internal category
                        });
                    }

                    // Parse the model's file with PrismFileParser
                    List<String> parsedParams = null;
					try {
						parsedParams = parser.parseFile(filePath);
					} catch (IOException e) {
						e.printStackTrace();
					}

                    // Check each parsed parameter
                    for (String parsedParam : parsedParams) {
                        // Extract the parameter name (after the "const <type>" part)
                        String[] parts = parsedParam.split("\\s+");
                        if (parts.length == 3) {
                            String paramName = parts[2];  // This is the <name> part

                            // Check if the name exists in any of the categories
                            if (!dependencyNames.contains(paramName) &&
                                !environmentNames.contains(paramName) &&
                                !internalNames.contains(paramName)) {

                                // Create a new UndefinedParameter and add it to the model
                                UndefinedParameter up = new UndefinedParameter(paramName);
                                model.addUndefinedParameter(up.getName());
                            }
                        }
                    }

                    // Add the constructed model to the list
                    models.add(model);
                });
            } catch (IOException e) {
                showAlert("Error", "Failed to load models: " + e.getMessage());
            }
        }
      handleDownButton(); // so a model is selected after loading
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void handleSave() {
        List<Model> modelList = models;  // Convert ObservableList to List
        if (saveFile != null) {
        	saveModelsToFile(modelList, saveFile.getAbsolutePath());
        }
        else {
        	handleSaveAs();
        }
    }
    
    // TODO: re-factor (create file dialog method)
    private void handleSaveAs() {
        // Convert ObservableList to List
        List<Model> modelList = models;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(mainStage);  // Let user choose file path

        if (file != null) {
            // Update saveFile so it can be reused for future saves
            saveFile = file;
            saveModelsToFile(modelList, file.getAbsolutePath());  // Save the models to the file
        }
    }
    
    private void handleQuit() {
    	mainStage.close();
    }
    
    private void handleAddEParam(Stage mainStage, Model currentModel) {
        Stage editorStage = new Stage();
        editorStage.setTitle("Add New Environment Parameter");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ultimatemodelmanager/AddEParamDialog.fxml"));
        loader.setController(new AddEParamController(editorStage, currentModel));
        
        VBox editorLayout = null;
		try {
			editorLayout = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        // Set up the scene
        Scene scene = new Scene(editorLayout);
        editorStage.setScene(scene);
        editorStage.initOwner(mainStage);
        editorStage.showAndWait();
        
        updateModelDetails(currentModel);
    }
    
    private void handleAddDParam() {
    	// TODO handle adding d parameter
    }
    
    private void handleAddIParam() {
    	// TODO handle adding i parameter
    }
    
    private void updateModelDetails(Model model) {
        // Update the model ID and file path in the Text component
        Label modelIDFile = (Label) mainStage.getScene().lookup("#modelDetails"); // Remove '#' from the ID
        if (modelIDFile != null) {
            modelIDFile.setText("Model ID: " + model.getModelId() + "\nFile Path: " + model.getFilePath());
        }

        // Update the environment parameters list
        ListView<EnvironmentParameter> environmentParamList = (ListView<EnvironmentParameter>) mainStage.getScene().lookup("#eParamList"); 
        if (environmentParamList != null) {
            environmentParamList.getItems().clear(); // Clear existing items
            environmentParamList.getItems().addAll(model.getEnvironmentParameters()); // Add new items from the model
        }

        // Update the dependency parameters list
        ListView<DependancyParameter> dependancyParamList = (ListView<DependancyParameter>) mainStage.getScene().lookup("#dParamList"); 
        if (dependancyParamList != null) {
            dependancyParamList.getItems().clear(); // Clear existing items
            dependancyParamList.getItems().addAll(model.getDependencyParameters()); // Add new items from the model
        }

        // Update the internal parameters list
        ListView<InternalParameter> internalParamList = (ListView<InternalParameter>) mainStage.getScene().lookup("#iParamList"); 
        if (internalParamList != null) {
            internalParamList.getItems().clear(); // Clear existing items
            internalParamList.getItems().addAll(model.getInternalParameters()); // Add new items from the model
        }

        // Update the undefined parameters list
        ListView<UndefinedParameter> undefinedParamList = (ListView<UndefinedParameter>) mainStage.getScene().lookup("#uParamList"); 
        if (undefinedParamList != null) {
            undefinedParamList.getItems().clear(); // Clear existing items
            undefinedParamList.getItems().addAll(model.getUndefinedParameters()); // Add new items from the model
        }
    }
    
    // TODO: re-factor (create file dialog method)
    private void saveModelsToFile(List<Model> models, String filePath) {
        JSONObject root = new JSONObject();
        JSONObject modelsObject = new JSONObject();

        for (Model model : models) {
            JSONObject modelObject = new JSONObject();
            modelObject.put("id", model.getModelId());
            modelObject.put("fileName", model.getFilePath());

            // Parameters object
            JSONObject parametersObject = new JSONObject();

            // Handling dependency parameters
            JSONObject dependencyObject = new JSONObject();
            for (DependancyParameter dep : model.getDependencyParameters()) {
                JSONObject depObj = new JSONObject();
                depObj.put("name", dep.getName());
                depObj.put("modelId", dep.getModelID());
                depObj.put("property", dep.getDefinition());
                dependencyObject.put(dep.getName(), depObj);  // Add dependency parameters to the object
            }
            parametersObject.put("dependency", dependencyObject);

            // Handling environment parameters
            JSONObject environmentObject = new JSONObject();
            for (EnvironmentParameter env : model.getEnvironmentParameters()) {
                JSONObject envObj = new JSONObject();
                envObj.put("name", env.getName());
                envObj.put("type", env.getCalculation());
                envObj.put("dataFile", env.getFilePath());
                environmentObject.put(env.getName(), envObj);  // Add environment parameters
            }
            parametersObject.put("environment", environmentObject);

            // Handling internal parameters (only name should be included)
            JSONObject internalObject = new JSONObject();
            for (InternalParameter internal : model.getInternalParameters()) {
                JSONObject internalObj = new JSONObject();
                internalObj.put("name", internal.getName());  // Only name for internal parameters
                internalObject.put(internal.getName(), internalObj);  // Add internal parameters
            }
            parametersObject.put("internal", internalObject);

            // Add the parameters to the model object
            modelObject.put("parameters", parametersObject);

            // Add model to the modelsObject
            modelsObject.put(model.getModelId(), modelObject);
        }

        // Wrap everything under "models"
        root.put("models", modelsObject);

        // Save the JSON to file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(root.toString(4));  // Pretty print with indentation
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private VBox createCellLayout(Parameter item) {
        // Parse the item into fields
        String[] fields = item.toString().split(",");
        String f1 = fields.length > 0 ? fields[0] : "";
        String f2 = fields.length > 1 ? fields[1] : "";
        String f3 = fields.length > 2 ? fields[2] : "";

        // Create a VBox for the overall layout
        VBox vbox = new VBox(5);

        // Create HBox for f1 with buttons
        HBox f1Box = createHBoxForF1(f1, item);

        // Create a VBox for f2 and f3 with indentation and bullets
        VBox subParamsBox = createSubParamsBox(f2, f3);

        // Add the HBox and VBox to the main VBox
        vbox.getChildren().addAll(f1Box, subParamsBox);
        return vbox;
    	
    }
    
    private HBox createHBoxForF1(String f1, Parameter item) {
        HBox f1Box = new HBox(10);
        Label f1Label = new Label(f1);
        f1Label.setStyle("-fx-font-weight: bold;"); // Make f1 bold
        Button minusButton = new Button("-");
        Button editButton = new Button("Edit");

        // Add event handlers for the buttons
        minusButton.setOnAction(e -> handleRemoveItem(item));
        editButton.setOnAction(e -> handleEditItem(item));

        f1Box.getChildren().addAll(f1Label, minusButton, editButton);
        return f1Box;
    }

    // Helper method to create VBox for f2 and f3
    private VBox createSubParamsBox(String f2, String f3) {
        VBox subParamsBox = new VBox(5);
        subParamsBox.setPadding(new Insets(0, 0, 0, 20)); // Indent the sub-parameters
        Label f2Label = new Label("• " + f2);
        Label f3Label = new Label("• " + f3);
        subParamsBox.getChildren().addAll(f2Label, f3Label);
        return subParamsBox;
    }

    private void handleRemoveItem(Parameter item) {
        // TODO: Implement action handler for the remove button
    	if (item instanceof EnvironmentParameter) {
    		String name = item.getName();
    		getCurrentModel().removeEnvironmentParamter((EnvironmentParameter) item);
    		getCurrentModel().addUndefinedParameter(name);
    	}
    	updateModelDetails(getCurrentModel());
    }

    private void handleEditItem(Parameter item) {
        // TODO: Implement action handler for the edit button
        Stage editorStage = new Stage();
        editorStage.setTitle("Edit Environment Parameter");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ultimatemodelmanager/EditEPAramDialog.fxml"));
        loader.setController(new EditEParamController(editorStage, getCurrentModel(), (EnvironmentParameter) item));
        
        VBox editorLayout = null;
		try {
			editorLayout = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        // Set up the scene
        Scene scene = new Scene(editorLayout);
        editorStage.setScene(scene);
        editorStage.initOwner(mainStage);
        editorStage.showAndWait();
        
        updateModelDetails(getCurrentModel());
    }
}
// TODO task list
/**
 * Highlight models green/red depending on whether all parameters defined
 * Implement keyboard shortcuts for deleting models
 * Implement adding parameters from drop-down list of undefined parameters
 * Hide/showing undefined parameters section when empty/non-empty
 * Ensure window cannot be made too small to obscure the GUI
 * Do not allow adding of undefined when list empty (remove "+" buttons?)
 * prevent interaction with main window when dialog open
 */