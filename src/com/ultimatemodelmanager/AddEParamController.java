package com.ultimatemodelmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEParamController {
	
	@FXML
	private ChoiceBox<UndefinedParameter> undefinedParameters;
	@FXML
	private TextField eParamName;
	@FXML
	private TextField eParamFP;
	@FXML
	private TextField eParamCalc;
	@FXML
	private Button save;
	
	private Stage editorStage;
	private Model model;
	
	public AddEParamController(Stage editorStage, Model model) {
		this.editorStage = editorStage;
		this.model = model;
	}
	
	@FXML
	public void initialize() {
		save.setOnAction(e -> saveEParam());
		// set the choice box options based
		ObservableList<UndefinedParameter> observableItems = FXCollections.observableArrayList(model.getUndefinedParameters());
        undefinedParameters.setItems(observableItems);  // Set the list as items for the ChoiceBox
        undefinedParameters.setOnAction(e -> getName());
	}
	
	private void getName() {
		eParamName.setText(undefinedParameters.getValue().getParameter());
	}
	
	private void saveEParam() {
		// remove the parameter
		model.removeUndefinedParamter(undefinedParameters.getValue());
		// get values to create e param
		String name = eParamName.getText().trim();
		String file = eParamFP.getText().trim();
		String calc = eParamCalc.getText().trim();
		
        // Validate that both the model ID and file path are provided
        if (name.isEmpty() || file.isEmpty() || calc.isEmpty()) {
            showAlert("Validation Error", "Both ID and file path are required.");
            return;
        }
        
        model.addEnvironmentParameter(name, file, calc);
        editorStage.close();

	}
	
    /**
     * Displays an alert to the user with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the message to display in the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
