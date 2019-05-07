package controllers;

public class Merchant {
    private String name;
    private int visitedCount;

    public Merchant() {

    }

    public Merchant(String name, int count) {
        this.name = name;
        this.visitedCount = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void incrementVisited() {
        this.visitedCount++;
    }

    public int getVisitedCount() {
        return this.visitedCount;
    }
}
