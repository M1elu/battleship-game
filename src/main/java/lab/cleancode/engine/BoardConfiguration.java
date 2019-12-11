package lab.cleancode.engine;

import lab.cleancode.engine.ships.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardConfiguration {

    public static BoardConfiguration getDefault() {
        List<Ship> ships = List.of(
                new Carrier(),
                new Battleship(),
                new Cruiser(),
                new Destroyer()
        );
        return new BoardConfiguration(new BoardConstraints(10, 10), ships);
    }

//    public static BoardConfiguration getRandom() {
//        Random random = new Random();
//        random.nextInt(10);
//    }

    public BoardConfiguration(BoardConstraints boardConstraints, List<Ship> ships) {
        this.boardConstraints = boardConstraints;
        this.ships = ships;
    }

    public BoardConfiguration(BoardConstraints boardConstraints) {
        this.boardConstraints = boardConstraints;
        this.ships = new ArrayList<>();
    }

    private BoardConstraints boardConstraints;
    private List<Ship> ships;

    public BoardConstraints getBoardConstraints() {
        return boardConstraints;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public Boolean canAddShips(List<Ship> newShips) {
        boolean isAnyShipTooLong = newShips.stream().anyMatch(s -> s.getLength() > boardConstraints.getSizeX() || s.getLength() > boardConstraints.getSizeY());
        if (isAnyShipTooLong) return false;
        int maxCells = boardConstraints.getSizeX() * boardConstraints.getSizeY();
        int shipCells = ships.stream().mapToInt(Ship::getLength).sum();
        int newShipCells = newShips.stream().mapToInt(Ship::getLength).sum();
        return shipCells + newShipCells < maxCells;
    }

    public void addShips(List<Ship> newShips) {
        if (canAddShips(newShips)) ships.addAll(newShips);
    }

//    private int minimumDistanceBetweenShips;
//
//    public int getMinimumDistanceBetweenShips() {
//        return minimumDistanceBetweenShips;
//    }

//    public int getShipsCountLimit() {
//        return shipCounts.stream().mapToInt(Ship::getVerticalLength).sum();
//    }
}

