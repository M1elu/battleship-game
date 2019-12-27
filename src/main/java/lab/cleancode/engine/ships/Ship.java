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

    public void setCoordinates(Coordinate startCoordinate, Boolean isPlacedHorizontal) {
        this.coordinates = generateCoordinates(startCoordinate, isPlacedHorizontal);
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

    private ArrayList<Coordinate> generateCoordinates(
            Coordinate startCoordinate,
            Boolean isHorizontal
    ) {
        ArrayList<Coordinate> newCoordinates = new ArrayList<>();
        newCoordinates.add(startCoordinate);
        for (int i = 1; i <= getLength() - 1; i++) {
            if (isHorizontal) {
                int xCoordinate = startCoordinate.getX();
                int yCoordinate = startCoordinate.getY() + i;
                Coordinate nextCoordinate = new Coordinate(xCoordinate, yCoordinate);
                newCoordinates.add(nextCoordinate);
            } else {
                int xCoordinate = startCoordinate.getX() + i;
                int yCoordinate = startCoordinate.getY();
                Coordinate nextCoordinate = new Coordinate(xCoordinate, yCoordinate);
                newCoordinates.add(nextCoordinate);
            }
        }
        return newCoordinates;
    }

    @Override
    public Ship clone() throws CloneNotSupportedException {
        return (Ship) super.clone();
    }
}
