package com.example.simulinkviewer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulinkViewer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FileReader fileReader = new FileReader("/home/mahmoud/Downloads/Example.mdl");
            Line[] fileLines = fileReader.getLines();
            for (Line line: fileLines) {
                System.out.println(line.getSrcBlockId());
            }
            Block[] fileBlocks = fileReader.getBlocks();
            for (Block block: fileBlocks) {
                System.out.println(block.getType());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        StackPane pane = new StackPane();

        pane.getChildren().add(new Button("OK"));
        Scene scene = new Scene(pane, 500, 500);
        stage.setTitle("SimulinkViewer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}