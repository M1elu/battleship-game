package lab.cleancode.engine.board;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.FieldState;
import lab.cleancode.engine.ships.Ship;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class PlayerBoard {

    public static PlayerBoard setup(BoardConfiguration boardConfiguration) {
        if (boardConfiguration == null) {
            boardConfiguration = BoardConfiguration.getDefault();
        }
        return new PlayerBoard(boardConfiguration);
    }

    private BoardConstraints constraints;
    private List<Ship> battleships;
    private FieldState[][] stateBoard;
    private FieldState[][] shotBoard;

    public FieldState[][] getStateBoard() {
        FieldState[][] copy = new FieldState[stateBoard.length][stateBoard.length];
        IntStream.range(0, stateBoard.length).forEach(i -> copy[i] = Arrays.copyOf(stateBoard[i], stateBoard[i].length));
        return copy;
    }

    public FieldState[][] getShotBoard() {
        FieldState[][] copy = new FieldState[shotBoard.length][shotBoard.length];
        IntStream.range(0, shotBoard.length).forEach(i -> copy[i] = Arrays.copyOf(shotBoard[i], shotBoard[i].length));
        return copy;
    }

    private PlayerBoard(BoardConfiguration boardConfiguration) {
        this.stateBoard = new FieldState[boardConfiguration.getBoardConstraints().getSizeX()][boardConfiguration.getBoardConstraints().getSizeY()];
        Arrays.stream(stateBoard).forEach(s -> Arrays.fill(s, FieldState.Idle));
        this.shotBoard = new FieldState[boardConfiguration.getBoardConstraints().getSizeX()][boardConfiguration.getBoardConstraints().getSizeY()];
        Arrays.stream(shotBoard).forEach(s -> Arrays.fill(s, FieldState.Idle));
        boardConfiguration.getShips().stream()
                .flatMap(b -> b.getCoordinates().stream())
                .forEach(coordinate ->
                        stateBoard[coordinate.getX()][coordinate.getY()] = FieldState.Ship
                );
        this.constraints = boardConfiguration.getBoardConstraints();
        this.battleships = boardConfiguration.getShips();
    }

    public void shoot(Coordinate coordinate) {
        Optional<Ship> ship = battleships.stream().filter(s ->
                s.getCoordinates().stream().anyMatch(
                        c -> c.getX() == coordinate.getX() && c.getY() == coordinate.getY()
                )
        ).findAny();
        if (ship.isPresent()) {
            ship.get().hit();
            if (ship.get().isSunk()) {
                markShipAsSunk(ship.get(), stateBoard);
                markShipAsSunk(ship.get(), shotBoard);
            } else {
                stateBoard[coordinate.getX()][coordinate.getY()] = FieldState.Hit;
                shotBoard[coordinate.getX()][coordinate.getY()] = FieldState.Hit;
            }
        } else {
            shotBoard[coordinate.getX()][coordinate.getY()] = FieldState.Miss;
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
        return coordinate.getX() >= 0 && coordinate.getX() <= constraints.getSizeX() - 1
                && coordinate.getY() >= 0 && coordinate.getY() <= constraints.getSizeY() - 1;
    }
}
