package com.ultimate.modelmanager.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class MainWindowUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a ListView
        ListView<String> listView = new ListView<>();
        
        // Add some items to the list
        listView.getItems().addAll("Item 1", "Item 2", "Item 3", "Item 4");

        // Set a custom cell factory
        listView.setCellFactory(param -> new ListCell<>() {
            private final Label label = new Label();
            private final Button button = new Button("Action");
            private final HBox hBox = new HBox(label, button);

            {
                // Style the HBox
                hBox.setSpacing(10);
                HBox.setHgrow(label, Priority.ALWAYS);

                // Add a click handler for the button
                button.setOnAction(event -> {
                    String item = getItem();
                    if (item != null) {
                        System.out.println("Button clicked for: " + item);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item); // Set the text for the label
                    setGraphic(hBox);    // Set the HBox as the graphic
                }
            }
        });

        // Set up the scene and stage
        Scene scene = new Scene(listView, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ListView with Buttons");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}