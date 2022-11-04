package com.chess.inter;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile  destinationTile;
    private Piece humaneMovedPiece;
    private BoardDirection boardDirection;

    private boolean highlightLegalMoves;

    private Color lightTileColor = Color.decode("#FFFACD");
    private Color darkTileColor = Color.decode("#593E1A");
    private static Dimension Outer_Frame_D= new Dimension(600, 600);

   private final static Dimension Board_Panel_D = new Dimension(400, 350);

   private final static Dimension Tile_Panel_D = new Dimension(10, 10);
   private static String defaultPieceImagesPath = "art/pieces/plain/";

    public Table() throws IOException {
        this.gameFrame = new JFrame("chess");
        final JMenuBar tableMenuBar = new JMenuBar();
        this.gameFrame.setLayout(new BorderLayout());
        populateMenuBar(tableMenuBar);
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.highlightLegalMoves = false;
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setSize(Outer_Frame_D);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar populateMenuBar(JMenuBar tableMenuBar) {
//        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());

        tableMenuBar.add(createPreferenceMenu());
                return tableMenuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("file");
        JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open Pgn file");
            }

        });
        fileMenu.add(openPGN);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private JMenu createPreferenceMenu() {
        final JMenu preferencesMenu = new JMenu("Prefernces");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                try {
                    boardPanel.drawBoard(chessBoard);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckbox);

        return preferencesMenu;
    }

    public enum BoardDirection {
        NORMAL {
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {

                Collections.reverse(boardTiles);
                return boardTiles;
            }

            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();

    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        BoardPanel() throws IOException {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            for(int i=0; i< BoardUtils.NUM_TILES; i++) {
                TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(Board_Panel_D);
//            setPreferredSize(BOARD_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.decode("#8B4726"));
            validate();
        }

        public void drawBoard(final Board board) throws IOException {
            removeAll();
            for(TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    private class TilePanel extends JPanel {
       private final int tileId;
       TilePanel(BoardPanel boardPanel, int tileId) throws IOException {
           super(new GridLayout());
           this.tileId = tileId;
           setPreferredSize(Tile_Panel_D);
           assignTileColor();
           assignTilePieceIcon(chessBoard);
           addMouseListener(new MouseListener() {
               @Override
               public void mouseClicked(MouseEvent e) {
                   if(isRightMouseButton(e)) {
                       sourceTile = null;
//                       destinationTile = null;
                       humaneMovedPiece = null;

                       } else if(isLeftMouseButton(e)){
                            if(sourceTile == null) {
                                sourceTile = chessBoard.getTile(tileId);
                                humaneMovedPiece = sourceTile.getPeice();
                                if (humaneMovedPiece == null) {
                                    sourceTile = null;

                                }
                            }
                            else {
//                                destinationTile = chessBoard.getTile(tileId);
//                                Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getPeice().getPiecePosition(), destinationTile.getTileCoordinate());
                                final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getPeice().getPiecePosition(),
                                        tileId);
                                MoveTransition transition =  chessBoard.currentPlayer().makeMove(move);
                                if(transition.getMoveStatus().isDone()) {
                                    chessBoard = transition.getTransitionBoard();
                                }
                                sourceTile = null;
                                destinationTile = null;
                                humaneMovedPiece = null;
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boardPanel.drawBoard(chessBoard);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            });
                      }
                  }

               @Override
               public void mousePressed(MouseEvent e) {

               }

               @Override
               public void mouseReleased(MouseEvent e) {

               }

               @Override
               public void mouseEntered(MouseEvent e) {

               }

               @Override
               public void mouseExited(MouseEvent e) {

               }
           });
           validate();
       }

       public void drawTile(Board board) throws IOException {
           assignTileColor();
           assignTilePieceIcon(board);
//           highlightTileBorder(board);
//           highlightLegals(board);
//           highlightAIMove();
           validate();
           repaint();
       }
        private void assignTilePieceIcon(Board board) throws IOException {
           this.removeAll();
           if(board.getTile(this.tileId).isTileOccupied()) {
               try {
                   final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).getPeice().getPieceAlliance().toString().substring(0, 1) + board.getTile(this.tileId).getPeice().toString() + ".gif"));
                   add(new JLabel(new ImageIcon(image)));
               } catch(IOException e) {
                   e.printStackTrace();
               }


           }
        }

        private void highLightLegals(final Board board) {
           if(highlightLegalMoves) {
               for(Move move: pieceLegalMoves(board)) {
                   if(move.getDestinationCoordinate() == this.tileId) {
                       try {
                           add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                       } catch(Exception e) {
                           e.printStackTrace();
                       }
                   }
               }
           }
        }
        private Collection<Move> pieceLegalMoves(final Board board) {
           if(humaneMovedPiece != null && humaneMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
               return humaneMovedPiece.calculateLegalMoves(board);
           }
           return Collections.emptyList();
        }

        private void assignTileColor() {
           if(BoardUtils.FIRST_ROW[this.tileId] ||
           BoardUtils.THIRD_ROW[this.tileId] ||
                   BoardUtils.FIFTH_ROW[this.tileId]  ||
                   BoardUtils.SEVENTH_ROW[this.tileId]) {
               setBackground(this.tileId%2==0 ? lightTileColor: darkTileColor);
           } else if(BoardUtils.SECOND_ROW[this.tileId] || BoardUtils.FOURTH_ROW[this.tileId] || BoardUtils.SIXTH_ROW[this.tileId] || BoardUtils.EIGHTH_ROW[this.tileId]) {
               setBackground(this.tileId%2 !=0 ? lightTileColor: darkTileColor);
           }
        }
    }

}
