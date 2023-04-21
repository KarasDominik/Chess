package pl.karasdominik.chessgame;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Chessboard extends GridPane {

    protected static final int SIZE = 8;
    protected Piece[][] piecesOnBoard = new Piece[8][8];
    protected List<Move> moves = new ArrayList<>();
    protected List<Circle> circles = new ArrayList<>();
    protected List<Piece> whitePiecesLeft = new ArrayList<>();
    protected List<Piece> blackPiecesLeft = new ArrayList<>();

    protected List<Move> possibleMoves = new ArrayList<>();
    protected List<String> squaresAttackedByWhite = new ArrayList<>();
    protected List<String> squaresAttackedByBlack = new ArrayList<>();
    protected King whiteKing;
    protected King blackKing;

    public Chessboard() {
        int rowCounter = 8;
        char colCounter = 'a';
        Font font = Font.font("Arial", FontWeight.BOLD, 16);

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                String rowString = "";
                String colString = "";
                if (column == 0) {
                    rowString += rowCounter;
                    rowCounter -= 1;
                }
                if (row == SIZE - 1) {
                    colString += colCounter;
                    colCounter += 1;
                }
                Rectangle square = new Rectangle();
                Color color = (row + column) % 2 == 0 ? Color.rgb(208, 193, 175) : Color.rgb(89, 44, 24);
                square.setFill(color);
                square.widthProperty().bind(widthProperty().divide(SIZE));
                square.heightProperty().bind(heightProperty().divide(SIZE));
                Text rowText = new Text(rowString);
                Text colText = new Text(colString);
                rowText.setFont(font);
                colText.setFont(font);
                StackPane stackPane = new StackPane(square, rowText, colText);
                StackPane.setMargin(rowText, new Insets(0, 75, 50, 0));
                StackPane.setMargin(colText, new Insets(50, 0, 0, 85));
                add(stackPane, column, row);
            }
        }
        addStartingPieces();
    }

    private void updateChessboardGraphically(boolean wasCastling){
        Move move = moves.get(moves.size() - 1);
        Piece piece = move.piece;
        Piece capturedPiece = move.capturedPiece;

        int newRow = Helper.convertSquareToInts(move.targetSquare)[0];
        int newCol = Helper.convertSquareToInts(move.targetSquare)[1];

        if(move.wasPromoting){
            getChildren().remove(move.pawnBeforePromotion);
            GridPane.setHalignment(piece, HPos.CENTER);
            GridPane.setValignment(piece, VPos.CENTER);
        }
        ObservableList<Node> nodes = getChildren();
        for (Node node : nodes) {
            if (node instanceof Piece && node.equals(capturedPiece)) {
                getChildren().remove(node);
                break;
            }
        }

        getChildren().remove(move.piece);

        if(wasCastling){
            int initialColumn = Helper.convertSquareToInts(move.initialSquare)[1];
            int targetColumn = Helper.convertSquareToInts(move.targetSquare)[1];

            int newRookColumn;
            if(targetColumn > initialColumn) newRookColumn = targetColumn - 1;
            else newRookColumn = targetColumn + 1;
            Piece rookToMove = piecesOnBoard[newRow][newRookColumn];
            getChildren().remove(rookToMove);
            add(rookToMove, newRookColumn, newRow);
        }

        add(piece, newCol, newRow);
        removeCircles();
    }

    public void makeMove(Move move, boolean isFinal){
        Piece piece = move.piece;
        boolean wasCastling = false;

        int initialSquareRow = Helper.convertSquareToInts(move.initialSquare)[0];
        int initialSquareColumn = Helper.convertSquareToInts(move.initialSquare)[1];

        int targetSquareRow = Helper.convertSquareToInts(move.targetSquare)[0];
        int targetSquareColumn = Helper.convertSquareToInts(move.targetSquare)[1];

        // Handle pawn promotion
        if(piece instanceof Pawn && (targetSquareRow == 0 || targetSquareRow == 7)){
            ((Pawn) piece).promotePawn(move, this, isFinal);
            return;
        }

        // Handle castling
        if(piece instanceof King && Math.abs(initialSquareColumn - targetSquareColumn) == 2){
            wasCastling = true;
            int oldRookColumn;
            int newRookColumn;
            if(targetSquareColumn > initialSquareColumn){
                // Castling kingside
                oldRookColumn = targetSquareColumn + 1;
                newRookColumn = targetSquareColumn - 1;
            } else {
                // Castling queenside
                oldRookColumn = targetSquareColumn - 2;
                newRookColumn = targetSquareColumn + 1;
            }
            Piece rookToMove = piecesOnBoard[targetSquareRow][oldRookColumn];
            piecesOnBoard[targetSquareRow][oldRookColumn] = null;
            piecesOnBoard[targetSquareRow][newRookColumn] = rookToMove;
            rookToMove.piecePosition = Helper.convertSquareToString(targetSquareRow, newRookColumn);
        }

        // Handle en passant
        else if(piece instanceof Pawn && piecesOnBoard[targetSquareRow][targetSquareColumn] == null && initialSquareColumn != targetSquareColumn){
            move.capturedPiece = piecesOnBoard[initialSquareRow][targetSquareColumn];
            piecesOnBoard[initialSquareRow][targetSquareColumn] = null;
        } else {
            move.capturedPiece = piecesOnBoard[targetSquareRow][targetSquareColumn];
        }

        piecesOnBoard[targetSquareRow][targetSquareColumn] = piece;
        piecesOnBoard[initialSquareRow][initialSquareColumn] = null;
        piece.piecePosition = Helper.convertSquareToString(targetSquareRow, targetSquareColumn);

        if (piece.isWhite) {
            updateBlackPiecesLeft();
            updateSquaresAttackedByBlack();
        } else {
            updateWhitePiecesLeft();
            updateSquaresAttackedByWhite();
        }

        moves.add(move);

        if(isFinal){
            piece.isFirstMove = false;
            updateChessboardGraphically(wasCastling);
            Engine engine = chessApplication.getEngine();
            possibleMoves = moveGenerator();
            if(isGameOver()) return;
            if(engine.isMyTurn()) engine.makeMove();
        }
    }

    public void unmakeMove(){
        Move moveToUnmake = moves.remove(moves.size() - 1);

        Piece capturedPiece = moveToUnmake.capturedPiece;
        Piece piece = moveToUnmake.piece;

        int initialSquareRow = Helper.convertSquareToInts(moveToUnmake.initialSquare)[0];
        int initialSquareColumn = Helper.convertSquareToInts(moveToUnmake.initialSquare)[1];

        int targetSquareRow = Helper.convertSquareToInts(moveToUnmake.targetSquare)[0];
        int targetSquareColumn = Helper.convertSquareToInts(moveToUnmake.targetSquare)[1];

        // Handle pawn promotion
        if(moveToUnmake.wasPromoting){
            piece = moveToUnmake.pawnBeforePromotion;
        }

        // Handle castling
        if(piece instanceof King && Math.abs(initialSquareColumn - targetSquareColumn) == 2){
            int oldRookColumn;
            int newRookColumn;
            if(targetSquareColumn > initialSquareColumn){
                // Castling kingside
                newRookColumn = targetSquareColumn + 1;
                oldRookColumn = targetSquareColumn - 1;
            } else {
                // Castling queenside
                newRookColumn = targetSquareColumn - 2;
                oldRookColumn = targetSquareColumn + 1;
            }
            Piece rookToMove = piecesOnBoard[targetSquareRow][oldRookColumn];
            piecesOnBoard[targetSquareRow][oldRookColumn] = null;
            piecesOnBoard[targetSquareRow][newRookColumn] = rookToMove;
            rookToMove.piecePosition = Helper.convertSquareToString(targetSquareRow, newRookColumn);
        }

        // Handle en passant
        if(piece instanceof Pawn && capturedPiece != null && !moveToUnmake.targetSquare.equals(capturedPiece.piecePosition)){
            piecesOnBoard[initialSquareRow][targetSquareColumn] = capturedPiece;
            piecesOnBoard[targetSquareRow][targetSquareColumn] = null;
        } else {
            piecesOnBoard[targetSquareRow][targetSquareColumn] = capturedPiece;
        }

        piecesOnBoard[initialSquareRow][initialSquareColumn] = piece;
        piece.piecePosition = Helper.convertSquareToString(initialSquareRow, initialSquareColumn);

        if (piece.isWhite) {
            updateBlackPiecesLeft();
            updateSquaresAttackedByBlack();
        } else {
            updateWhitePiecesLeft();
            updateSquaresAttackedByWhite();
        }
    }

    private void addStartingPieces() {
        boolean isWhite = false;
        for (int row = 0; row < SIZE; row++) {
            if (!(row == 0 || row == 1 || row == 6 || row == 7)) {
                continue;
            }
            if (row == 6) {
                isWhite = true;
            }
            for (int column = 0; column < SIZE; column++) {
                Piece piece;
                if (row == 0 || row == 7) {
                    switch (column) {
                        case 0, 7 -> piece = new Rook(isWhite, "rook", row, column);
                        case 1, 6 -> piece = new Knight(isWhite, "knight", row, column);
                        case 2, 5 -> piece = new Bishop(isWhite, "bishop", row, column);
                        case 3 -> piece = new Queen(isWhite, "queen", row, column);
                        default -> {
                            piece = new King(isWhite, "king", row, column);
                            if (isWhite) whiteKing = (King) piece;
                            else blackKing = (King) piece;
                        }
                    }
                } else {
                    piece = new Pawn(isWhite, "pawn", row, column);
                }
                if(piece.isWhite) piece.getPossibleMoves(this);
                addPieceToTheBoard(piece, row, column);
            }
        }
    }

    public void addPieceToTheBoard(Piece piece, int row, int column){
        piecesOnBoard[row][column] = piece;
        add(piece, column, row);
        GridPane.setHalignment(piece, HPos.CENTER);
        GridPane.setValignment(piece, VPos.CENTER);
    }

    public void updateSquaresAttackedByWhite() {
        squaresAttackedByWhite.clear();
        for (Piece piece : whitePiecesLeft) {
            piece.getPossibleMoves(this);
            if (piece instanceof Pawn) {
                squaresAttackedByWhite.addAll(((Pawn) piece).squaresAttacked);
            } else if (piece instanceof King) {
                squaresAttackedByWhite.addAll(((King) piece).squaresAttacked);
            } else {
                squaresAttackedByWhite.addAll(piece.getAvailableMoves());
            }
        }
    }

    public void updateSquaresAttackedByBlack() {
        squaresAttackedByBlack.clear();
        for (Piece piece : blackPiecesLeft) {
            piece.getPossibleMoves(this);
            if (piece instanceof Pawn) {
                squaresAttackedByBlack.addAll(((Pawn) piece).squaresAttacked);
            } else if (piece instanceof King) {
                squaresAttackedByBlack.addAll(((King) piece).squaresAttacked);
            } else {
                squaresAttackedByBlack.addAll(piece.getAvailableMoves());
            }
        }
    }

    public void updateBlackPiecesLeft() {
        blackPiecesLeft.clear();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Piece piece = piecesOnBoard[row][column];
                if (piece != null && !piece.isWhite) {
                    blackPiecesLeft.add(piece);
                }
            }
        }
    }

    public void updateWhitePiecesLeft() {
        whitePiecesLeft.clear();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Piece piece = piecesOnBoard[row][column];
                if (piece != null && piece.isWhite) {
                    whitePiecesLeft.add(piece);
                }
            }
        }
    }

    public void removeCircles() {
        for (Circle circle : circles) {
            getChildren().remove(circle);
        }
        circles.clear();
    }

    private boolean isGameOver(){
        if(possibleMoves.size() == 0){
            if(blackKing.isInCheck(this)){
                System.out.println("Checkmate, white wins");
            }
            else if(whiteKing.isInCheck(this)){
                System.out.println("Checkmate, black wins");
            } else {
                System.out.println("Stalemate");
            }
            return true;
        }
        if(blackPiecesLeft.size() == 1 && whitePiecesLeft.size() == 1) {
            System.out.println("Draw");
            return true;
        }
        return false;
    }

    public int countMaterial(boolean isWhite){
        int material = 0;
        List<Piece> piecesToCount = isWhite ? whitePiecesLeft : blackPiecesLeft;
        for (Piece piece : piecesToCount){
            material += piece.value;
        }
        return material;
    }

    private double centerControl(boolean isWhite){
        double centerControl = 0;

        for(int row = 3; row <= 4; row++){
            for(int col = 2; col <= 5; col++){
                Piece piece = piecesOnBoard[row][col];
                if(piece != null && piece.isWhite == isWhite) {
                    centerControl += 0.5;
                }
            }
        }
        return centerControl;
    }

    public double evaluate(){
        double whiteEvaluation = countMaterial(true) + centerControl(true);
        double blackEvaluation = countMaterial(false) + centerControl(false);

        return whiteEvaluation - blackEvaluation;
    }

    public double Search(int depth, boolean isMaximizing, Double alpha, Double beta){
        if (depth == 0) return evaluate();

        List<Move> availableMoves = moveGenerator();
        King kingToCheck = whiteToMove() ? whiteKing : blackKing;

        if(availableMoves.size() == 0){
            if (kingToCheck.isInCheck(this)){
                return Integer.MIN_VALUE;
            }
            return 0;
        }

        double bestValue;
        if (isMaximizing){
            bestValue = Double.MIN_VALUE;
            for(Move move : availableMoves){
                makeMove(move, false);
                double value = Search(depth - 1, false, alpha, beta);
                unmakeMove();
                bestValue = Math.max(bestValue, value);
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    break; // beta cut-off
                }
            }
        } else {
            bestValue = Double.MAX_VALUE;
            for(Move move : availableMoves){
                makeMove(move, false);
                double value = Search(depth - 1, true, alpha, beta);
                unmakeMove();
                bestValue = Math.min(bestValue, value);
                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    break; // alpha cut-off
                }
            }
        }
        return bestValue;
    }

    public boolean whiteToMove(){
        return moves.size() % 2 == 0;
    }

    public List<Move> moveGenerator(){
        List<Move> possibleMoves = new ArrayList<>();
        for (int row = 0; row < SIZE; row++){
            for (int col = 0; col < SIZE; col++){
                Piece piece = piecesOnBoard[row][col];
                if(piece != null){
                    if(whiteToMove() && piece.isWhite || !whiteToMove() && !piece.isWhite){
                        piece.getPossibleMoves(this);
                        King kingToProtect = piece.isWhite ? whiteKing : blackKing;
                        List<String> movesToRemove = new ArrayList<>();
                        for (String availableMove : piece.availableMoves){
                            Move move = new Move(piece, piece.piecePosition, availableMove);
                            makeMove(move, false);
                            if(kingToProtect.isInCheck(this)) {
                                movesToRemove.add(availableMove);
                            } else {
                                possibleMoves.add(move);
                            }
                            unmakeMove();
                        }
                        piece.availableMoves.removeAll(movesToRemove);
                    }
                }
            }
        }
        return possibleMoves;
    }

//    private void printChessboard(){
//        for (int row = 0; row < SIZE; row++){
//            for (int col = 0; col < SIZE; col++) {
//                System.out.printf("%10s", piecesOnBoard[row][col]);
//                if(col==7) System.out.println();
//            }
//        }
//        System.out.println("=======================================================================");
//    }
}
