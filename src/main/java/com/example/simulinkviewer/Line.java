package com.example.simulinkviewer;

public class Line {
    private int z;
    private int srcBlockId;
    private int srcBlockPort;
    // if you see -1, then it is empty
    private int distBlockId = -1;
    private int distBlockPort = -1;

    private Point[] pts = new Point[0];
    private Branch[] branches = new Branch[0];

    public int getZ() {
        return z;
    }

    public int getSrcBlockId() {
        return srcBlockId;
    }

    public int getSrcBlockPort() {
        return srcBlockPort;
    }

    public int getDistBlockId() {
        return distBlockId;
    }

    public int getDistBlockPort() {
        return distBlockPort;
    }

    public Point[] getPts() {
        return pts;
    }

    public Branch[] getBranches() {
        return branches;
    }

    public Line(int z, int srcBlockId, int srcBlockPort, int distBlockId, int distBlockPort, Branch[] branches) {
        this.z = z;
        this.srcBlockId = srcBlockId;
        this.srcBlockPort = srcBlockPort;
        this.distBlockId = distBlockId;
        this.distBlockPort = distBlockPort;
        this.branches = branches;
    }

    public Line(int z, int srcBlockId, int srcBlockPort, int distBlockId, int distBlockPort) {
        this.z = z;
        this.srcBlockId = srcBlockId;
        this.srcBlockPort = srcBlockPort;
        this.distBlockId = distBlockId;
        this.distBlockPort = distBlockPort;
    }

    public Line(int z, int srcBlockId, int srcBlockPort, int distBlockId, int distBlockPort, Point[] pts, Branch[] branches) {
        this.z = z;
        this.srcBlockId = srcBlockId;
        this.srcBlockPort = srcBlockPort;
        this.distBlockId = distBlockId;
        this.distBlockPort = distBlockPort;
        this.pts = pts;
        this.branches = branches;
    }

    @Override
    public String toString() {
        String points = "";
        String brnches = "";
        for (Point p : pts) {
            points = points.concat(p.toString());
        }
        for (Branch b : branches) {
            brnches = brnches.concat(b.toString());
        }
        return String.format(
                "Src block id: %s, Src block port: %s, Dist block id: %s, Dist block port: %s, Z: %s. ",
                srcBlockId, srcBlockPort, distBlockId, distBlockPort, z) + "Points: " + points + "Branches: " + brnches;
    }
}
