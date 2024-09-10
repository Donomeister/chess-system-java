package chess.pieces;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString(){
        return "K";
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0,0);

        // above
        p.setValues(getPosition().getRow() - 1, getPosition().getColumn());

        if(canMove(p) && getBoard().positionExists(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        // right
        p.setValues(getPosition().getRow() , getPosition().getColumn() + 1);

        if(canMove(p) && getBoard().positionExists(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        // left
        p.setValues(getPosition().getRow(), getPosition().getColumn() - 1);

        if(canMove(p) && getBoard().positionExists(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        // below
        p.setValues(getPosition().getRow() + 1, getPosition().getColumn());

        if(canMove(p) && getBoard().positionExists(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        //ne
        p.setValues(getPosition().getRow() - 1, getPosition().getColumn() + 1);

        if(canMove(p) && getBoard().positionExists(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        //se
        p.setValues(getPosition().getRow() + 1, getPosition().getColumn() + 1);

        if(canMove(p) && getBoard().positionExists(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        //no
        p.setValues(getPosition().getRow() - 1, getPosition().getColumn() - 1);

        if(canMove(p) && getBoard().positionExists(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
        //so
        p.setValues(getPosition().getRow() + 1, getPosition().getColumn() - 1);

        if(canMove(p) && getBoard().positionExists(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }
}
