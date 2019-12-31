package lab.cleancode.view;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.board.BoardConfiguration;
import lab.cleancode.engine.board.BoardConfigurationUtils;
import lab.cleancode.engine.board.BoardConstraints;
import lab.cleancode.engine.ships.Battleship;
import lab.cleancode.engine.ships.Carrier;
import lab.cleancode.engine.ships.Cruiser;
import lab.cleancode.engine.ships.Destroyer;
import lab.cleancode.engine.ships.Ship;

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
        Carrier carrier = createExampleCarrier();
        Battleship battleship = createExampleBattleship();
        Cruiser cruiser = createExampleCruiser();
        Destroyer destroyer = createExampleDestroyer();
        return List.of(carrier, battleship, cruiser, destroyer);
    }

    public static Carrier createExampleCarrier() {
        Carrier carrier = new Carrier();
        Coordinate startCoordinate = new Coordinate(0, 0);
        ArrayList<Coordinate> coordinates = generateCoordinates(
                startCoordinate,
                true,
                carrier.getLength()
        );
        carrier.setCoordinates(coordinates);
        return carrier;
    }

    public static Battleship createExampleBattleship() {
        Battleship battleship = new Battleship();
        Coordinate startCoordinate = new Coordinate(2, 0);
        ArrayList<Coordinate> coordinates = generateCoordinates(
                startCoordinate,
                false,
                battleship.getLength()
        );
        battleship.setCoordinates(coordinates);
        return battleship;
    }

    public static Cruiser createExampleCruiser() {
        Cruiser cruiser = new Cruiser();
        Coordinate startCoordinate = new Coordinate(2, 2);
        ArrayList<Coordinate> coordinates = generateCoordinates(
                startCoordinate,
                false,
                cruiser.getLength()
        );
        cruiser.setCoordinates(coordinates);
        return cruiser;
    }

    public static Destroyer createExampleDestroyer() {
        Destroyer destroyer = new Destroyer();
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
                new Carrier(),
                new Battleship(),
                new Cruiser(),
                new Destroyer()
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
