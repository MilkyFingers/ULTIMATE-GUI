package com.ultimate.modelmanager.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainWindowUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX UI Example");

        // Create the root layout - BorderPane
        BorderPane root = new BorderPane();

        // Left side: Scrollable pane
        VBox leftSide = new VBox(10);  // You can add any items you want in this VBox
        leftSide.setStyle("-fx-background-color: lightgray; -fx-padding: 10;");
        ScrollPane scrollPane = new ScrollPane(leftSide);
        scrollPane.setFitToHeight(true);  // Make sure it stretches to fill the height of the window
        scrollPane.setFitToWidth(true);   // Make sure it stretches to fill the width of the window
        root.setLeft(scrollPane);

        // Right side: Displaying information
        VBox rightSide = new VBox(10);
        rightSide.setStyle("-fx-background-color: white; -fx-padding: 10;");
        Label infoLabel = new Label("Information will be displayed here");
        rightSide.getChildren().add(infoLabel);
        root.setCenter(rightSide);

        // Menu Bar
        MenuBar menuBar = new MenuBar();

        // "File" Menu
        Menu fileMenu = new Menu("File");
        MenuItem loadItem = new MenuItem("Load");
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().addAll(loadItem, saveItem);

        // "Edit" Menu
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        editMenu.getItems().addAll(undoItem, redoItem);

        // "Quit" Menu
        Menu quitMenu = new Menu("Quit");
        MenuItem quitItem = new MenuItem("Quit");
        quitMenu.getItems().add(quitItem);

        // Add menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, quitMenu);

        // Set the menu bar at the top of the window
        root.setTop(menuBar);

        // Action handlers for the menu items
        loadItem.setOnAction(e -> {
            System.out.println("Load option selected");
            // Implement Load functionality here
        });

        saveItem.setOnAction(e -> {
            System.out.println("Save option selected");
            // Implement Save functionality here
        });

        quitItem.setOnAction(e -> {
            primaryStage.close();
        });

        // Create the scene with the root layout
        Scene scene = new Scene(root, 800, 600);

        // Set the scene on the primaryStage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
