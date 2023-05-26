package com.example.simulinkviewer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SimulinkViewer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ArrayList<DrawLine> drawLines = new ArrayList<>();
        ArrayList<Block> blocksG  = new ArrayList<>();
        readFile(drawLines, blocksG);

        // Create a Canvas with a size
        Canvas canvas = new Canvas(1200, 1200);

        // Get the GraphicsContext for drawing
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set the fill color for the circle
        gc.setFill(Color.GRAY);

        // Set the stroke color and width (optional)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

//        // Draw a block at the center of the canvas
        for (var block: blocksG) {
            Point topleft = new Point(block.getLeft(), block.getTop());
            double blockX = topleft.getX();
            double blockY = topleft.getY();
            double blockWidth = block.getLeft() - block.getRight();
            double blockHeight = block.getTop() - block.getBottom();
            gc.fillRect(blockX-200, blockY+100, -blockWidth, -blockHeight);
            gc.strokeRect(blockX-200, blockY+100, -blockWidth, -blockHeight);
            //print out the block information in a single line
            System.out.println("block info: " + blockX + "," + blockY + "," + blockWidth + "," + blockHeight);
        }
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        for (var line: drawLines)
            gc.strokeLine(line.getP1().getX()-200, line.getP1().getY()+100, line.getP2().getX()-200, line.getP2().getY()+100);
        //loop in the same manner but printout the lines information
        for (var line: drawLines) {
            //convert the line argument which are int to a string
            //convert an int to a string
            String lineString = String.valueOf(line.getP1().getX()) + "," + String.valueOf(line.getP1().getY()) + "," + String.valueOf(line.getP2().getX()) + "," + String.valueOf(line.getP2().getY());
            System.out.println(lineString);
        }

        // Create a StackPane and add the canvas to it
        StackPane pane = new StackPane();
        pane.getChildren().add(canvas);

        Scene scene = new Scene(pane, 1500, 1500);
        stage.setTitle("SimulinkViewer");
        stage.setScene(scene);
        stage.show();
    }

    public void readFile(ArrayList<DrawLine> lines, ArrayList<Block> blocks){
        try {
            // Get the current working directory
            String currentDirectory = System.getProperty("user.dir");

            // Create the file path by appending the file name to the current directory
            String filePath = currentDirectory + File.separator +"src//main//java//com//example//simulinkviewer//" +  "Example.mdl";
//            String filePath = "/home/mahmoud/Downloads/Example.mdl";
            FileReader fileReader = new FileReader(filePath);
            Line[] fileLines = fileReader.getLines();
            for (Line line: fileLines) {
                System.out.println(line.toString());
            }
            Block[] fileBlocks = fileReader.getBlocks();
            for (Block block: fileBlocks) {
                //add the block to the blockG
                blocks.add(block);
                System.out.println(block.toString());
            }
            lines.addAll(GenerateDrawLines(fileBlocks, fileLines));
            System.out.println("called generatedrawlines");



        } catch (Exception e) {
            System.out.println(e);
        }
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
            int startY = 5 + srcBlock.getTop() + (srcPort-1) * 8;
            int startX = srcBlock.getRight();

            Point startPt = new Point(startX, startY);
            Point endPt;

            if(l.getDistBlockId() != -1 && l.getDistBlockId() != 0) { // there is dist
                if(l.getPts() != null && l.getPts().length > 0) { // if there are points, make more lines
                    for(Point linePt: l.getPts()) { // NOTE: line points are distance moved, not absolute coordinations
                        if(linePt.getX() < 0) {
                            startPt = new Point(srcBlock.getLeft(), startPt.getY());
                        }
                        endPt = new Point(startPt.getX() + linePt.getX(), startPt.getY() + linePt.getY());
                        drawLines.add(new DrawLine(startPt, endPt));
                        startPt = endPt;
                    }
                }
                int distId = l.getDistBlockId();
                int distPort = l.getDistBlockPort();
                Block distBlock = Block.findById(blocks, distId);
                // assuming height of blocks = 34
                int endY = portLocationCalc(distBlock.getTop(), distPort);
                System.out.println("after calling gettop");
                int endX = distBlock.getLeft();

                endPt = new Point(endX, endY);

                drawLines.add(new DrawLine(startPt, endPt));

            } else { // there is no dist
                // there must be points
                for(Point linePt: l.getPts()) { // NOTE: line points are distance moved, not absolute coordinations
                    if(linePt.getX() < 0) {
                        startPt = new Point(srcBlock.getLeft(), startPt.getY());
                    }
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
                        int endY = portLocationCalc(distBlock.getTop(), distPort);
                        int endX = distBlock.getLeft();

                        endPt = new Point(endX, endY);

                        drawLines.add(new DrawLine(startPt, endPt));

                    }

                }

            }

        }

        return drawLines;
    }

    int portLocationCalc(int top, int distPort) {
        return top + (distPort -1) * 10 + 5;
    }
}