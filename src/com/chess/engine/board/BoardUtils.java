package com.chess.engine.board;
import java.util.Collection;
import java.util.*;

public class BoardUtils {
//    public static final int NUM_TILES = 64 ;
//    public static final boolean[] First_Column = initColumn{0};
    public static final int START_TILE_INDEX = 0;
    public static final int NUM_TILES_PER_ROW = 8;
    public static final int NUM_TILES = 64;

    public final boolean[] FIRST_COLUMN = initColumn(0);
    public final boolean[] SECOND_COLUMN = initColumn(1);
    public final boolean[] THIRD_COLUMN = initColumn(2);
    public final boolean[] FOURTH_COLUMN = initColumn(3);
    public final boolean[] FIFTH_COLUMN = initColumn(4);
    public final boolean[] SIXTH_COLUMN = initColumn(5);
    public final boolean[] SEVENTH_COLUMN = initColumn(6);
    public final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final boolean[] FIRST_ROW = initRow(0);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] THIRD_ROW = initRow(16);
    public static final boolean[] FOURTH_ROW = initRow(24);
    public static final boolean[] FIFTH_ROW = initRow(32);
    public static final boolean[] SIXTH_ROW = initRow(40);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final boolean[] EIGHTH_ROW = initRow(56);

    private BoardUtils() {
        throw new RuntimeException("OOP's Sorry!!");
    }
    private static boolean[] initColumn(int columnNumber) {
        final boolean[]column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while(columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % NUM_TILES_PER_ROW  != 0);
        return row;
    }



    public static boolean isvalidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < 64;
    }
}
