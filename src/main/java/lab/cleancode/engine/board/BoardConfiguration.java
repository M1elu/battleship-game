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

    public boolean canSetCoordinates(List<Coordinate> coordinates) {
        return !isAnyCoordinateOutOfBoard(coordinates)
                && !isAnyCoordinateAlreadyUsed(coordinates)
                && !isAnyAdjacentCoordinateUsed(coordinates);
    }

    private boolean isAnyCoordinateOutOfBoard(List<Coordinate> coordinates) {
        return coordinates.stream().anyMatch(coordinate ->
                coordinate.getX() < 0 || coordinate.getX() > boardConstraints.getSizeX() - 1
                        || coordinate.getY() < 0 || coordinate.getY() > boardConstraints.getSizeY() - 1
        );
    }

    private boolean isAnyCoordinateAlreadyUsed(List<Coordinate> coordinates) {
        return ships.stream().anyMatch(s ->
                s.getCoordinates().stream().anyMatch(c ->
                        coordinates.stream().anyMatch(newC ->
                                newC.getX() == c.getX() && newC.getY() == c.getY()
                        )
                )
        );
    }

    private boolean isAnyAdjacentCoordinateUsed(List<Coordinate> coordinates) {
        return ships.stream().anyMatch(s ->
                s.getCoordinates().stream().anyMatch(c ->
                        coordinates.stream().anyMatch(newC ->
                                (newC.getX() - 1 == c.getX() && newC.getY() == c.getY())
                                        || (newC.getX() == c.getX() && newC.getY() + 1 == c.getY())
                                        || (newC.getX() == c.getX() && newC.getY() - 1 == c.getY())
                                        || (newC.getX() + 1 == c.getX() && newC.getY() == c.getY())
                                        || (newC.getX() - 1 == c.getX() && newC.getY() - 1 == c.getY())
                                        || (newC.getX() - 1 == c.getX() && newC.getY() + 1 == c.getY())
                                        || (newC.getX() + 1 == c.getX() && newC.getY() + 1 == c.getY())
                                        || (newC.getX() + 1 == c.getX() && newC.getY() - 1 == c.getY())
                        )
                )
        );
    }

//    private int minimumDistanceBetweenShips;
//
//    public int getMinimumDistanceBetweenShips() {
//        return minimumDistanceBetweenShips;
//    }
}

