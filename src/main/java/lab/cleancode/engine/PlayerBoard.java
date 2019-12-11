package lab.cleancode.engine;

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

    private List<Ship> battleships;
    private FieldState[][] stateBoard;

    public FieldState[][] getBoardState() {
        FieldState[][] copy = new FieldState[stateBoard.length][stateBoard.length];
        IntStream.range(0, stateBoard.length).forEach(i -> copy[i] = Arrays.copyOf(stateBoard[i], stateBoard[i].length));
        return copy;
    }

    private PlayerBoard(BoardConfiguration boardConfiguration) {
        stateBoard = new FieldState[boardConfiguration.getBoardConstraints().getSizeX()][boardConfiguration.getBoardConstraints().getSizeY()];
        Arrays.stream(stateBoard).forEach(s -> Arrays.fill(s, FieldState.Idle));
        boardConfiguration.getShips().stream()
                .flatMap(b -> b.getCoordinates().stream())
                .forEach(coordinate ->
                        stateBoard[coordinate.getX()][coordinate.getY()] = FieldState.Hit
                );

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
                markShipAsSunk(ship.get());
            } else {
                stateBoard[coordinate.getX()][coordinate.getY()] = FieldState.Hit;
            }
        } else {
            stateBoard[coordinate.getX()][coordinate.getY()] = FieldState.Miss;
        }
    }

    private void markShipAsSunk(Ship ship) {
        ship.getCoordinates().forEach(
                c -> stateBoard[c.getX()][c.getY()] = FieldState.Sunk
        );
    }

    public int getNumberOfBattleshipsLeft() {
        return (int) battleships.stream().filter(s -> !s.isSunk()).count();
    }
}
