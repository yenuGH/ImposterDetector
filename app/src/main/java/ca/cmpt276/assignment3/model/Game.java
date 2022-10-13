package ca.cmpt276.assignment3.model;

import java.util.Random;

/*
    Represents a game. Stores information about where mines are, whether cells have been scanned and
    helps to find information about games.
*/
public class Game {

    /*
     * DONE - helper functions to get info on cells (is mine, is etc.)
     * DONE - helper functions to get adjacent mines in mine column/row ex -
     * getMinesadjacent(row,column)
     * DONE - number of mines found
     * DONE - number of mines total
     * DONE -number of scans used - getter/setter
     * DONE - helper function to place mines
     * serealiziation to save game progress - later
     */

    enum CellType {
        UNSCANNED,
        UNSCANNED_MINE,
        SCANNED,
        REVEALED_MINE,
        SCANNED_MINE
    }

    CellType[][] cells;
    int totalMines;
    int foundMines;
    int totalScans;

    int rows, columns;

    public Game() {
        gameOptions = GameOptions.getInstance();
        totalMines = gameOptions.getMineCountValue();
        rows = gameOptions.getRowValue();
        columns = gameOptions.getColumnValue();
    }

    public int getRowValue() {
        return rows;
    }

    public int getColumnValue() {
        return columns;
    }

    public int getTotalMines() {
        return totalMines;
    }

    public int getScans() {
        return totalScans;
    }

    /**
     * Scans a cell. If no mine is found it performs a scan. If a mine is found it
     * reveals it. If a mine has already been revealed it scans.
     * 
     * @param row
     * @param column
     */
    public void interactCell(int row, int column) {
        // Get the cell
        CellType cell = cells[row][column];

        // Update the cell if necessary
        switch (cell) {
            case UNSCANNED: {
                cell = CellType.SCANNED;
                totalScans++;
                break;
            }

            case UNSCANNED_MINE: {
                cell = CellType.REVEALED_MINE;
                foundMines++;
                break;
            }

            case REVEALED_MINE: {
                totalScans++;
                cell = CellType.SCANNED_MINE;
                break;
            }

        }

        // Update the cell
        cells[row][column] = cell;
    }

    public boolean isMine(int row, int column) {
        return (cells[row][column] == CellType.UNSCANNED_MINE || cells[row][column] == CellType.SCANNED_MINE);
    }

    public boolean isScanned(int row, int column) {
        return (cells[row][column] == CellType.SCANNED_MINE || cells[row][column] == CellType.SCANNED);
    }

    public int numberOfMinesTotal() {
        return totalMines;
    }

    public boolean gameWon() {
        return (totalMines == foundMines);
    }

    private void placeMines() {
        int rowValue = gameOptions.getRowValue();
        int columnValue = gameOptions.getColumnValue();

        // Clear cells with default values
        for (int row = 0; row < rowValue; row++) {
            for (int column = 0; column < columnValue; column++) {
                cells[row][column] = CellType.UNSCANNED;
            }
        }

        // Randomly fill cells with mines
        Random random = new Random();
        int minesToPlace = totalMines;

        while (minesToPlace > 0) {
            int row = random.nextInt(rowValue - 1);
            int column = random.nextInt(columnValue - 1);

            // If the cell is empty place the mine
            if ((cells[row][column]) == CellType.UNSCANNED) {
                cells[row][column] = CellType.UNSCANNED_MINE;
                minesToPlace--;
            }
        }
    }

    public int getAdjacentMines(int row, int column) {
        int rowValue = gameOptions.getRowValue();
        int columnValue = gameOptions.getColumnValue();
        int adjacentMineCount = 0;

        // check the row
        for (int i = 0; i < columnValue; i++) {
            if (cells[row][i] == CellType.UNSCANNED_MINE) {
                adjacentMineCount++;
            }
        }
        // check the column
        for (int i = 0; i < rowValue; i++) {
            if (cells[i][column] == CellType.UNSCANNED_MINE) {
                adjacentMineCount++;
            }
        }

        return adjacentMineCount;
    }

    public int numberOfMinesFound() {
        int rowValue = gameOptions.getRowValue();
        int columnValue = gameOptions.getColumnValue();
        int mineCount = gameOptions.getMineCountValue();

        int mineFoundCount = 0;

        // Iterate through each column and row and check if cells are revealed mines
        // If so, increment the mineFoundCount
        for (int row = 0; row < rowValue; row++) {
            for (int column = 0; column < columnValue; column++) {
                CellType cell = cells[row][column];
                if (cell == CellType.REVEALED_MINE) {
                    mineFoundCount++;
                }
            }
        }

        return mineFoundCount;
    }

    public int gNumberOfScansUsed() {
        return totalScans;
    }

}
