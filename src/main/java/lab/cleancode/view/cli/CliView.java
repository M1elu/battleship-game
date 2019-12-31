package lab.cleancode.view.cli;

import lab.cleancode.engine.Coordinate;
import lab.cleancode.engine.FieldState;
import lab.cleancode.engine.ShotStatistics;
import lab.cleancode.engine.board.BoardConfiguration;
import lab.cleancode.engine.board.BoardConfigurationUtils;
import lab.cleancode.engine.board.BoardConstraints;
import lab.cleancode.engine.board.PlayerBoard;
import lab.cleancode.engine.ships.Ship;
import lab.cleancode.view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static lab.cleancode.view.DataGenerator.generateCoordinates;
import static lab.cleancode.view.DataGenerator.getDefaultBoardConfiguration;
import static lab.cleancode.view.DataGenerator.getDefaultShips;
import static lab.cleancode.view.DataGenerator.getRandomBoardConfiguration;

public class CliView implements GameView {

    private final Scanner reader;

    public CliView() {
        this.reader = new Scanner(System.in);
    }

    @Override
    public void displayBoard(FieldState[][] board, BoardConstraints constraints) {
        System.out.println("State board:");
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

    @Override
    public void displayEnd(FieldState[][] shotBoard, BoardConstraints boardConstraints) {
        System.out.println("You sank all ships!");
        ShotStatistics shotStatistics = new ShotStatistics(shotBoard, boardConstraints);
        displayStatistics(shotStatistics);
    }

    @Override
    public void displayShotResult(boolean isCoordinateWithinBoard, boolean isCoordinateAlreadyHit) {
        if (!isCoordinateWithinBoard) {
            System.out.println("Coordinate out of board!");
        } else if (isCoordinateAlreadyHit) {
            System.out.println("You have already hit this coordinate!");
        }
    }

    @Override
    public PlayerBoard readBoardFromPlayer() {
        System.out.println("Choose setup type (custom(c), default(d), random(r)): ");
        String setupType = reader.next();
        BoardConfiguration boardConfiguration;
        switch (setupType) {
            case "c":
                boardConfiguration = readCustomBoardConfiguration();
                break;
            case "r":
                boardConfiguration = getRandomBoardConfiguration();
                break;
            default:
                boardConfiguration = getDefaultBoardConfiguration();
        }
        return new PlayerBoard(boardConfiguration);
    }

    @Override
    public Coordinate readShotCoordinate() {
        System.out.println("Enter shot x coordinate");
        int xCoordinate = reader.nextInt();
        System.out.println("Enter shot y coordinate");
        int yCoordinate = reader.nextInt();
        return new Coordinate(xCoordinate, yCoordinate);
    }


    private void displayStatistics(ShotStatistics shotStatistics) {
        System.out.println("All shots: " + shotStatistics.getAllShotsCount());
        System.out.println("Missed shots: " + shotStatistics.getMissedShotsCount());
    }

    private BoardConstraints readBoardConstraints() {
        System.out.println("Let's configure board!");
        System.out.println("Enter size X of board:");
        int sizeX = reader.nextInt();
        System.out.println("Enter size Y of board:");
        int sizeY = reader.nextInt();
        return new BoardConstraints(sizeX, sizeY);
    }

    private List<Ship> readShips() {
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

    private BoardConfiguration readCustomBoardConfiguration() {
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

    private void readShipCoordinates(BoardConfiguration boardConfiguration) {
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
}
