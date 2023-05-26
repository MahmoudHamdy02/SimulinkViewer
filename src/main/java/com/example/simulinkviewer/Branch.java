package com.example.simulinkviewer;

public class Branch {
    private int z;
    private Point pt;
    private int distBlockId;
    private int distBlockPort;

    public int getZ() {
        return z;
    }

    public Point getPt() {
        return pt;
    }

    public int getDistBlockId() {
        return distBlockId;
    }

    public int getDistBlockPort() {
        return distBlockPort;
    }

    public Branch(int z, int distBlockId, int distBlockPort) {
        this.z = z;
        this.distBlockId = distBlockId;
        this.distBlockPort = distBlockPort;
    }
    public Branch(int z, Point pt, int distBlockId, int distBlockPort) {
        this.z = z;
        this.pt = pt;
        this.distBlockId = distBlockId;
        this.distBlockPort = distBlockPort;
    }

    @Override
    public String toString() {
        return String.format("Z: %s, Dist block id: %s, Dist block port: %s, Point: ", z, distBlockId, distBlockPort) + pt.toString();
    }
}
