package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {

    private final List<Tile> gameBoard;




    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;

    private final Player currentPlayer;
    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calclulateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calclulateActivePieces(this.gameBoard, Alliance.BLACK);

        final Collection<Move> whiteStandardLegaMoves = calculateLegalMoves(this.whitePieces);
        
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegaMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegaMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);

    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0 ; i <BoardUtils.NUM_TILES; i++) {
            String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }

        }
        return builder.toString();
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }


    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    private static String prettyPrint(Tile tile) {

        return tile.toString();
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
//
        final List<Move> legalMoves = new ArrayList<>();

        for(Piece piece: pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }


        return legalMoves;
//        return pieces.stream().flatMap(piece -> piece.calculateLegalMoves(this).stream())
//                .collect(Collectors.toList());
    }

    private static  Collection<Piece> calclulateActivePieces(List<Tile> gameBoard, Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(Tile tile: gameBoard) {
            if(tile.isTileOccupied()) {
                Piece piece = tile.getPeice();
                if(piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }


    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    public static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i<BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return List.of(tiles);
    }

    public static Board createStandardBoard() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        //white to move
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        return builder.build();
    }

    public Collection<Move> getAllLegalMoves() {
        return Stream.concat(this.whitePlayer.getLegalMoves().stream(),
                this.blackPlayer.getLegalMoves().stream()).collect(Collectors.toList());
    }



    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;

        public Builder() {
              this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}






