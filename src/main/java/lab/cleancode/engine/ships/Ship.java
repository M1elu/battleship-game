package lab.cleancode.engine.ships;

import lab.cleancode.engine.Coordinate;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship implements Cloneable {
    final private String name;
    final private int length;
    private ArrayList<Coordinate> coordinates;
    private int hitCount = 0;

    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
        this.coordinates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public int getLength() {
        return length;
    }

    public void hit() {
        if (hitCount < length) {
            this.hitCount++;
        }
    }

    public boolean isSunk() {
        return hitCount == length;
    }

    @Override
    public Ship clone() throws CloneNotSupportedException {
        return (Ship) super.clone();
    }
}
