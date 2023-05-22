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

    FileReader() {

    }

    FileReader(String path) throws ParserConfigurationException, IOException, SAXException {//
        String fileString = Files.readString(Path.of(path));
        String firstSplit = fileString.split("<System>")[1];
        String secondSplit = firstSplit.split("</System>")[0];
        String finalString = "<System>" + secondSplit + "</System>";
        System.out.println(finalString);


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(finalString)));

        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

//        NodeList nList = doc.getElementsByTagName("CONTAINER");
    }
}
