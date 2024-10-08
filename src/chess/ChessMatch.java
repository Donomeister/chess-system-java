package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();


    public ChessMatch(){
        board = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        //booleanos começam como falso, porém quero enfatizar
        checkMate = false;
        check =false;
        initialSetup();
    }

    public boolean isCheckMate(){
        return checkMate;
    }

    public Color getcurrentPlayer() {
        return currentPlayer;
    }

    public int getTurn() {
        return turn;
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

    public boolean getCheck() {
        return check;
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

        if(testCheck(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        }
        else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    private void validSourcePosition(Position source){
        if(!board.thereIsAPiece(source)){
            throw new ChessException("Invalid source position!");
        }
        if(currentPlayer != ((ChessPiece) board.piece(source)).getColor()){
            throw new ChessException("This piece chosen is not yours! ");
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

    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Piece makeMove(Position source, Position target){
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);

        board.placePiece(p,target);
        if(capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        //small rook
        if(p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT1 = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT1 = new Position(source.getRow(), source.getColumn() + 1);

            ChessPiece rook = (ChessPiece) board.removePiece(sourceT1);
            board.placePiece(rook, targetT1);
            rook.increaseMoveCount();
        }
        //big rook
        if(p instanceof King && target.getColumn() == source.getColumn() - 2){
            Position sourceT1 = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT1 = new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece)board.removePiece(sourceT1);
            board.placePiece(rook, targetT1);
            rook.increaseMoveCount();

        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();

        board.placePiece(p, source);

        if(capturedPiece != null) {
            board.placePiece(capturedPiece, target);
        }

        //small rook
        if(p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT1 = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT1 = new Position(source.getRow(), source.getColumn() + 1);

            ChessPiece rook = (ChessPiece)board.removePiece(targetT1);
            board.placePiece(rook, sourceT1);
            rook.decreaseMoveCount();

        }
        //big rook
        if(p instanceof King && target.getColumn() == source.getColumn() - 2){
            Position sourceT1 = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT1 = new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece)board.removePiece(targetT1);
            board.placePiece(rook, sourceT1);
            rook.decreaseMoveCount();
        }

    }

    private ChessPiece king(Color color){
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).toList();

        for(Piece p : list){
            if(p instanceof King){
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board!");
    }

    public boolean testCheck(Color color){
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).toList();

        for(Piece p : opponentPieces){
            boolean [][] mat = p.possibleMoves();
            if(mat[kingPosition.getRow()][kingPosition.getColumn()]){
               return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i=0; i<board.getRows(); i++) {
                for (int j=0; j<board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup(){

        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
    }
}
