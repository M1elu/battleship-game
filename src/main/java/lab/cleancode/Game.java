package lab.cleancode;

import lab.cleancode.engine.*;
import lab.cleancode.engine.board.BoardConfiguration;
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
            PlayerBoard playerBoard = setup();
            System.out.println("State board:");
            displayBoard(playerBoard.getStateBoard(), playerBoard.getConstraints());
            System.out.println("Let's play!");
            while (playerBoard.getNumberOfBattleshipsLeft() > 0) {
                play(playerBoard);
            }
            end(playerBoard.getShotBoard());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private PlayerBoard setup() throws CloneNotSupportedException {
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

    private BoardConstraints readBoardConstraints() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Let's configure board!");
        System.out.println("Enter size X of board:");
        int sizeX = reader.nextInt();
        System.out.println("Enter size Y of board:");
        int sizeY = reader.nextInt();
        return new BoardConstraints(sizeX, sizeY);
    }

    private List<Ship> getDefaultShips() {
        return List.of(
                new Carrier(),
                new Battleship(),
                new Cruiser(),
                new Destroyer()
        );
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

    private void setShipCoordinates(BoardConfiguration boardConfiguration) {
        Scanner reader = new Scanner(System.in);
        List<Ship> ships = boardConfiguration.getShips();
        for (Ship ship : ships) {
            boolean isCoordinateCorrect = false;
            while (!isCoordinateCorrect) {
                System.out.println("Do you want place " + ship.getName() + " horizontally(true) or vertically(false)?");
                boolean isPlacedHorizontal = reader.nextBoolean();
                System.out.println("Enter x coordinate for " + ship.getName());
                int xCoordinate = reader.nextInt();
                System.out.println("Enter y coordinate for " + ship.getName());
                int yCoordinate = reader.nextInt();
                Coordinate startCoordinate = new Coordinate(xCoordinate, yCoordinate);
                isCoordinateCorrect = boardConfiguration.canSetCoordinates(startCoordinate, isPlacedHorizontal, ship.getLength());
                if (isCoordinateCorrect) {
                    ship.setCoordinates(startCoordinate, isPlacedHorizontal);
                } else {
                    System.out.println("It's not possible to place ship this way. Wrong coordinate!");
                }
            }
        }
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
                } else {
                    System.out.print("S ");
                }
            }
            System.out.println();
        }
    }

    private void play(PlayerBoard playerBoard) {
        boolean isCoordinateWithinBoard = false;
        Scanner reader = new Scanner(System.in);
        Coordinate shotCoordinate = null;
        while (!isCoordinateWithinBoard) {
            System.out.println("Enter shot x coordinate");
            int xCoordinate = reader.nextInt();
            System.out.println("Enter shot y coordinate");
            int yCoordinate = reader.nextInt();
            shotCoordinate = new Coordinate(xCoordinate, yCoordinate);
            isCoordinateWithinBoard = playerBoard.isCoordinateWithinBoard(shotCoordinate);
            if (!isCoordinateWithinBoard) {
                System.out.println("Coordinate out of board!");
            }
        }
        playerBoard.shoot(shotCoordinate);
        System.out.println("Shot board");
        displayBoard(playerBoard.getShotBoard(), playerBoard.getConstraints());
    }

    private void end(FieldState[][] shotBoard) {
        System.out.println("You sank all ships!");
        ShotStatistics shotStatistics = new ShotStatistics(shotBoard);
        displayStatistics(shotStatistics);
    }

    private void displayStatistics(ShotStatistics shotStatistics) {
        System.out.println("All shots: " + shotStatistics.getAllShotsCount());
        System.out.println("Missed shots: " + shotStatistics.getMissedShotsCount());
    }

    private BoardConfiguration getCustomBoardConfiguration() throws CloneNotSupportedException {
        BoardConstraints boardConstraints = readBoardConstraints();
        BoardConfiguration boardConfiguration = new BoardConfiguration(boardConstraints);
        Boolean isConfigurationCorrect;
        do {
            List<Ship> ships = readShips();
            isConfigurationCorrect = boardConfiguration.canAddShips(ships);
            if (!isConfigurationCorrect) {
                System.out.println("Board capacity is full. Add ships again!");
            } else {
                boardConfiguration.addShips(ships);
            }
        } while (!isConfigurationCorrect);
        setShipCoordinates(boardConfiguration);
        return boardConfiguration;
    }

    private BoardConfiguration getDefaultBoardConfiguration() {
        int defaultSizeX = 10;
        int defaultSizeY = 10;
        BoardConstraints defaultBoardConstraints = new BoardConstraints(defaultSizeX, defaultSizeY);
        BoardConfiguration boardConfiguration = new BoardConfiguration(defaultBoardConstraints);
        Carrier carrier = new Carrier();
        carrier.setCoordinates(new Coordinate(0, 0), true);
        Battleship battleship = new Battleship();
        battleship.setCoordinates(new Coordinate(2, 0), false);
        Cruiser cruiser = new Cruiser();
        cruiser.setCoordinates(new Coordinate(2, 2), false);
        Destroyer destroyer = new Destroyer();
        destroyer.setCoordinates(new Coordinate(2, 4), false);
        List<Ship> ships = List.of(carrier, battleship, cruiser, destroyer);
        boardConfiguration.addShips(ships);
        return boardConfiguration;
    }

    private BoardConfiguration getRandomBoardConfiguration() throws CloneNotSupportedException {
        Random random = new Random();
        int minSize = 5;
        int maxSizeX = 15;
        int maxSizeY = 15;
        int sizeX = random.nextInt(maxSizeX - minSize) + minSize;
        int sizeY = random.nextInt(maxSizeY - minSize) + minSize;
        BoardConstraints randomBoardConstraints = new BoardConstraints(sizeX, sizeY);
        BoardConfiguration randomBoardConfiguration = new BoardConfiguration(randomBoardConstraints);
        List<Ship> ships = new ArrayList<>();
        Boolean isConfigurationCorrect;
        do {
            List<Ship> defaultShips = getDefaultShips();
            for (Ship defaultShip : defaultShips) {
                int count = random.nextInt() % 4;
                for (int j = 0; j < count; j++) {
                    ships.add(defaultShip.clone());
                }
            }
            isConfigurationCorrect = randomBoardConfiguration.canAddShips(ships);
            if (!isConfigurationCorrect) {
                System.out.println("Board capacity is full. Add ships again!");
            } else {
                randomBoardConfiguration.addShips(ships);
            }
        } while (!isConfigurationCorrect);
        for (Ship ship : ships) {
            boolean isCoordinateCorrect = false;
            while (!isCoordinateCorrect) {
                boolean isPlacedHorizontal = random.nextBoolean();
                int xCoordinate = random.nextInt(sizeX - 1);
                int yCoordinate = random.nextInt(sizeY - 1);
                Coordinate startCoordinate = new Coordinate(xCoordinate, yCoordinate);
                isCoordinateCorrect = randomBoardConfiguration.canSetCoordinates(startCoordinate, isPlacedHorizontal, ship.getLength());
                if (isCoordinateCorrect) {
                    ship.setCoordinates(startCoordinate, isPlacedHorizontal);
                }
            }
        }
        return randomBoardConfiguration;
    }
}
