package com.example.simulinkviewer;

public class Block {
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public int getZ() {
        return z;
    }

    private String type;
    private String name;
    private int id;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private int z;
    private int portsIn = 1;
    private int portsOut = 1;

    public int getPortsIn() {
        return portsIn;
    }

    public void setPortsIn(int portsIn) {
        this.portsIn = portsIn;
    }

    public int getPortsOut() {
        return portsOut;
    }

    public void setPortsOut(int portsOut) {
        this.portsOut = portsOut;
    }

    public Block(String type, String name, int id, int left, int top, int right, int bottom, int z) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.z = z;
    }

    public static Block findById(Block[] blocks, int id) {
        for(Block b: blocks) {
            if(b.id == id) return b;
        }
        return null;
    }
}
