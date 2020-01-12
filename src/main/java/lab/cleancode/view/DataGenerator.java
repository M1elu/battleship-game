package lab.cleancode.view;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.board.BoardConfiguration;
import lab.cleancode.engine.board.BoardConstraints;
import lab.cleancode.engine.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static lab.cleancode.engine.board.BoardConfigurationUtils.isCapableToAddShips;

public class DataGenerator {

    private DataGenerator() {
    }

    public static List<Ship> getRandomShips() {
        Random random = new Random();
        List<Ship> randomShips = new ArrayList<>();
        List<Ship> defaultShips = getDefaultShips();
        for (Ship defaultShip : defaultShips) {
            int count = random.nextInt() % 4;
            for (int j = 0; j < count; j++) {
                randomShips.add(defaultShip.clone());
            }
        }
        return randomShips;
    }

    public static BoardConstraints getRandomBoardConstraints() {
        Random random = new Random();
        int minSize = 5;
        int maxSizeX = 10;
        int maxSizeY = 10;
        int sizeX = random.nextInt(maxSizeX - minSize) + minSize;
        int sizeY = random.nextInt(maxSizeY - minSize) + minSize;
        return new BoardConstraints(sizeX, sizeY);
    }

    public static BoardConfiguration getDefaultBoardConfiguration() {
        int sizeX = 10;
        int sizeY = 10;
        BoardConstraints boardConstraints = new BoardConstraints(sizeX, sizeY);
        BoardConfiguration boardConfiguration = new BoardConfiguration(boardConstraints);
        boardConfiguration.setShips(getExampleShips());
        return boardConfiguration;
    }

    public static List<Ship> getExampleShips() {
        Ship carrier = createExampleCarrier();
        Ship battleship = createExampleBattleship();
        Ship cruiser = createExampleCruiser();
        Ship destroyer = createExampleDestroyer();
        return List.of(carrier, battleship, cruiser, destroyer);
    }

    public static Ship createExampleCarrier() {
        Ship carrier = new Ship("Carrier", 5);
        Coordinate startCoordinate = new Coordinate(0, 0);
        ArrayList<Coordinate> coordinates = generateCoordinates(
                startCoordinate,
                true,
                carrier.getLength()
        );
        carrier.setCoordinates(coordinates);
        return carrier;
    }

    public static Ship createExampleBattleship() {
        Ship battleship = new Ship("Battleship", 4);
        Coordinate startCoordinate = new Coordinate(2, 0);
        ArrayList<Coordinate> coordinates = generateCoordinates(
                startCoordinate,
                false,
                battleship.getLength()
        );
        battleship.setCoordinates(coordinates);
        return battleship;
    }

    public static Ship createExampleCruiser() {
        Ship cruiser = new Ship("Cruiser", 3);
        Coordinate startCoordinate = new Coordinate(2, 2);
        ArrayList<Coordinate> coordinates = generateCoordinates(
                startCoordinate,
                false,
                cruiser.getLength()
        );
        cruiser.setCoordinates(coordinates);
        return cruiser;
    }

    public static Ship createExampleDestroyer() {
        Ship destroyer = new Ship("Destroyer", 2);
        Coordinate startCoordinate = new Coordinate(2, 4);
        ArrayList<Coordinate> coordinates = generateCoordinates(
                startCoordinate,
                false,
                destroyer.getLength()
        );
        destroyer.setCoordinates(coordinates);
        return destroyer;
    }

    public static List<Ship> getDefaultShips() {
        return List.of(
                new Ship("Carrier", 5),
                new Ship("Battleship", 4),
                new Ship("Cruiser", 3),
                new Ship("Destroyer", 2)
        );
    }

    public static ArrayList<Coordinate> generateCoordinates(
            Coordinate startCoordinate,
            boolean isPlacedHorizontal,
            int shipLength
    ) {
        ArrayList<Coordinate> newCoordinates = new ArrayList<>();
        newCoordinates.add(startCoordinate);
        for (int i = 1; i <= shipLength - 1; i++) {
            if (isPlacedHorizontal) {
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

    public static BoardConfiguration getRandomBoardConfiguration() {
        BoardConstraints boardConstraints = getRandomBoardConstraints();
        BoardConfiguration boardConfiguration = new BoardConfiguration(boardConstraints);
        boolean isBoardOverflow;
        do {
            List<Ship> ships = getRandomShips();
            isBoardOverflow = !isCapableToAddShips(ships, boardConstraints);
            if (!isBoardOverflow) {
                boardConfiguration.setShips(ships);
                setRandomCoordinates(boardConfiguration);
            }
        } while (isBoardOverflow);
        return boardConfiguration;
    }

    private static void setRandomCoordinates(BoardConfiguration boardConfiguration) {
        Random random = new Random();
        List<Ship> ships = boardConfiguration.getShips();
        for (Ship ship : ships) {
            boolean areCoordinatesCorrect = false;
            while (!areCoordinatesCorrect) {
                boolean isPlacedHorizontal = random.nextBoolean();
                int boardSizeX = boardConfiguration.getBoardConstraints().getSizeX();
                int boardSizeY = boardConfiguration.getBoardConstraints().getSizeY();
                int xCoordinate = random.nextInt(boardSizeX - 1);
                int yCoordinate = random.nextInt(boardSizeY - 1);
                Coordinate startCoordinate = new Coordinate(xCoordinate, yCoordinate);
                ArrayList<Coordinate> coordinates = generateCoordinates(
                        startCoordinate,
                        isPlacedHorizontal,
                        ship.getLength()
                );
                areCoordinatesCorrect = boardConfiguration.canSetCoordinates(coordinates);
                if (areCoordinatesCorrect) {
                    ship.setCoordinates(coordinates);
                }
            }
        }
    }
}
