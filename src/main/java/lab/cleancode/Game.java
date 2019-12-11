package lab.cleancode;

import lab.cleancode.engine.*;
import lab.cleancode.engine.board.BoardConfiguration;
import lab.cleancode.engine.board.BoardConstraints;
import lab.cleancode.engine.board.PlayerBoard;
import lab.cleancode.engine.ships.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    public void start() {
        try {
            PlayerBoard playerBoard = setup();
            displayBoard(playerBoard);
            System.out.println("Let's play!");
            while (playerBoard.getNumberOfBattleshipsLeft() > 0) {
                play(playerBoard);
            }
            end();

        } catch (CloneNotSupportedException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private PlayerBoard setup() throws CloneNotSupportedException, InstantiationException {
        BoardConstraints boardConstraints = readBoardConstraints();
        BoardConfiguration boardConfiguration = new BoardConfiguration(boardConstraints);
        Boolean isConfigurationCorrect;
        do {
            List<Ship> ships = readShips();
            boardConfiguration.addShips(ships);
            isConfigurationCorrect = boardConfiguration.canAddShips(ships);
            if (!isConfigurationCorrect) {
                System.out.println("Board capacity is full. Add ships again!");
            }
        } while (!isConfigurationCorrect);
        setShipCoordinates(boardConfiguration);
        return PlayerBoard.setup(boardConfiguration);
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
                System.out.println("Do you want place ship horizontally(true) or vertically(false)?");
                boolean isPlacedHorizontal = reader.nextBoolean();
                System.out.println("Enter x coordinate for " + ship.getName());
                int xCoordinate = reader.nextInt();
                System.out.println("Enter y coordinate for " + ship.getName());
                int yCoordinate = reader.nextInt();
                Coordinate startCoordinate = new Coordinate(xCoordinate, yCoordinate);
                isCoordinateCorrect = ship.canSetCoordinates(startCoordinate, isPlacedHorizontal, boardConfiguration.getBoardConstraints());
                if (isCoordinateCorrect) {
                    ship.setCoordinates(startCoordinate, isPlacedHorizontal);
                } else {
                    System.out.println("It's not possible to place ship this way. Wrong coordinates!");
                }
            }
        }
    }

    private void displayBoard(PlayerBoard playerBoard) {
        FieldState[][] boardState = playerBoard.getBoardState();
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState.length; j++) {
                FieldState state = boardState[i][j];
                if (state == FieldState.Idle) {
                    System.out.print("I ");
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
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter shot x coordinate");
        int xCoordinate = reader.nextInt();
        System.out.println("Enter shot y coordinate");
        int yCoordinate = reader.nextInt();
        Coordinate shotCoordinate = new Coordinate(xCoordinate, yCoordinate);
        playerBoard.shoot(shotCoordinate);
    }

    private void end() {
        System.out.println("You sank all ships!");
        //displayStatistics();
    }
}
