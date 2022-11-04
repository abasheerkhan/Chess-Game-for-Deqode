package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import java.util.*;
public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;  // tile coordinate occu[ied by piece
    protected final Alliance pieceAlliance; // piece either be black or white
    private final int cachedHashCode;
    protected final boolean isFirstMove;
    // going to create enum for alliance
    Piece(final PieceType pieceType ,final int piecePosition, final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.isFirstMove = false;
        this.cachedHashCode = camputeHashCode();
    }

    private int camputeHashCode() {
        return this.cachedHashCode;
    }

    public boolean eqauls(final Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Piece)) {
            return false;
        }
        Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() && pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    public int hashCode() {
        int result  = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1: 0);
        return result;

    }

    public int getPiecePosition() {
        return this.piecePosition;

    }
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }
//    calculating legal moves
     public abstract Collection<Move> calculateLegalMoves(final Board board); // collection is for duplicate purpose

//    public PieceType getPieceType() {
//        return this.pieceType;
//    }
      public abstract Piece movePiece(Move move);
    public enum PieceType {
         PAWN("P") {
             public boolean isKing() {
                 return false;
             }
         },
         KNIGHT("N") {
             public boolean isKing() {
                 return false;
             }
         },
         BISHOP("B") {
             public boolean isKing() {
                 return false;
             }
         },
         ROOK("R") {
             public boolean isKing() {
                 return false;
             }
         },
         QUEEN("Q") {
             public boolean isKing() {
                 return false;
             }
         },
         KING("K") {
             public boolean isKing() {
                 return true;
             }
         };
         private String pieceName;
         PieceType(String pieceName) {
             this.pieceName = pieceName;
         }

         public String toString() {
             return this.pieceName;
         }
         public abstract boolean isKing() ;
     }
}


