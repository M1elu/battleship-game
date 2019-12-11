package lab.cleancode.engine;

import lab.cleancode.engine.ships.Ship;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class PlayerBoard {

    public static PlayerBoard setup(BoardConfiguration boardConfiguration) throws InstantiationException {
        if (boardConfiguration == null)
            boardConfiguration = BoardConfiguration.getDefault();

        //validate setup
        // check if ships are in range of board
        // check if ships are in one line without breaks

        //
        // check board constraints
        // check if ship counts is ok
        // check if spaces between ships are ok


        boolean isValid = true;

        if (!isValid)
            throw new InstantiationException("Cannot initialize board with given setup.");

        return new PlayerBoard(boardConfiguration.getShips());
    }

    private List<Ship> battleships;
    private FieldState[][] stateBoard;

    public FieldState[][] getBoardState() {
        FieldState[][] copy = new FieldState[stateBoard.length][stateBoard.length];
        IntStream.range(0, stateBoard.length).forEach(i -> copy[i] = Arrays.copyOf(stateBoard[i], stateBoard[i].length));
        return copy;
    }

    public PlayerBoard(List<Ship> battleships, BoardConstraints boardConstraints) {
        this(boardConstraints);
        battleships.stream()
                .flatMap(b -> b.getCoordinates().stream())
                .forEach(coordinate ->
                        stateBoard[coordinate.x][coordinate.y] = FieldState.Hit
                );
        this.battleships = battleships;
    }

    private PlayerBoard(BoardConstraints boardConstraints) {
        stateBoard = new FieldState[boardConstraints.getSizeX()][boardConstraints.getSizeY()];
        Arrays.stream(stateBoard).forEach(s -> Arrays.fill(s, FieldState.Idle));
    }

    public void shoot(Coordinate coordinate) {
        Optional<Ship> ship = battleships.stream().filter(s ->
                s.getCoordinates().stream().anyMatch(c -> c.x == coordinate.x && c.y == coordinate.y)
        ).findAny();
        if (ship.isPresent()) {
            ship.get().hit(coordinate);
            if (ship.get().isSunk()) {
                markShipAsSunk(ship.get());
            } else {
                stateBoard[coordinate.x][coordinate.y] = FieldState.Hit;
            }
        } else {
            stateBoard[coordinate.x][coordinate.y] = FieldState.Miss;
        }
    }

    private void markShipAsSunk(Ship ship) {
        ship.getCoordinates().forEach(
                c -> stateBoard[c.x][c.y] = FieldState.Sunk
        );
    }

    public int getNumberOfBattleshipsLeft() {
        return (int) battleships.stream().filter(s -> !s.isSunk()).count();
    }
}
