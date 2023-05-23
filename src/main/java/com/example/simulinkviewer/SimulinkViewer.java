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
import java.util.ArrayList;

public class SimulinkViewer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FileReader fileReader = new FileReader("D:\\CODING\\Java\\SimulinkViewer\\src\\main\\java\\com\\example\\simulinkviewer\\Example.mdl");
            Line[] fileLines = fileReader.getLines();
            for (Line line: fileLines) {
                System.out.println(line.getDistBlockId());
            }
            Block[] fileBlocks = fileReader.getBlocks();
            for (Block block: fileBlocks) {
                System.out.println(block.getType());
            }
            ArrayList<DrawLine> drawLines = GenerateDrawLines(fileBlocks, fileLines);

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

    public ArrayList<DrawLine> GenerateDrawLines(Block[] blocks, Line[] lines) {
        ArrayList<DrawLine> drawLines = new ArrayList();

        for(Line l: lines) {
            int srcId = l.getSrcBlockId();
            int srcPort = l.getSrcBlockPort();
            Block srcBlock = Block.findById(blocks, srcId);
            // assuming height of blocks = 34
            int startY = 5 + srcBlock.getTop() + srcPort * 8;
            int startX = srcBlock.getRight();

            Point startPt = new Point(startX, startY);
            Point endPt;

            if(l.getDistBlockId() != -1 || l.getDistBlockId() != 0) { // there is dist
                if(l.getPts() != null && l.getPts().length > 0) { // if there are points, make more lines
                    for(Point linePt: l.getPts()) { // NOTE: line points are distance moved, not absolute coordinations
                        endPt = new Point(startPt.getX() + linePt.getX(), startPt.getY() + linePt.getY());
                        drawLines.add(new DrawLine(startPt, endPt));
                        startPt = endPt;
                    }
                }
                int distId = l.getDistBlockId();
                int distPort = l.getDistBlockPort();
                Block distBlock = Block.findById(blocks, distId);
                // assuming height of blocks = 34
                int endY = 5 + distBlock.getTop() + distPort * 8;
                int endX = distBlock.getLeft();

                endPt = new Point(endX, endY);

                drawLines.add(new DrawLine(startPt, endPt));

            } else { // there is no dist
                // there must be points
                for(Point linePt: l.getPts()) { // NOTE: line points are distance moved, not absolute coordinations
                    endPt = new Point(startPt.getX() + linePt.getX(), startPt.getY() + linePt.getY());
                    drawLines.add(new DrawLine(startPt, endPt));
                    startPt = endPt;
                }

                final Point branchStart = startPt;

                // branches
                if(l.getBranches() != null && l.getBranches().length > 0) { // are there branches?
                    for(Branch br: l.getBranches()) {
                        // always contains dist, could contain points
                        startPt = branchStart;

                        if(br.getPt() != null) { // if there are points,
                            endPt = new Point(startPt.getX() + br.getPt().getX(), startPt.getY() + br.getPt().getY());
                            drawLines.add(new DrawLine(startPt, endPt));
                            startPt = endPt;
                        }

                        int distId = br.getDistBlockId();
                        int distPort = br.getDistBlockPort();
                        Block distBlock = Block.findById(blocks, distId);
                        // assuming height of blocks = 34
                        int endY = 5 + distBlock.getTop() + distPort * 8;
                        int endX = distBlock.getLeft();

                        endPt = new Point(endX, endY);

                        drawLines.add(new DrawLine(startPt, endPt));

                    }

                }

            }

        }

        return drawLines;
    }
}