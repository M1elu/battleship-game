package lab.cleancode.engine.board;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.FieldState;
import lab.cleancode.engine.Ship;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class PlayerBoard {

    private BoardConstraints constraints;
    private List<Ship> battleships;
    private FieldState[][] stateBoard;
    private FieldState[][] shotBoard;

    public PlayerBoard(BoardConfiguration boardConfiguration) {
        BoardConstraints boardConstraints = boardConfiguration.getBoardConstraints();
        this.constraints = boardConstraints;
        List<Ship> ships = boardConfiguration.getShips();
        this.battleships = ships;
        int boardSizeX = boardConstraints.getSizeX();
        int boardSizeY = boardConstraints.getSizeY();
        this.stateBoard = new FieldState[boardSizeX][boardSizeY];
        this.shotBoard = new FieldState[boardSizeX][boardSizeY];
        Arrays.stream(stateBoard).forEach(s -> Arrays.fill(s, FieldState.Idle));
        Arrays.stream(shotBoard).forEach(s -> Arrays.fill(s, FieldState.Idle));
        ships.stream().flatMap(ship ->
                ship.getCoordinates().stream()
        ).forEach(shipCoordinate ->
                stateBoard[shipCoordinate.getX()][shipCoordinate.getY()] = FieldState.Ship
        );
    }

    public FieldState[][] getStateBoard() {
        FieldState[][] copy = new FieldState[constraints.getSizeX()][constraints.getSizeY()];
        IntStream.range(0, stateBoard.length).forEach(i ->
                copy[i] = Arrays.copyOf(stateBoard[i], stateBoard[i].length)
        );
        return copy;
    }

    public FieldState[][] getShotBoard() {
        FieldState[][] copy = new FieldState[constraints.getSizeX()][constraints.getSizeY()];
        IntStream.range(0, shotBoard.length).forEach(i ->
                copy[i] = Arrays.copyOf(shotBoard[i], shotBoard[i].length)
        );
        return copy;
    }

    public BoardConstraints getConstraints() {
        return constraints;
    }

    public void shoot(Coordinate coordinate) {
        Optional<Ship> ship = battleships.stream().filter(s ->
                s.getCoordinates().stream().anyMatch(c ->
                        c.getX() == coordinate.getX()
                                && c.getY() == coordinate.getY()
                )
        ).findAny();
        if (ship.isPresent()) {
            hitShip(ship.get(), coordinate);
        } else {
            shotBoard[coordinate.getX()][coordinate.getY()] = FieldState.Miss;
        }
    }

    private void hitShip(Ship ship, Coordinate coordinate) {
        ship.hit();
        if (ship.isSunk()) {
            markShipAsSunk(ship, stateBoard);
            markShipAsSunk(ship, shotBoard);
        } else {
            stateBoard[coordinate.getX()][coordinate.getY()] = FieldState.Hit;
            shotBoard[coordinate.getX()][coordinate.getY()] = FieldState.Hit;
        }
    }

    private void markShipAsSunk(Ship ship, FieldState[][] board) {
        ship.getCoordinates().forEach(c ->
                board[c.getX()][c.getY()] = FieldState.Sunk
        );
    }

    public int getNumberOfBattleshipsLeft() {
        return (int) battleships.stream().filter(s -> !s.isSunk()).count();
    }

    public boolean isCoordinateWithinBoard(Coordinate coordinate) {
        return coordinate.getX() >= 0
                && coordinate.getX() <= constraints.getSizeX() - 1
                && coordinate.getY() >= 0
                && coordinate.getY() <= constraints.getSizeY() - 1;
    }

    public boolean isCoordinateAlreadyHit(Coordinate coordinate) {
        FieldState fieldState = stateBoard[coordinate.getX()][coordinate.getY()];
        return fieldState == FieldState.Hit
                || fieldState == FieldState.Sunk
                || fieldState == FieldState.Miss;
    }
}
