package com.example.simulinkviewer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import java.io.File;

public class FileReader {
    private Block[] blocks;
    private Line[] lines;

    FileReader() {

    }

    FileReader(String path) throws ParserConfigurationException, IOException, SAXException {//
        String fileString = Files.readString(Path.of(path));
        String firstSplit = fileString.split("<System>")[1];
        String secondSplit = firstSplit.split("</System>")[0];
        String finalString = "<System>" + secondSplit + "</System>";


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(finalString)));

        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());


        // Blocks
        NodeList blockList = doc.getElementsByTagName("Block");
        blocks = new Block[blockList.getLength()];

        for (int i = 0; i < blockList.getLength(); i++) {
            Node block = blockList.item(i);
            Element eBlock = (Element)block;

            String name = eBlock.getAttribute("Name");
            String type = eBlock.getAttribute("BlockType");
            int id = Integer.parseInt(eBlock.getAttribute("SID"));
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int zOrder = 0;
            int portsIn = 1;
            int portsOut = 1;

            NodeList properties = eBlock.getElementsByTagName("P");
            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                Element eProperty = (Element)property;
                String propType = eProperty.getAttribute("Name");

                if (propType.equals("Position")) {
                    String[] position = eProperty.getTextContent()
                            .replace("[", "")
                            .replace("]", "")
                            .replace(" ", "")
                            .split(",");
                    left = Integer.parseInt(position[0]);
                    top = Integer.parseInt(position[1]);
                    right = Integer.parseInt(position[2]);
                    bottom = Integer.parseInt(position[3]);
                }

                if (propType.equals("ZOrder")) {
                    zOrder = Integer.parseInt(eProperty.getTextContent());
                }

                if (propType.equals("Ports")) {
                    String[] ports = eProperty.getTextContent()
                            .replace("[", "")
                            .replace("]", "")
                            .replace(" ", "")
                            .split(",");

                    if (ports.length == 2) {
                        portsIn = Integer.parseInt(ports[0]);
                        portsOut = Integer.parseInt(ports[1]);
                    } else if (ports.length == 1) {
                        portsIn = Integer.parseInt(ports[0]);
                        portsOut = 0;
                    }
                }
            }

            Block new_block = new Block(type, name, id, left, top, right, bottom, zOrder);
            new_block.setPortsIn(portsIn);
            new_block.setPortsOut(portsOut);
            blocks[i] = new_block;
        }

        // Lines
        NodeList linesList = doc.getElementsByTagName("Line");
        lines = new Line[linesList.getLength()];
        for (int i = 0; i < linesList.getLength(); i++) {
            Node line = linesList.item(i);
            Element eLine = (Element)line;

            int zOrder = 0;
            int srcBlockId = 0;
            int srcBlockPort = 0;
            int dstBlockId = -1;
            int dstBlockPort = -1;
            Point[] points = new Point[0];
            boolean hasPoints = false;
            Branch[] branches;

            NodeList properties = eLine.getElementsByTagName("P");
            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                Element eProperty = (Element)property;
                String propType = eProperty.getAttribute("Name");

                if (propType.equals("ZOrder")) {
                    zOrder = Integer.parseInt(eProperty.getTextContent());
                }
                if (propType.equals("Src")) {
                    String source = eProperty.getTextContent();
                    srcBlockId = Integer.parseInt(String.valueOf(source.charAt(0)));
                    srcBlockPort = Integer.parseInt(String.valueOf(source.charAt(6)));
                }
                if (propType.equals("Dst")) {
                    String source = eProperty.getTextContent();
                    dstBlockId = Integer.parseInt(String.valueOf(source.charAt(0)));
                    dstBlockPort = Integer.parseInt(String.valueOf(source.charAt(5)));
                }
                if (propType.equals("Points")) {
                    hasPoints = true;
                    String[] pointsText = eProperty.getTextContent()
                            .replace("[", "")
                            .replace("]", "")
                            .replace(";", ",")
                            .replace(" ", "")
                            .split(",");
                    points = new Point[pointsText.length/2];
                    int index = 0;
                    for (int p = 0; p < pointsText.length; p = p+2) {
                        points[index] = new Point(Integer.parseInt(pointsText[p]), Integer.parseInt(pointsText[p+1]));
                        index++;
                    }
                }
            }

            NodeList branchList = eLine.getElementsByTagName("Branch");
            branches = new Branch[branchList.getLength()];
            for (int j = 0; j < branchList.getLength(); j++) {
                Node branch = branchList.item(j);
                Element eBranch = (Element)branch;

                int branchZ = 0;
                int branchDstBlockId = 0;
                int branchDstBlockPort = 0;
                Point branchPoint = new Point(0,0);
                boolean pointExists = false;

                NodeList branchProperties = eBranch.getElementsByTagName("P");
                for (int k = 0; k < branchProperties.getLength(); k++) {
                    Node branchProperty = branchProperties.item(k);
                    Element eBranchProp = (Element)branchProperty;
                    String propType = eBranchProp.getAttribute("Name");

                    if (propType.equals("ZOrder")) {
                        branchZ = Integer.parseInt(eBranchProp.getTextContent());
                    }
                    if (propType.equals("Dst")) {
                        String source = eBranchProp.getTextContent();
                        branchDstBlockId = Integer.parseInt(String.valueOf(source.charAt(0)));
                        branchDstBlockPort = Integer.parseInt(String.valueOf(source.charAt(5)));
                    }
                    if (propType.equals("Points")) {
                        pointExists = true;
                        String[] pointsText = eBranchProp.getTextContent()
                                .replace("[", "")
                                .replace("]", "")
                                .replace(" ", "")
                                .split(",");
                        branchPoint = new Point(Integer.parseInt(pointsText[0]), Integer.parseInt(pointsText[1]));
                    }
                }
                if (pointExists) {
                    branches[j] = new Branch(branchZ, branchPoint, branchDstBlockId, branchDstBlockPort);
                } else {
                    branches[j] = new Branch(branchZ, branchDstBlockId, branchDstBlockPort);
                }
            }
            if (hasPoints) {
                lines[i] = new Line(zOrder, srcBlockId, srcBlockPort, dstBlockId, dstBlockPort, points, branches);
            } else {
                lines[i] = new Line(zOrder, srcBlockId, srcBlockPort, dstBlockId, dstBlockPort, branches);
            }

        }

    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[] blocks) {
        this.blocks = blocks;
    }

    public Line[] getLines() {
        return lines;
    }

    public void setLines(Line[] lines) {
        this.lines = lines;
    }
}
