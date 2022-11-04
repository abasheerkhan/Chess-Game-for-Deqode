package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece{

    private final static int[] candidateMove = {8, 16, 7, 9 };

    public Pawn( Alliance pieceAlliance, int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(int currentCandidate: candidateMove) {
            int candidateDestinationCoordinate = this.piecePosition +  (this.getPieceAlliance().getDirection() * currentCandidate);
            if(!BoardUtils.isvalidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if(currentCandidate == 8 && board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidate == 16 && this.isFirstMove() && this.getPieceAlliance().isBlack() || this.getPieceAlliance().isWhite()) {
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(candidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                     legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));

                }

            } else if (currentCandidate == 7) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPeice();
                    if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()) {
                        //add attack move
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            } else if(currentCandidate == 9) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPeice();
                    if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()) {
                        //add attack move
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            }

        }
        return legalMoves;
    }

    public Piece movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    public String toString() {
        return  PieceType.PAWN.toString();
    }
}
