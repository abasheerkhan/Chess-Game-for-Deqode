package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class Rook extends Piece {
    private final static int[] candidateMove = {-8, -1, 1, 8};
//    private Board board;


    public Rook( Alliance pieceAlliance, int piecePosition) {
        super(PieceType.ROOK, piecePosition, pieceAlliance);
    }

//    @Override
public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidate : candidateMove) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isvalidTileCoordinate(candidateDestinationCoordinate)) {
                candidateDestinationCoordinate += candidate;

                if (BoardUtils.isvalidTileCoordinate(candidateDestinationCoordinate)) {
//                    final Piece pieceAtDestination = board.getPeice();
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                      final Piece pieceAtDestination = candidateDestinationTile.getPeice();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));

                        }
                        break;
                    }

                }
            }
        }


        return Collections.unmodifiableList(legalMoves);
    }
    public Piece movePiece(Move move) {
        return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }


    public String toString() {
        return  PieceType.ROOK.toString();
    }
}
