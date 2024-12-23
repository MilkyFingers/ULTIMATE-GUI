package com.controllers;

import com.parameters.EnvironmentParameter;
import com.parameters.Model;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditEParamController {
	
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
	private EnvironmentParameter oldParam;
	
	public EditEParamController(Stage editorStage, Model model, EnvironmentParameter oldParam) {
		this.editorStage = editorStage;
		this.model = model;
		this.oldParam = oldParam;
	}
	
	@FXML
	public void initialize() {
		save.setOnAction(e -> saveEParam());
		eParamName.setText(oldParam.getName());
		eParamFP.setText(oldParam.getFilePath());
		eParamCalc.setText(oldParam.getCalculation());
	}
	
	private void saveEParam() {
		// get values to create e param
		String name = eParamName.getText().trim();
		String file = eParamFP.getText().trim();
		String calc = eParamCalc.getText().trim();
		
        // Validate that both the model ID and file path are provided
        if (name.isEmpty() || file.isEmpty() || calc.isEmpty()) {
            showAlert("Validation Error", "Both ID and file path are required.");
            return;
        }
        
        EnvironmentParameter newParam = new EnvironmentParameter(name, file, calc);
        
        model.replaceParameter(oldParam, newParam);
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