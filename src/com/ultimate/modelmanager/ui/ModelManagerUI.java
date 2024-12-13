package com.ultimate.modelmanager.ui;

import com.ultimate.modelmanager.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ModelManagerUI extends Application {
	// This will store the models during the user session
    private final ObservableList<Model> models = FXCollections.observableArrayList();
    // Used for the save function
    File saveFile = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ULTIMATE Model Manager");

        // Layouts
        BorderPane root = new BorderPane();

        // Text box to display model information
        Text modelDetails = new Text();
        root.setCenter(modelDetails);

        // ListView to display models
        ListView<Model> modelListView = new ListView<>(models);
        modelListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Model item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getModelId());
            }
        });
        
        // Add a listener to handle selection changes
        modelListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                modelDetails.setText(updateModelDetails(newValue)); // Call your method with the newly selected item
            }
        });
        
        // VBox for buttons and list view
        VBox vbox = new VBox(10); // 10-pixel spacing between elements
        vbox.setStyle("-fx-border-width: 2px; -fx-padding: 5px;");

        // Create an HBox for the buttons (arrange horizontally)
        HBox hbox = new HBox(10); // 10-pixel spacing between buttons
        // Centre the buttons horizontally
        hbox.setAlignment(Pos.CENTER);
        // Apply a border to the HBox
        hbox.setStyle("-fx-border-width: 2px; -fx-padding: 5px;");

        // Buttons
        Button addModelButton = new Button("+");
        Button upButton = new Button("↑");
        Button downButton = new Button("↓");

        // Add event handlers for the buttons
        addModelButton.setOnAction(e -> openModelEditor(primaryStage, null));

        upButton.setOnAction(e -> {
            // Handle the "Up" button action
            int selectedIndex = modelListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex > 0) {
                // Select the item just above the current one
                modelListView.getSelectionModel().select(selectedIndex - 1);
            }
        });

        downButton.setOnAction(e -> {
            // Handle the "Down" button action
            int selectedIndex = modelListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex < models.size() - 1) {
                // Select the item just below the current one
                modelListView.getSelectionModel().select(selectedIndex + 1);
            }
        });

        // Add buttons to the HBox
        hbox.getChildren().addAll(addModelButton, upButton, downButton);

        // Add the HBox for buttons and the ListView to the VBox
        vbox.getChildren().addAll(hbox, modelListView);

        // Ensure the ListView fills the remaining vertical space in the VBox
        VBox.setVgrow(modelListView, Priority.ALWAYS);

        // Set the VBox to the left side of the BorderPane
        root.setLeft(vbox);

        
        // Menu Bar
        MenuBar menuBar = new MenuBar();

        // "File" Menu
        Menu fileMenu = new Menu("File");
        MenuItem loadItem = new MenuItem("Load");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem quitItem = new MenuItem("Quit");
        fileMenu.getItems().addAll(loadItem, saveItem, saveAsItem, quitItem);

        // Add menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu);

        // Set the menu bar at the top of the window
        root.setTop(menuBar);

        /**
        // Button actions
        addModelButton.setOnAction(e -> openModelEditor(primaryStage, null));

        editModelButton.setOnAction(e -> {
            Model selectedModel = modelListView.getSelectionModel().getSelectedItem();
            if (selectedModel != null) {
                openModelEditor(primaryStage, selectedModel);
            } else {
                showAlert("No Selection", "Please select a model to edit.");
            }
        });

        deleteModelButton.setOnAction(e -> {
            Model selectedModel = modelListView.getSelectionModel().getSelectedItem();
            if (selectedModel != null) {
                models.remove(selectedModel);
            } else {
                showAlert("No Selection", "Please select a model to delete.");
            }
        });
        
                // **View Model Details Button Action**
        viewModelDetailsButton.setOnAction(e -> {
            Model selectedModel = modelListView.getSelectionModel().getSelectedItem();
            if (selectedModel != null) {
                showModelDetailsWindow(selectedModel);
            } else {
                showAlert("No Selection", "Please select a model to view details.");
            }
        });

		**/
       
        saveItem.setOnAction(e -> {
            List<Model> modelList = models;  // Convert ObservableList to List
            if (saveFile != null) {
            	saveModelsToFile(modelList, saveFile.getAbsolutePath());
            }
            else {
            	handleSaveAs(models, primaryStage);
            }
        });
        
        saveAsItem.setOnAction(e -> {
            // Call the reusable "Save As" logic directly
            handleSaveAs(models, primaryStage);
        });

        loadItem.setOnAction(e -> loadModelsFromFile(primaryStage));
        
        quitItem.setOnAction(e -> {
            primaryStage.close();
        });
        
        // Scene setup
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    
    private String updateModelDetails(Model model) {
    	String details = "";
    	details += "Model ID: " + model.getModelId();
    	details += "\nFile:     " + model.getFilePath();
    	details += "\nDependency Params:\n";
        
    	// Display Dependency Parameters
        StringBuilder depParams = new StringBuilder();
        for (DependancyParameter dep : model.getDependencyParameters()) {
            depParams.append("\t").append("Param Name:").append(dep.getName()).append("\n");
        }
        details += depParams;
     
        return details;
    	
    }
    
    // Method to handle "Save As" functionality
    private void handleSaveAs(List<Model> models, Stage primaryStage) {
        // Convert ObservableList to List
        List<Model> modelList = models;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(primaryStage);  // Let user choose file path

        if (file != null) {
            // Update saveFile so it can be reused for future saves
            saveFile = file;
            saveModelsToFile(modelList, file.getAbsolutePath());  // Save the models to the file
        }
    }

    // **New Method to Show Model Details Window**
    private void showModelDetailsWindow(Model model) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Model Details");

        VBox detailsLayout = new VBox(10);
        detailsLayout.setPadding(new Insets(10));

        // Display Model ID and File Path
        detailsLayout.getChildren().add(new Label("Model ID: " + model.getModelId()));
        detailsLayout.getChildren().add(new Label("File Path: " + model.getFilePath()));

        // Display Dependency Parameters
        StringBuilder depParams = new StringBuilder();
        for (DependancyParameter dep : model.getDependencyParameters()) {
            depParams.append(dep.getName()).append(" (Model ID: ").append(dep.getModelID()).append(")\n");
        }
        detailsLayout.getChildren().add(new Label("Dependency Parameters: " + (depParams.length() > 0 ? depParams.toString() : "None")));

        // Display Environment Parameters
        StringBuilder envParams = new StringBuilder();
        for (EnvironmentParameter env : model.getEnvironmentParameters()) {
            envParams.append(env.getName()).append(" (File: ").append(env.getFilePath()).append(", Calculation: ").append(env.getCalculation()).append(")\n");
        }
        detailsLayout.getChildren().add(new Label("Environment Parameters: " + (envParams.length() > 0 ? envParams.toString() : "None")));

        // Display Internal Parameters
        StringBuilder intParams = new StringBuilder();
        for (InternalParameter internal : model.getInternalParameters()) {
            intParams.append(internal.getName()).append("\n");
        }
        detailsLayout.getChildren().add(new Label("Internal Parameters: " + (intParams.length() > 0 ? intParams.toString() : "None")));

        Scene detailsScene = new Scene(detailsLayout, 400, 300);
        detailsStage.setScene(detailsScene);
        detailsStage.show();
    }

    private void openModelEditor(Stage owner, Model model) {
        Stage editorStage = new Stage();
        editorStage.setTitle(model == null ? "Add New Model" : "Edit Model");

        VBox editorLayout = new VBox(10);
        editorLayout.setPadding(new Insets(10));

        // Create text fields for Model ID and File Path
        TextField idField = new TextField();
        idField.setPromptText("Model ID");

        TextField filePathField = new TextField();
        filePathField.setPromptText("File Path");

        // Open file dialog when the editor window is shown
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

        // If a model is provided, populate the fields
        if (model != null) {
            idField.setText(model.getModelId());
            filePathField.setText(model.getFilePath());
        } else {
            // If no model exists (i.e., adding a new model), open a file dialog
            fileChooser.setTitle("Select Model File");
            File selectedFile = fileChooser.showOpenDialog(owner);
            if (selectedFile != null) {
                // Set the file path and extract the model ID from the file name
                String filePath = selectedFile.getAbsolutePath();
                String modelId = selectedFile.getName().replaceFirst("[.][^.]+$", ""); // Remove file extension
                idField.setText(modelId);
                filePathField.setText(filePath);
            }
        }

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String id = idField.getText().trim();
            String filePath = filePathField.getText().trim();

            // Validation: Ensure ID and File Path are not empty
            if (id.isEmpty() || filePath.isEmpty()) {
                showAlert("Validation Error", "Both ID and file path are required.");
                return;
            }

            if (model == null) {
                // Create a new model and add it to the list
                Model newModel = new Model(id, filePath);
                models.add(newModel);
            } else {
                // Update the existing model
                model.setModelId(id);
                model.setFilePath(filePath);
            }

            // Close the editor window after saving
            editorStage.close();
        });

        // Add elements to the layout
        editorLayout.getChildren().addAll(
            new Label("Model ID:"), idField,
            new Label("File Path:"), filePathField,
            saveButton
        );

        // Set the layout and display the editor stage
        editorStage.setScene(new Scene(editorLayout));
        editorStage.initOwner(owner);
        editorStage.showAndWait();
    }

    private void openDependencyParamEditor(Model model) {
        if (model == null) {
            showAlert("Error", "Model must be created before adding parameters.");
            return;
        }
        Stage paramStage = new Stage();
        paramStage.setTitle("Add DependencyParam");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField modelIdField = new TextField();
        modelIdField.setPromptText("Model ID");
        TextField definitionField = new TextField();
        definitionField.setPromptText("Definition");

        Button saveButton = new Button("Add");
        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String modelId = modelIdField.getText().trim();
            String definition = definitionField.getText().trim();

            if (!name.isEmpty() && !modelId.isEmpty() && !definition.isEmpty()) {
                model.addDependencyParameter(name, modelId, definition);
                paramStage.close();
            } else {
                showAlert("Validation Error", "All fields are required.");
            }
        });

        layout.getChildren().addAll(new Label("Name:"), nameField, new Label("Model ID:"), modelIdField, new Label("Definition:"), definitionField, saveButton);
        paramStage.setScene(new Scene(layout));
        paramStage.showAndWait();
    }

    private void openEnvironmentParamEditor(Model model) {
        if (model == null) {
            showAlert("Error", "Model must be created before adding parameters.");
            return;
        }
        Stage paramStage = new Stage();
        paramStage.setTitle("Add EnvironmentParam");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField filePathField = new TextField();
        filePathField.setPromptText("File Path");
        TextField calculationField = new TextField();
        calculationField.setPromptText("Calculation");

        Button saveButton = new Button("Add");
        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String filePath = filePathField.getText().trim();
            String calculation = calculationField.getText().trim();

            if (!name.isEmpty() && !filePath.isEmpty() && !calculation.isEmpty()) {
                model.addEnvironmentParameter(name, filePath, calculation);
                paramStage.close();
            } else {
                showAlert("Validation Error", "All fields are required.");
            }
        });

        layout.getChildren().addAll(new Label("Name:"), nameField, new Label("File Path:"), filePathField, new Label("Calculation:"), calculationField, saveButton);
        paramStage.setScene(new Scene(layout));
        paramStage.showAndWait();
    }

    private void openInternalParamEditor(Model model) {
        if (model == null) {
            showAlert("Error", "Model must be created before adding parameters.");
            return;
        }
        Stage paramStage = new Stage();
        paramStage.setTitle("Add InternalParam");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        Button saveButton = new Button("Add");
        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();

            if (!name.isEmpty()) {
                model.addInternalParameter(name);
                paramStage.close();
            } else {
                showAlert("Validation Error", "Name is required.");
            }
        });

        layout.getChildren().addAll(new Label("Name:"), nameField, saveButton);
        paramStage.setScene(new Scene(layout));
        paramStage.showAndWait();
    }

    public void saveModelsToFile(List<Model> models, String filePath) {
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

    private void loadModelsFromFile(Stage owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Models");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(owner);

        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
                JSONObject root = new JSONObject(content);

                models.clear();
                
                // Get the 'models' object (it's a JSONObject, not a JSONArray)
                JSONObject modelsObject = root.getJSONObject("models");
                
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
                    if (dependencyObject != null) {
                        dependencyObject.keySet().forEach(depName -> {
                            JSONObject depObj = dependencyObject.getJSONObject(depName);
                            String depId = depObj.getString("modelId");
                            String depDefinition = depObj.getString("property");
                            model.addDependencyParameter(depName, depId, depDefinition);
                        });
                    }

                    // Deserialize environment parameters
                    JSONObject environmentObject = parametersObject.optJSONObject("environment");
                    if (environmentObject != null) {
                        environmentObject.keySet().forEach(envName -> {
                            JSONObject envObj = environmentObject.getJSONObject(envName);
                            String filePathEnv = envObj.getString("dataFile");
                            String calculation = envObj.getString("type");
                            model.addEnvironmentParameter(envName, filePathEnv, calculation);
                        });
                    }

                    // Deserialize internal parameters
                    JSONObject internalObject = parametersObject.optJSONObject("internal");
                    if (internalObject != null) {
                        internalObject.keySet().forEach(internalName -> {
                            model.addInternalParameter(internalName);
                        });
                    }

                    // Add the constructed model to the list
                    models.add(model);
                });
            } catch (IOException e) {
                showAlert("Error", "Failed to load models: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}