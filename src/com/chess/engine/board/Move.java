//package com.chess.engine.board;
package com.chess.engine.board;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public abstract class Move {
    final Board board;  // track of board
    final Piece movedPiece; // Track of piece
    final int destinationCoordinate; //where that pieced moved


    public static final Move NULL_MOVE = new NullMove();
    Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Move)) {
            return false;
        }

        final Move otherMove = (Move)other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPiece().equals(otherMove.getMovedPiece());
    }
    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {
        final Board.Builder builder = new Board.Builder();
        for(Piece piece: this.board.currentPlayer().getActivePieces()) {
            if(!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for(Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        // move the moved piece
        builder.setPiece(this.movedPiece.movePiece(this));

        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static final class MajorMove extends Move {

        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static class AttackMove extends Move {

        Piece attackedPiece;

        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        public boolean equals(Object other) {
            if(this == other) {
                return true;
            }
            if(!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
        @Override
        public Board execute() {
             return null;
        }

        public boolean isAttack() {
            return true;
        }

        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move {
        final Piece attackedPiece;
        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
    }

    public static final class PawnAttackMove extends AttackMove {
        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        public Board execute() {

            final Board.Builder builder = new Board.Builder();
            for(Piece piece: this.board.currentPlayer().getActivePieces()) {
                if(!this.movedPiece.eqauls(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
           builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
           return builder.build();

        }
    }

    public static final class CastleMove extends Move {
        public CastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class KingSideCastleMove extends Move {
        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class QueenSideCastleMove extends Move {
        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null,null, -1);
        }

        public Board execute() {
            throw new RuntimeException("cannot execute null move!");
        }
    }

    public static class MoveFactory {
        private static final Move NULL_MOVE = new NullMove();

        private MoveFactory() {
            throw new RuntimeException("Not instantiatable!");
        }

        public static Move getNullMove() {
            return NULL_MOVE;
        }

        public static Move createMove(Board board, int currentCoordinate, int destinationCoordinate) {
            for(Move move: board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
