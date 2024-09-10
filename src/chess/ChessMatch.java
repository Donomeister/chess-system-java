package chess;

import boardgame.Board;
import boardgame.BoardGameException;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

    private Board board;

    public ChessMatch(){
        board = new Board(8,8);
        initialSetup();
    }

    public ChessPiece[][] getPieces(){
        ChessPiece[][]  mat = new ChessPiece[board.getRows()][board.getColumns()];
        for(int i = 0; i<board.getColumns(); i++){
            for(int j = 0; j< board.getRows(); j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition source){
        Position sourcePosition = source.toPosition();
        validSourcePosition(sourcePosition);
        return board.piece(sourcePosition).possibleMoves();
    }

    public Piece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){

        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validSourcePosition(source);

        validTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        return (ChessPiece) capturedPiece;
    }

    private void validSourcePosition(Position source){
        if(!board.thereIsAPiece(source)){
            throw new ChessException("Invalid source position!");
        }
        if(!board.piece(source).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece!");
        }
    }

    private void validTargetPosition (Position source, Position target){
        if (!board.piece(source).possibleMove(target)){
            throw new ChessException("Invalid target!");
        }
    }

    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p,target);
        return capturedPiece;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup(){
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 4, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e',2 , new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new King(board, Color.WHITE));
    }
}
