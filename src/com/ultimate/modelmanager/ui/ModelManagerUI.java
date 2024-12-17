package com.ultimate.modelmanager.ui;

import com.ultimate.modelmanager.model.*;
import com.ultimate.modelmanager.utils.PrismFileParser;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelManagerUI extends Application {
	
	// This is the root window. It will contain all the secondary widgets
	private Stage mainStage;
	
	// This will contain the models in the session
    private final ObservableList<Model> models = FXCollections.observableArrayList();
	
	// These IDs will be assigned to widgets -> allows access outside of the startMethod
    // TODO : Assign all IDs
	private String menu = "menu";
	private String selectModel = "selectModelButtons";
	private String modelsList = "modelsList";
	private String modelDetails = "modelDetails";
	private String environmentParamList = "environmentParamList";
	private String dependencytParamList = "dependencytParamList";
	private String internalParamList = "internalParamList";
	private String undefinedParamList = "undefinedParamList";
	
	// These strings will contain the title and project name
	private String title = "ULTIMATE Model Manager: ";
	private String projectName = "untitled";

	/**
	 * Inherited from Application - this is where all the layout of the GUI will be defined
	 */
	@Override
	public void start(Stage stage) throws Exception {
		mainStage = stage;
		mainStage.setTitle(title + projectName);
		
		GridPane root = new GridPane(); // This GridPane will act as the root layer widget -> all other widgets will be placed on this
		
		// Creating the menuBar widget
		MenuBar menuBar = new MenuBar(); // The MenuBar -> placed at the top of the root
		menuBar.setId(menu); // This ID will be used to access this widget outside of this method
        Menu fileMenu = new Menu("File"); // 'File' menu
        MenuItem loadItem = new MenuItem("Load");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem quitItem = new MenuItem("Quit");
        fileMenu.getItems().addAll(loadItem, saveItem, saveAsItem, quitItem);
        menuBar.getMenus().addAll(fileMenu);
        
        // This VBox will contain an HBox and a ListView on the left of the root -> displays the list of models
        VBox projectDetails = new VBox(10);
        HBox selectModelButtonsBox = new HBox(10);
        selectModelButtonsBox.setAlignment(Pos.CENTER);
        Button addModelButton = new Button("+");
        Button upButton = new Button("↑");
        Button downButton = new Button("↓");
        selectModelButtonsBox.getChildren().addAll(addModelButton, upButton, downButton);
        ListView<Model> modelListView = new ListView<>(models);
        projectDetails.getChildren().addAll(selectModelButtonsBox, modelListView);
        projectDetails.setVgrow(modelListView, Priority.ALWAYS);
        
        // This VBox will contain the list/labels and buttons for adding/editing parameters of the 3 classes
        VBox definedParamDetails = new VBox(10);
        Text modelIDFile = new Text("Model ID:\nFile Path:"); // display the selected modelID and file path
        
        VBox environmentParamDetails = new VBox(10); // this will hold the labels/buttons/lists for e params
        HBox eParamLabelButton = new HBox(10);
        eParamLabelButton.setAlignment(Pos.CENTER);
        Label environmentParamLabel = new Label("Environment Parameters");
        Button addEnvironmentParamButton = new Button("+");
        eParamLabelButton.getChildren().addAll(environmentParamLabel, addEnvironmentParamButton);
        ListView<EnvironmentParameter> environmentParamList = new ListView<>();
        environmentParamDetails.getChildren().addAll(eParamLabelButton, environmentParamList);
        
        VBox dependencyParamDetails = new VBox(10); // this will hold the labels/buttons/lists for d params
        HBox dParamLabelButton = new HBox(10);
        dParamLabelButton.setAlignment(Pos.CENTER);
        Label dependencyParamLabel = new Label("Dependency Parameters");
        Button addDependencyParamButton = new Button("+");
        dParamLabelButton.getChildren().addAll(dependencyParamLabel, addDependencyParamButton);
        ListView<DependancyParameter> dependencyParamList = new ListView<>();
        dependencyParamDetails.getChildren().addAll(dParamLabelButton, dependencyParamList);
        
        VBox internalParamDetails = new VBox(10); // this will hold the labels/buttons/lists for d params
        HBox iParamLabelButton = new HBox(10);
        iParamLabelButton.setAlignment(Pos.CENTER);
        Label internalParamLabel = new Label("Internal Parameters");
        Button addInternalParamButton = new Button("+");
        iParamLabelButton.getChildren().addAll(internalParamLabel, addInternalParamButton);
        ListView<InternalParameter> internalParamList = new ListView<>();
        internalParamDetails.getChildren().addAll(iParamLabelButton, internalParamList);
        
        definedParamDetails.getChildren().addAll(modelIDFile, environmentParamDetails, dependencyParamDetails, internalParamDetails);
        
        // This will hold the list of undefined parameters
        VBox undefinedParamDetails = new VBox(10);
        Label uParamLabel = new Label("Undefined Paramters");
        uParamLabel.setAlignment(Pos.CENTER);
        ListView<UndefinedParameter> undefinedParamList = new ListView<>();
        undefinedParamDetails.getChildren().addAll(uParamLabel, undefinedParamList);
        undefinedParamDetails.setVgrow(undefinedParamList, Priority.ALWAYS);

        // Adding all the widgets to the root grid
        root.add(menuBar, 0,0); // Set the menu bar at the top of the window
        root.add(projectDetails, 0, 1);        
        root.add(definedParamDetails, 1, 1);
        root.add(undefinedParamDetails, 2, 1);
        
        // Dynamic resizing control for widgets
        root.setColumnSpan(menuBar, root.REMAINING); // Span menu bar across all columns        
        // Ensure both VBoxes take up equal space
        root.setHgrow(projectDetails, Priority.ALWAYS);
        root.setHgrow(definedParamDetails, Priority.ALWAYS);
        root.setHgrow(undefinedParamDetails, Priority.ALWAYS);
        root.setVgrow(projectDetails, Priority.ALWAYS);
        root.setVgrow(definedParamDetails, Priority.ALWAYS);
        root.setVgrow(undefinedParamDetails, Priority.ALWAYS);

        // Set margins for VBoxes to create spacing between them and the GridPane edges
        root.setMargin(projectDetails, new Insets(10));
        root.setMargin(definedParamDetails, new Insets(10));
        root.setMargin(undefinedParamDetails, new Insets(10));


        // Scene setup
        mainStage.setScene(new Scene(root, 800, 600));
        mainStage.show();

	}
	
    public static void main(String[] args) {
        launch(args);
    }
	
}

/**
public class ModelManagerUI extends Application {
	// The Stage -> allows changing the title name
	private Stage mainStage;
	// This will store the models during the user session
    private final ObservableList<Model> models = FXCollections.observableArrayList();
    // Used for the save function
    File saveFile = null;
    // used when a project is loaded into the editor 
    String projectName = "untitled";
    // Parses files and returns the list of params
    PrismFileParser parser = new PrismFileParser();

    @Override
    public void start(Stage stage) {
    	mainStage = stage;
        mainStage.setTitle("ULTIMATE Model Manager: " + projectName);

        // Layouts
        BorderPane root = new BorderPane();
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
        
        saveItem.setOnAction(e -> {
            List<Model> modelList = models;  // Convert ObservableList to List
            if (saveFile != null) {
            	saveModelsToFile(modelList, saveFile.getAbsolutePath());
            }
            else {
            	handleSaveAs(models, mainStage);
            }
        });
        
        saveAsItem.setOnAction(e -> {
            // Call the reusable "Save As" logic directly
            handleSaveAs(models, mainStage);
        });

        loadItem.setOnAction(e -> loadModelsFromFile(mainStage));
        
        quitItem.setOnAction(e -> {
            mainStage.close();
        });
        
        GridPane modelDetails = new GridPane();
        modelDetails.setStyle("-fx-border-width: 2px; -fx-padding: 5px;");
        // This textbox will hold the modelID and filePath
        Text modelIDFile = new Text("\nModelID:\n\nFile Path:\n");
        modelIDFile.setId("modelIDFile"); // assign an id to update this widget later
        modelDetails.add(modelIDFile, 0, 0, 2, 1);
        // Environmental param label
        Label environmentParamLabel = new Label("Environment Parameters:\t");
        modelDetails.add(environmentParamLabel, 0, 1, 1, 1);
        Button addEnvironmentParam = new Button("+");
        modelDetails.add(addEnvironmentParam, 1, 1, 1, 1);
        ListView<EnvironmentParameter> environmentParamList = new ListView<>();
        environmentParamList.setId("environmentParamList");  // Ensure this ID is set
        modelDetails.add(environmentParamList, 0, 2, 2, 1);
        Label dependancyParamLabel = new Label("Dependency Parameters:\t");
        modelDetails.add(dependancyParamLabel, 0, 3, 1, 1);
        Button addDependencyParam = new Button("+");
        modelDetails.add(addDependencyParam, 1, 3, 1, 1);
        ListView<DependancyParameter> dependancyParamList = new ListView<>();
        dependancyParamList.setId("dependancyParamList");
        modelDetails.add(dependancyParamList, 0, 4, 2, 1);
        Label internalParamLabel = new Label("Internal Parameters:\t");
        modelDetails.add(internalParamLabel, 0, 5, 1, 1);
        Button addInternalParam = new Button("+");
        modelDetails.add(addInternalParam, 1, 5, 1, 1);
        ListView<InternalParameter> internalParamList = new ListView<>();
        internalParamList.setId("internalParamList");
        modelDetails.add(internalParamList, 0, 6, 2, 1);
        root.setCenter(modelDetails);
        
        // This will list all the undefinedParams
        VBox undefinedParams = new VBox();
        undefinedParams.setStyle("-fx-border-width: 2px; -fx-padding: 5px;");
        Label undefinedParamsLabel = new Label("Undefined Parameters:");
        ListView<UndefinedParameter> undefinedParamList = new ListView<>();
        undefinedParamList.setId("undefinedParamList");
        undefinedParams.getChildren().addAll(undefinedParamsLabel, undefinedParamList);
        VBox.setVgrow(undefinedParamList, Priority.ALWAYS);
        root.setRight(undefinedParams);

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
                updateModelDetails(newValue); // Call your method with the newly selected item
            }
        });
        
        // Buttons
        Button addModelButton = new Button("+");
        Button upButton = new Button("↑");
        Button downButton = new Button("↓");
        // VBox for buttons and list view
        VBox vbox = new VBox(10); // 10-pixel spacing between elements
        vbox.setStyle("-fx-border-width: 2px; -fx-padding: 5px;");
        // Create an HBox for the buttons (arrange horizontally)
        HBox hbox = new HBox(10); // 10-pixel spacing between buttons
        // Centre the buttons horizontally
        hbox.setAlignment(Pos.CENTER);
        // Apply a border to the HBox
        hbox.setStyle("-fx-border-width: 2px; -fx-padding: 5px;");
        // Add buttons to the HBox
        hbox.getChildren().addAll(addModelButton, upButton, downButton);
        // Add the HBox for buttons and the ListView to the VBox
        vbox.getChildren().addAll(hbox, modelListView);
        // Ensure the ListView fills the remaining vertical space in the VBox
        VBox.setVgrow(modelListView, Priority.ALWAYS);
        // Set the VBox to the left side of the BorderPane
        root.setLeft(vbox);

        // Add event handlers for the buttons
        addModelButton.setOnAction(e -> openModelEditor(mainStage, null));

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
     
        // Scene setup
        mainStage.setScene(new Scene(root, 800, 600));
        mainStage.show();
    }
    
    // Here, known dependencies will be displayed as well as unknown
    private void updateModelDetails(Model model) {
        // Update the model ID and file path in the Text component
        Text modelIDFile = (Text) mainStage.getScene().lookup("#modelIDFile");
        modelIDFile.setText("ModelID: " + model.getModelId() + "\nFile Path: " + model.getFilePath());

        // Update the environment parameters list
        ListView<EnvironmentParameter> environmentParamList = (ListView<EnvironmentParameter>) mainStage.getScene().lookup("#environmentParamList");
        environmentParamList.getItems().clear(); // Clear existing items
        environmentParamList.getItems().addAll(model.getEnvironmentParameters()); // Add new items from the model

        // Update the dependency parameters list
        ListView<DependancyParameter> dependancyParamList = (ListView<DependancyParameter>) mainStage.getScene().lookup("#dependancyParamList");
        dependancyParamList.getItems().clear(); // Clear existing items
        dependancyParamList.getItems().addAll(model.getDependencyParameters()); // Add new items from the model

        // Update the internal parameters list
        ListView<InternalParameter> internalParamList = (ListView<InternalParameter>) mainStage.getScene().lookup("#internalParamList");
        internalParamList.getItems().clear(); // Clear existing items
        internalParamList.getItems().addAll(model.getInternalParameters()); // Add new items from the model

        // Update the undefined parameters list
        ListView<UndefinedParameter> undefinedParamList = (ListView<UndefinedParameter>) mainStage.getScene().lookup("#undefinedParamList");
        undefinedParamList.getItems().clear(); // Clear existing items
        undefinedParamList.getItems().addAll(model.getUndefinedParameters()); // Add new items from the model

    }
  
    // Method to handle "Save As" functionality
    private void handleSaveAs(List<Model> models, Stage mainStage) {
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
                // Update title
                projectName = file.getName().replaceAll(".json", "");
                setStageTitle(projectName);
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
						// TODO Auto-generated catch block
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
                                UndefinedParameter up = new UndefinedParameter(parsedParam);
                                model.addUndefinedParameter(up.getParameter());
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
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void setStageTitle(String newTitle) {
    	mainStage.setTitle("ULTIMATE Model Manager: " + newTitle);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
**/