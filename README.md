# SimulinkViewer
![Screenshot from 2023-05-27 15-36-20](https://github.com/MahmoudHamdy02/SimulinkViewer/assets/100296264/fe3e7344-16bb-4ed2-87a6-2df4b4185dc7)

## Description

A simple Simulink model viewer that can be used to view Simulink models and their contents. Translates XML files to a box and arrow diagram. The viewer is written in Java and uses JavaFX for the GUI.

The project is split into three main parts:

1- Parsing the XML file and converting its content to java objects.

2- Split the lines into smaller lines, depending on points and branches.

3- Draw the prepared line segments using JavaFX.

The viewer is a project for the Advanced Programming CSE231 course.

## Team Members

Section 3

- Mahmoud Hamdy - 2001300
- Ahmed Atwa - 2001391
- Mohamed Adham Mohamed Nagiub - 2001184

## How to use

Run `SimulinkViewer.jar` to start the viewer. It will load `Example.mdl` present with the code and create a window with the model's contents.
