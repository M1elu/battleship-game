package lab.cleancode.engine.board;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class BoardConfiguration {

    public BoardConfiguration(BoardConstraints boardConstraints) {
        this.boardConstraints = boardConstraints;
        this.ships = new ArrayList<>();
    }

    private BoardConstraints boardConstraints;
    private List<Ship> ships;

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

    public BoardConstraints getBoardConstraints() {
        return boardConstraints;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public boolean canSetCoordinates(Coordinate startCoordinate, boolean isPlacedHorizontal, int shipLength) {
        if (startCoordinate.getX() > boardConstraints.getSizeX()
                || startCoordinate.getY() > boardConstraints.getSizeY()) {
            return false;
        }
        if (isCoordinateAlreadyUsed(startCoordinate)) {
            return false;
        }
        if (isPlacedHorizontal) {
            return startCoordinate.getY() <= boardConstraints.getSizeY() - shipLength;
        } else {
            return startCoordinate.getX() <= boardConstraints.getSizeX() - shipLength;
        }
    }

    private boolean isCoordinateAlreadyUsed(Coordinate startCoordinate) {
        return ships.stream().anyMatch(s ->
                s.getCoordinates().stream().anyMatch(c ->
                        c.getX() == startCoordinate.getX() && c.getY() == startCoordinate.getY()
                )
        );
    }

//    private int minimumDistanceBetweenShips;
//
//    public int getMinimumDistanceBetweenShips() {
//        return minimumDistanceBetweenShips;
//    }
}

