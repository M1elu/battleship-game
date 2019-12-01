package lab.cleancode.engine;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Keeps track of player's shots results and battlefield's state.
 */
public class PlayerBoard {
    /**
     * Instantiates new board if the validation of given constraints is passed.
     *
     * @param battleshipsPositions positions of fields that store battleships
     * @param boardConstraints     parameters defining board validation rules
     * @return instance of board generated from given settings
     * @throws InstantiationException if the validation fails
     */
    public static PlayerBoard setup(Point[][] battleshipsPositions, BoardConstraints boardConstraints) throws InstantiationException {
        if (boardConstraints == null)
            boardConstraints = BoardConstraints.getDefault();

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

        return new PlayerBoard(battleshipsPositions);
    }

    private Point[][] battleships;
    private FieldState[][] stateBoard;
    private ShotResult[][] shotResults;

    /**
     * Gets a copy of current board state.
     *
     * @return Two dimensional array with battlefield data.
     */
    public FieldState[][] getBoardState() {
        FieldState[][] copy = new FieldState[stateBoard.length][stateBoard.length];
        IntStream.range(0, stateBoard.length).forEach(i -> copy[i] = Arrays.copyOf(stateBoard[i], stateBoard[i].length));
        return copy;
    }

    /**
     * Gets a copy of current shots state.
     *
     * @return Two dimensional array with past shots data.
     */
    public ShotResult[][] getShotResults() {
        ShotResult[][] copy = new ShotResult[shotResults.length][shotResults.length];
        IntStream.range(0, shotResults.length).forEach(i -> copy[i] = Arrays.copyOf(shotResults[i], shotResults[i].length));
        return copy;
    }

    /**
     * Creates new 10x10 board instance with battleships on given positions.
     *
     * @param battleshipsPositions positions of fields that store battleships
     */
    public PlayerBoard(Point[][] battleshipsPositions) {
        this(battleshipsPositions, 10);
    }

    /**
     * Creates new board instance with battleships on given positions.
     *
     * @param battleshipsPositions positions of fields that store battleships
     * @param size                 board size
     */
    public PlayerBoard(Point[][] battleshipsPositions, int size) {
        this(size);

        Arrays.stream(battleshipsPositions)
                .flatMap(b -> Arrays.stream(b))
                .forEach(position ->
                        stateBoard[position.x][position.y] = FieldState.BattleshipPart);

        battleships = battleshipsPositions;
    }

    /**
     * Creates new 10x10 board instance from archival state data.
     *
     * @param stateBoard  archival state of player's board
     * @param shotResults archival state of player's shot results
     */
    public PlayerBoard(FieldState[][] stateBoard, ShotResult[][] shotResults) {
        this(stateBoard, shotResults, 10);
    }

    /**
     * Creates new board instance from archival state data.
     *
     * @param stateBoard  archival state of player's board
     * @param shotResults archival state of player's shot results
     * @param size        size of the board
     */
    public PlayerBoard(FieldState[][] stateBoard, ShotResult[][] shotResults, int size) {
        this(size);
        this.stateBoard = stateBoard;
        this.shotResults = shotResults;
    }

    private PlayerBoard(int size) {
        stateBoard = new FieldState[size][size];
        Arrays.stream(stateBoard).forEach(s -> Arrays.fill(s, FieldState.Clear));
        shotResults = new ShotResult[size][size];
        Arrays.stream(shotResults).forEach(s -> Arrays.fill(s, ShotResult.None));
    }

    /**
     * Counts number battleships still alive on tha board.
     *
     * @return number of not-sunk battleships.
     */
    public int getNumberOfBattleshipsLeft() {
        return (int) Arrays
                .stream(stateBoard)
                .flatMap(x -> Arrays.stream(x))
                .filter(f -> f == FieldState.BattleshipPart)
                .count();
    }

    /**
     * Simulates shot on the board.
     *
     * @param coords shot position
     * @return shot state whether it hit, miss or sank a battleship.
     */
    public ShotResult shoot(Point coords) {
        if (stateBoard[coords.x][coords.y] == FieldState.BattleshipPart) {
            stateBoard[coords.x][coords.y] = FieldState.SunkBattleshipPart;
            if (isShipSunk(coords))
                return ShotResult.HitAndSunk;
            //TODO check if whole ship has sunk
            return ShotResult.Hit;
        } else {
            return ShotResult.Miss;
        }
    }

    /**
     * Finds other battleship's parts and determines whether its sunk or not
     *
     * @param coords Battleship's field position
     * @return whether the whole ship is sunk or not
     */
    private boolean isShipSunk(Point coords) {
        Stream<Point> shipParts = Arrays.stream(battleships)
                .filter(b -> Arrays.stream(b).anyMatch(p -> p.x == coords.x && p.y == coords.y)).flatMap(s -> Arrays.stream(s));
        return shipParts.allMatch(s -> stateBoard[s.x][s.y] == FieldState.SunkBattleshipPart);
    }

    /**
     * Marks friendly shot on the board.
     *
     * @param coords shot position
     * @param result type of result that is being marked
     */
    public void markFriendlyShot(Point coords, ShotResult result) {
        shotResults[coords.x][coords.y] = result;
    }
}
