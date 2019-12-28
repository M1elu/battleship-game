package lab.cleancode.engine.board;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class BoardConfiguration {

    final private BoardConstraints boardConstraints;
    final private List<Ship> ships;

    public BoardConfiguration(BoardConstraints boardConstraints) {
        this.boardConstraints = boardConstraints;
        this.ships = new ArrayList<>();
    }

    public Boolean canAddShips(List<Ship> newShips) {
        boolean isAnyShipTooLong = isAnyShipTooLong(newShips);
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

    private boolean isAnyShipTooLong(List<Ship> newShips) {
        return newShips.stream().anyMatch(s ->
                s.getLength() > boardConstraints.getSizeX()
                        || s.getLength() > boardConstraints.getSizeY()
        );
    }

    public boolean canSetCoordinates(List<Coordinate> coordinates) {
        return !isAnyCoordinateOutOfBoard(coordinates)
                && !isAnyCoordinateAlreadyUsed(coordinates)
                && !isAnyAdjacentCoordinateUsed(coordinates);
    }

    private boolean isAnyCoordinateOutOfBoard(List<Coordinate> coordinates) {
        return coordinates.stream().anyMatch(coordinate ->
                coordinate.getX() < 0
                        || coordinate.getX() > boardConstraints.getSizeX() - 1
                        || coordinate.getY() < 0
                        || coordinate.getY() > boardConstraints.getSizeY() - 1
        );
    }

    private boolean isAnyCoordinateAlreadyUsed(List<Coordinate> coordinates) {
        return ships.stream().anyMatch(ship ->
                ship.getCoordinates().stream().anyMatch(coordinate ->
                        coordinates.stream().anyMatch(newCoordinate ->
                                newCoordinate.getX() == coordinate.getX()
                                        && newCoordinate.getY() == coordinate.getY()
                        )
                )
        );
    }

    private boolean isAnyAdjacentCoordinateUsed(List<Coordinate> coordinates) {
        return ships.stream().anyMatch(ship ->
                ship.getCoordinates().stream().anyMatch(coordinate ->
                        coordinates.stream().anyMatch(newCoordinate ->
                                isAdjacentCoordinateUsed(coordinate, newCoordinate)
                        )
                )
        );
    }

    private boolean isAdjacentCoordinateUsed(Coordinate c, Coordinate newC) {
        return (newC.getX() - 1 == c.getX() && newC.getY() == c.getY())
                || (newC.getX() == c.getX() && newC.getY() + 1 == c.getY())
                || (newC.getX() == c.getX() && newC.getY() - 1 == c.getY())
                || (newC.getX() + 1 == c.getX() && newC.getY() == c.getY())
                || (newC.getX() - 1 == c.getX() && newC.getY() - 1 == c.getY())
                || (newC.getX() - 1 == c.getX() && newC.getY() + 1 == c.getY())
                || (newC.getX() + 1 == c.getX() && newC.getY() + 1 == c.getY())
                || (newC.getX() + 1 == c.getX() && newC.getY() - 1 == c.getY());
    }
}

