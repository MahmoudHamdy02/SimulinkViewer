package com.example.simulinkviewer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;

public class SimulinkViewer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FileReader fileReader = new FileReader("/home/mahmoud/Downloads/Example.mdl");
            Line[] fileLines = fileReader.getLines();
            for (Line line: fileLines) {
                System.out.println(line.getDistBlockId());
            }
            Block[] fileBlocks = fileReader.getBlocks();
            for (Block block: fileBlocks) {
                System.out.println(block.getType());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        // Create a Canvas with a size
        Canvas canvas = new Canvas(500, 500);

        // Get the GraphicsContext for drawing
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set the fill color for the circle
        gc.setFill(Color.BLUE);

        // Set the stroke color and width (optional)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        // Draw a block at the center of the canvas
        double blockX = 150;
        double blockY = 150;
        double blockWidth = 100;
        double blockHeight = 200;
        gc.fillRect(blockX, blockY, blockWidth, blockHeight);
        gc.strokeRect(blockX, blockY, blockWidth, blockHeight);

        // Draw a line coming out of the block
        double lineX1 = blockX + blockWidth;
        double lineY1 = blockY + blockHeight / 2;
        double lineX2 = lineX1 + 150;
        double lineY2 = lineY1 - 100;
        gc.setStroke(Color.RED);
        gc.setLineWidth(4);
        gc.strokeLine(lineX1, lineY1, lineX2, lineY2);


        // Create a StackPane and add the canvas to it
        StackPane pane = new StackPane();
        pane.getChildren().add(canvas);

        Scene scene = new Scene(pane, 500, 500);
        stage.setTitle("SimulinkViewer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}