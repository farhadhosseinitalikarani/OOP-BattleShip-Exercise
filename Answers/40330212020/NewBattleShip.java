package org.example;
import java.util.Scanner;
import java.util.Random;


class Ship {
    private final int size;

    public Ship(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}

// Class representing the Grid
class Grid {
    private static final int GRID_SIZE = 10;
    private static final char WATER = '~';
    private static final char SHIP = 'S';
    private static final char HIT = 'X';
    private static final char MISS = 'O';

    private final char[][] grid;
    private final char[][] trackingGrid;
    private final Random rand = new Random();

    public Grid() {
        grid = new char[GRID_SIZE][GRID_SIZE];
        trackingGrid = new char[GRID_SIZE][GRID_SIZE];
        initializeGrid();
    }

    public void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = WATER;
                trackingGrid[i][j] = WATER;
            }
        }
    }

    public void manualPlacement(Scanner scanner, Ship[] ships) {
        for (Ship ship : ships) {
            boolean placed = false;
            while (!placed) {
                System.out.println("Enter row (A-J) and column (0-9) for ship size " + ship.getSize() + " (e.g. A5): ");
                String input = scanner.next().trim().toUpperCase();
                System.out.println("Horizontal? (y/n): ");
                boolean horizontal = scanner.next().equalsIgnoreCase("y");
                int row = input.charAt(0) - 'A';
                int col = Integer.parseInt(input.substring(1));
                placed = placeShip(row, col, ship, horizontal);
                if (!placed) {
                    System.out.println("Invalid placement. Try again.");
                }
            }
        }
    }

    public void randomPlacement(Ship[] ships) {
        for (Ship ship : ships) {
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt(GRID_SIZE);
                int col = rand.nextInt(GRID_SIZE);
                boolean horizontal = rand.nextBoolean();
                placed = placeShip(row, col, ship, horizontal);
            }
        }
    }

    public boolean placeShip(int row, int col, Ship ship, boolean horizontal) {
        int size = ship.getSize();
        if (canPlaceShip(row, col, size, horizontal)) {
            for (int i = 0; i < size; i++) {
                if (horizontal) {
                    grid[row][col + i] = SHIP;
                } else {
                    grid[row + i][col] = SHIP;
                }
            }
            return true;
        }
        return false;
    }

    private boolean canPlaceShip(int row, int col, int size, boolean horizontal) {
        if (horizontal && col + size > GRID_SIZE) return false;
        if (!horizontal && row + size > GRID_SIZE) return false;

        for (int i = 0; i < size; i++) {
            if (horizontal && grid[row][col + i] != WATER) return false;
            if (!horizontal && grid[row + i][col] != WATER) return false;
        }
        return true;
    }

    public void printTrackingGrid() {
        System.out.print("  ");
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(trackingGrid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isGameOver() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == SHIP) return false;
            }
        }
        return true;
    }

    public String attack(int row, int col) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE)
        {
            return "Invalid Input!";
        }
        if (grid[row][col] == SHIP)
        {
            grid[row][col] = HIT;
            trackingGrid[row][col] = HIT;
            return "Hit!";
        }
        else if (grid[row][col] == WATER)
        {
            grid[row][col] = MISS;
            trackingGrid[row][col] = MISS;
            return "Miss!";
        }
        return "Invalid Input!";
    }
}

// Class representing the AI Player
class AIPlayer {
    private final Random rand = new Random();
    public int[] makeMove()
    {
        return new int[]{rand.nextInt(10), rand.nextInt(10)};
    }
}

public class NewBattleShip {
            public static void main(String[] args)
            {
                Scanner scanner = new Scanner(System.in);
                AIPlayer ai = new AIPlayer();
                Ship[] ships = { new Ship(5), new Ship(4), new Ship(3), new Ship(2) };
                Grid player1Grid = new Grid();
                Grid player2Grid = new Grid();
                Grid aiGrid = new Grid();
                System.out.println("Select game mode:");
                System.out.println("1. Player vs Player");
                System.out.println("2. Player vs AI");
                int choice = scanner.nextInt();

                System.out.println("Do you want to place your ships manually or randomly?");
                System.out.println("1. Manual");
                System.out.println("2. Random");
                int placementChoice = scanner.nextInt();


                    if (choice == 1 && placementChoice == 1) {
                        player1Grid.manualPlacement(scanner, ships);
                        player2Grid.manualPlacement(scanner, ships);
                    } else if (choice == 1 && placementChoice == 2) {
                        player1Grid.randomPlacement(ships);
                        player2Grid.randomPlacement(ships);
                    } else if (choice == 2 && placementChoice == 1) {
                        player1Grid.manualPlacement(scanner, ships);
                        aiGrid.randomPlacement(ships);
                    } else if (choice == 2 && placementChoice == 2) {
                        player1Grid.randomPlacement(ships);
                        aiGrid.randomPlacement(ships);
                    }
                    else{
                        System.out.println("Invalid Input! Restart the game.");
                    }
                if (choice == 1) {
                    boolean player1Turn = true;
                    while(!player1Grid.isGameOver() && !player2Grid.isGameOver())
                    {
                        if (player1Turn)
                        {
                            System.out.println("Player 1'st turn!");
                            player2Grid.printTrackingGrid();
                            System.out.println("Enter target (e.g. A5): ");
                            String input = scanner.next().trim().toUpperCase();

                            if (!(input.length() == 2 && input.charAt(0) >= 'A' && input.charAt(0) < ('A' + 10)
                                && input.substring(1).matches("[0-9]"))){
                                System.out.println("Invalid Input! Missed Your Turn.");
                                return;
                            }
                            int row = input.charAt(0) - 'A';
                            int col = Integer.parseInt(input.substring(1));

                            String Result = player2Grid.attack(row, col);
                            System.out.println(Result);
                        }
                        else
                        {
                            System.out.println("Player 2'nd turn!");
                            player1Grid.printTrackingGrid();
                            System.out.println("Enter target (e.g. A5): ");
                            String input = scanner.next().trim().toUpperCase();

                            if (!(input.length() == 2 && input.charAt(0) >= 'A' && input.charAt(0) < ('A' + 10)
                                    && input.substring(1).matches("[0-9]"))){
                                System.out.println("Invalid Input! Missed Your Turn.");
                                return;
                            }
                            int row = input.charAt(0) - 'A';
                            int col = Integer.parseInt(input.substring(1));

                            String Result = player1Grid.attack(row, col);
                            System.out.println(Result);
                        }
                        player1Turn = !player1Turn;
                    }
                }

                else if (choice == 2) {
                    boolean player1Turn = true;
                    while (!player1Grid.isGameOver() && !aiGrid.isGameOver()){
                        if (player1Turn){
                            aiGrid.printTrackingGrid();
                            System.out.println("Enter target (e.g. A5): ");
                            String input = scanner.next().trim().toUpperCase();

                            if (!(input.length() == 2 && input.charAt(0) >= 'A' && input.charAt(0) < ('A' + 10)
                                    && input.substring(1).matches("[0-9]")))
                            {
                                System.out.println("Invalid Input! Missed Your Turn.");
                                continue;
                            }
                            int row = input.charAt(0) - 'A';
                            int col = Integer.parseInt(input.substring(1));

                            String Result = aiGrid.attack(row, col);
                            System.out.println(Result);
                        }
                        else {
                            int[] aiMove = ai.makeMove();
                            System.out.println("AI attacked " + (char)('A' + aiMove[0]) + aiMove[1]);
                            String Result = player1Grid.attack(aiMove[0], aiMove[1]);
                            System.out.println(Result);
                        }
                        player1Turn = !player1Turn;
                    }
                }

                System.out.println("Game over!");
                if (choice == 1) {
                    if (player1Grid.isGameOver())
                        System.out.println("Player 2 Wins!");
                    else
                        System.out.println("Player 1 Wins!");
                }
                else if (choice == 2) {
                    if (player1Grid.isGameOver())
                        System.out.println("Player 1 Wins!");
                    else
                        System.out.println("AI Wins!");
                }
            }

}
