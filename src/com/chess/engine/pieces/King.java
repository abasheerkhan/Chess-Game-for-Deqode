package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.Collection;
import java.util.*;

public class King extends Piece{

    private final static int[] candidateMove = {-9, -8, -7, -1, 7, 8, 9};

    public King(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.KING, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int candidateDestinationCoordinate;
        for(int currentCandidate: candidateMove) {
            candidateDestinationCoordinate = this.piecePosition + currentCandidate;
            if(BoardUtils.isvalidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
//                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
//                if(){
//                    continue;
//                }
                if(candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPeice();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));

                    }
                }
            }
        }
        return legalMoves;
    }
    public Piece movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    public String toString() {
        return  PieceType.KING.toString();
    }
}
