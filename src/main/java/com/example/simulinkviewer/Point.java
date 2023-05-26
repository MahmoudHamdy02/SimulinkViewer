package com.example.simulinkviewer;

public class Point {
    private int x;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ". ";
    }
}
