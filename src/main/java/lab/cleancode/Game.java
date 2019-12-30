package lab.cleancode;

import lab.cleancode.engine.*;
import lab.cleancode.engine.board.BoardConfiguration;
import lab.cleancode.engine.board.BoardConfigurationUtils;
import lab.cleancode.engine.board.BoardConstraints;
import lab.cleancode.engine.board.PlayerBoard;
import lab.cleancode.engine.ships.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public void start() {
        try {
            PlayerBoard playerBoard = setupBoard();
            System.out.println("State board:");
            displayBoard(playerBoard.getStateBoard(), playerBoard.getConstraints());
            System.out.println("Let's play!");
            while (playerBoard.getNumberOfBattleshipsLeft() > 0) {
                play(playerBoard);
            }
            end(playerBoard.getShotBoard(), playerBoard.getConstraints());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private PlayerBoard setupBoard() throws CloneNotSupportedException {
        System.out.println("Choose setup type (custom(c), default(d), random(r)): ");
        Scanner reader = new Scanner(System.in);
        String setupType = reader.next();
        BoardConfiguration boardConfiguration;
        switch (setupType) {
            case "c":
                boardConfiguration = getCustomBoardConfiguration();
                break;
            case "r":
                boardConfiguration = getRandomBoardConfiguration();
                break;
            default:
                boardConfiguration = getDefaultBoardConfiguration();
        }
        return new PlayerBoard(boardConfiguration);
    }

    private BoardConfiguration getCustomBoardConfiguration() throws CloneNotSupportedException {
        BoardConstraints boardConstraints = readBoardConstraints();
        BoardConfiguration boardConfiguration;
        boolean isBoardOverflow;
        do {
            boardConfiguration = new BoardConfiguration(boardConstraints);
            List<Ship> ships = readShips();
            isBoardOverflow = !BoardConfigurationUtils.isCapableToAddShips(
                    ships,
                    boardConstraints
            );
            if (isBoardOverflow) {
                System.out.println("Board capacity is full. Add ships again!");
            } else {
                boardConfiguration.setShips(ships);
                readShipCoordinates(boardConfiguration);
            }
        } while (isBoardOverflow);
        return boardConfiguration;
    }

    private BoardConstraints readBoardConstraints() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Let's configure board!");
        System.out.println("Enter size X of board:");
        int sizeX = reader.nextInt();
        System.out.println("Enter size Y of board:");
        int sizeY = reader.nextInt();
        return new BoardConstraints(sizeX, sizeY);
    }

    private List<Ship> readShips() throws CloneNotSupportedException {
        Scanner reader = new Scanner(System.in);
        List<Ship> defaultShips = getDefaultShips();
        List<Ship> newShips = new ArrayList<>();
        for (Ship defaultShip : defaultShips) {
            System.out.println("How many " + defaultShip.getName() + " ships?");
            int count = reader.nextInt();
            for (int j = 0; j < count; j++) {
                newShips.add(defaultShip.clone());
            }
        }
        return newShips;
    }

    private void readShipCoordinates(BoardConfiguration boardConfiguration) {
        Scanner reader = new Scanner(System.in);
        List<Ship> ships = boardConfiguration.getShips();
        for (Ship ship : ships) {
            boolean areCoordinatesCorrect = false;
            while (!areCoordinatesCorrect) {
                System.out.println("Do you want place " + ship.getName() + " horizontally(true) or vertically(false)?");
                boolean isPlacedHorizontal = reader.nextBoolean();
                System.out.println("Enter x coordinate for " + ship.getName());
                int xCoordinate = reader.nextInt();
                System.out.println("Enter y coordinate for " + ship.getName());
                int yCoordinate = reader.nextInt();
                Coordinate startCoordinate = new Coordinate(xCoordinate, yCoordinate);
                ArrayList<Coordinate> coordinates = generateCoordinates(startCoordinate, isPlacedHorizontal, ship.getLength());
                areCoordinatesCorrect = boardConfiguration.canSetCoordinates(coordinates);
                if (areCoordinatesCorrect) {
                    ship.setCoordinates(coordinates);
                } else {
                    System.out.println("It's not possible to place ship this way. Wrong coordinate!");
                }
            }
        }
    }

    private BoardConfiguration getRandomBoardConfiguration() throws CloneNotSupportedException {
        BoardConstraints boardConstraints = getRandomBoardConstraints();
        BoardConfiguration boardConfiguration = new BoardConfiguration(boardConstraints);
        boolean isBoardOverflow;
        do {
            List<Ship> ships = getRandomShips();
            isBoardOverflow = !BoardConfigurationUtils.isCapableToAddShips(ships, boardConstraints);
            if (isBoardOverflow) {
                System.out.println("Board capacity is full. Add ships again!");
            } else {
                boardConfiguration.setShips(ships);
                setRandomCoordinates(boardConfiguration);
            }
        } while (isBoardOverflow);
        return boardConfiguration;
    }

    private void setRandomCoordinates(BoardConfiguration boardConfiguration) {
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
                ArrayList<Coordinate> coordinates = generateCoordinates(startCoordinate, isPlacedHorizontal, ship.getLength());
                areCoordinatesCorrect = boardConfiguration.canSetCoordinates(coordinates);
                if (areCoordinatesCorrect) {
                    ship.setCoordinates(coordinates);
                }
            }
        }
    }

    private List<Ship> getRandomShips() throws CloneNotSupportedException {
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

    private BoardConstraints getRandomBoardConstraints() {
        Random random = new Random();
        int minSize = 5;
        int maxSizeX = 10;
        int maxSizeY = 10;
        int sizeX = random.nextInt(maxSizeX - minSize) + minSize;
        int sizeY = random.nextInt(maxSizeY - minSize) + minSize;
        return new BoardConstraints(sizeX, sizeY);
    }

    private BoardConfiguration getDefaultBoardConfiguration() {
        int defaultSizeX = 10;
        int defaultSizeY = 10;
        BoardConstraints defaultBoardConstraints =
                new BoardConstraints(defaultSizeX, defaultSizeY);
        BoardConfiguration boardConfiguration =
                new BoardConfiguration(defaultBoardConstraints);
        boardConfiguration.setShips(getExampleShips());
        return boardConfiguration;
    }

    private List<Ship> getExampleShips() {
        Carrier carrier = createExampleCarrier();
        Battleship battleship = createExampleBattleship();
        Cruiser cruiser = createExampleCruiser();
        Destroyer destroyer = createExampleDestroyer();
        return List.of(carrier, battleship, cruiser, destroyer);
    }

    private Carrier createExampleCarrier() {
        Carrier carrier = new Carrier();
        Coordinate carrierStartCoordinate = new Coordinate(0, 0);
        ArrayList<Coordinate> carrierCoordinates = generateCoordinates(
                carrierStartCoordinate,
                true,
                carrier.getLength()
        );
        carrier.setCoordinates(carrierCoordinates);
        return carrier;
    }

    private Battleship createExampleBattleship() {
        Battleship battleship = new Battleship();
        Coordinate battleshipStartCoordinate = new Coordinate(2, 0);
        ArrayList<Coordinate> battleshipCoordinates = generateCoordinates(
                battleshipStartCoordinate,
                false,
                battleship.getLength()
        );
        battleship.setCoordinates(battleshipCoordinates);
        return battleship;
    }

    private Cruiser createExampleCruiser() {
        Cruiser cruiser = new Cruiser();
        Coordinate cruiserStartCoordinate = new Coordinate(2, 2);
        ArrayList<Coordinate> cruiserCoordinates = generateCoordinates(
                cruiserStartCoordinate,
                false,
                cruiser.getLength()
        );
        cruiser.setCoordinates(cruiserCoordinates);
        return cruiser;
    }

    private Destroyer createExampleDestroyer() {
        Destroyer destroyer = new Destroyer();
        Coordinate destroyerStartCoordinate = new Coordinate(2, 4);
        ArrayList<Coordinate> destroyerCoordinates = generateCoordinates(
                destroyerStartCoordinate,
                false,
                destroyer.getLength()
        );
        destroyer.setCoordinates(destroyerCoordinates);
        return destroyer;
    }

    private List<Ship> getDefaultShips() {
        return List.of(
                new Carrier(),
                new Battleship(),
                new Cruiser(),
                new Destroyer()
        );
    }

    private ArrayList<Coordinate> generateCoordinates(Coordinate startCoordinate, boolean isPlacedHorizontal, int shipLength) {
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

    private void displayBoard(FieldState[][] board, BoardConstraints constraints) {
        for (int i = 0; i < constraints.getSizeX(); i++) {
            for (int j = 0; j < constraints.getSizeY(); j++) {
                FieldState state = board[i][j];
                if (state == FieldState.Idle) {
                    System.out.print("I ");
                } else if (state == FieldState.Ship) {
                    System.out.print("S ");
                } else if (state == FieldState.Miss) {
                    System.out.print("M ");
                } else if (state == FieldState.Hit) {
                    System.out.print("H ");
                } else if (state == FieldState.Sunk) {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }

    private void play(PlayerBoard playerBoard) {
        boolean isCoordinateWithinBoard = false;
        boolean isCoordinateAlreadyHit = true;
        Scanner reader = new Scanner(System.in);
        Coordinate shotCoordinate = null;
        while (!isCoordinateWithinBoard || isCoordinateAlreadyHit) {
            System.out.println("Enter shot x coordinate");
            int xCoordinate = reader.nextInt();
            System.out.println("Enter shot y coordinate");
            int yCoordinate = reader.nextInt();
            shotCoordinate = new Coordinate(xCoordinate, yCoordinate);
            isCoordinateWithinBoard = playerBoard.isCoordinateWithinBoard(shotCoordinate);
            if (!isCoordinateWithinBoard) {
                System.out.println("Coordinate out of board!");
            } else {
                isCoordinateAlreadyHit = playerBoard.isCoordinateAlreadyHit(shotCoordinate);
                if (isCoordinateAlreadyHit) {
                    System.out.println("You have already hit this coordinate!");
                }
            }
        }
        playerBoard.shoot(shotCoordinate);
        System.out.println("Shot board");
        displayBoard(playerBoard.getShotBoard(), playerBoard.getConstraints());
    }

    private void end(FieldState[][] shotBoard, BoardConstraints boardConstraints) {
        System.out.println("You sank all ships!");
        ShotStatistics shotStatistics = new ShotStatistics(shotBoard, boardConstraints);
        displayStatistics(shotStatistics);
    }

    private void displayStatistics(ShotStatistics shotStatistics) {
        System.out.println("All shots: " + shotStatistics.getAllShotsCount());
        System.out.println("Missed shots: " + shotStatistics.getMissedShotsCount());
    }
}
