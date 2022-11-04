package com.chess.engine.board;
import com.chess.engine.pieces.Piece;

import java.util.*;

public abstract class Tile {
    protected final int tileCordinate;    // To Achieve Immutablity.
    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap= new HashMap<>();
        for(int i = 0; i<64; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTileMap);
    }

    public static Tile createTile(final int tileCordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCordinate, piece): EMPTY_TILES.get(tileCordinate);
    }
    private Tile(int tileCordinate) { // For Immutabality
        this.tileCordinate = tileCordinate;
    }

    public abstract boolean isTileOccupied();
    public abstract Piece getPeice();

    public static final class EmptyTile extends Tile {
        private EmptyTile(final int coordinate) {
            super(coordinate);
        }
        public String toString() {
            return "-";
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPeice() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile; // For Immutabality

        private OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        public String toString() {
            return getPeice().getPieceAlliance().isBlack() ? getPeice().toString().toLowerCase(): getPeice().toString();
        }

        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPeice() {
            return this.pieceOnTile;
        }
    }
}
