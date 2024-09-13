package chess.pieces;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    private ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch= chessMatch;
    }

    @Override
    public String toString(){
        return "K";
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    private boolean testRook(Position position){
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p.getMoveCount() == 0 && p.getColor() == getColor() && p instanceof Rook;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        // above
        p.setValues(position.getRow() - 1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // below
        p.setValues(position.getRow() + 1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // left
        p.setValues(position.getRow(), position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // right
        p.setValues(position.getRow(), position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // nw
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // ne
        p.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // sw
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // se
        p.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        if(moveCount == 0 && !chessMatch.getCheck()){
            Position T1 = new Position(position.getRow(), position.getColumn() + 3);
            // king side rook
            if(testRook(T1)){
                Position t2 = new Position(position.getRow(), position.getColumn() + 1);
                Position t3 = new Position(position.getRow(), position.getColumn() + 2);
                if(getBoard().piece(t2) == null && getBoard().piece(t3) == null){
                    mat[position.getRow()][position.getColumn() + 2] = true;
                }
            }
            // queen side rook
            Position T2 = new Position(position.getRow(), position.getColumn() - 4);
            if(testRook(T2)){
                Position t1 = new Position(position.getRow(), position.getColumn() - 1);
                Position t2 = new Position(position.getRow(), position.getColumn() - 2);
                Position t3 = new Position(position.getRow(), position.getColumn() - 3);
                if(getBoard().piece(t1) == null && getBoard().piece(t2) == null && getBoard().piece(t3) == null){
                    mat[position.getRow()][position.getColumn() - 2] = true;
                }
            }

        }



        return mat;
    }
}
