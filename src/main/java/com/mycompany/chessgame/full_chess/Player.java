package com.mycompany.chessgame.full_chess;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Player {

    // The representative value of dark color be assigned for some player
    public static int DARK_COLOR = 0;
    // The representative value of light color be assigned for some player
    public static int LIGHT_COLOR = 1;
    // The representative value of upper side be assigned for some player
    public static int UPPER_SIDE = 0;
    // The representative value of lower side be assigned for some player
    public static int LOWER_SIDE = 1;

    private String Name;
    private ArrayList<Piece> Pieces;
    private boolean UnderChecking; // Check status for the player.
    private int Color;
    private int Side;

    public Player(String Name, int Side, int Color) {
        Pieces = new ArrayList<Piece>();
        this.Color = Color;
        this.Name = Name;
        this.Side = Side;
        UnderChecking = false;
        setupPieces();

    }

    public String getColor() {
        if (Color == Player.LIGHT_COLOR) {
            return "LIGHT";
        } else if (Color == Player.DARK_COLOR) {
            return "DARK";
        }
        return "";
    }

    public String getName() {
        return Name;
    }

    public boolean getCheckStauts() {
        return UnderChecking;
    }

    public void setCheckStatus(boolean status) {
        UnderChecking = status;
    }

    protected void setupPieces() {
        /*
	Method to initiate player's pieces then add them to "Pieces" ArrayList 
	Each piece's position is defined according to its initial state regarding to chess
	rules Column for each piece is fixed on both sides Only the row will be
	determined depending on player's side
         */
        int FirstRow;
        int SecondRow;
        int Twins;
        if (Side == UPPER_SIDE) {
            FirstRow = 1;
            SecondRow = 2;
        } else {
            FirstRow = 8;
            SecondRow = 7;
        }
        /*
	Adding Pawns
         */
        Pawn NewPawn;
        for (int i = 0; i < 8; i++) {
            NewPawn = new Pawn(new Position(i + 1, SecondRow));
            Pieces.add(NewPawn);
        }

        /*
	Adding Rooks
         */
        Rook NewRook;
        for (int i = 0; i < 2; i++) {
            Twins = i == 0 ? 1 : 8;
            NewRook = new Rook(new Position(Twins, FirstRow));
            Pieces.add(NewRook);
        }
        /*
	Adding Bishops
         */
        Bishop NewBishop;
        for (int i = 0; i < 2; i++) {
            Twins = i == 0 ? 3 : 6;
            NewBishop = new Bishop(new Position(Twins, FirstRow));
            Pieces.add(NewBishop);
        }

        /*
	Adding Knights
         */
        Knight NewKnight;
        for (int i = 0; i < 2; i++) {
            Twins = i == 0 ? 2 : 7;
            NewKnight = new Knight(new Position(Twins, FirstRow));
            Pieces.add(NewKnight);
        }
        /*
	Adding the Queen
         */
        Queen TheQueen = new Queen(new Position(4, FirstRow));
        Pieces.add(TheQueen);

        /*
	Adding the King
         */
        King TheKing = new King(new Position(5, FirstRow));
        Pieces.add(TheKing);

    }

    public void setPieceState(Position PiecePosition, boolean state) {
        Piece piece = fetchPieceByPosition(PiecePosition);
        piece.setAvailability(state);
    }

    public boolean makeMove(int PieceNo, Position next, boolean CaptureRequest) {
        /*
        Method to change the position of a given piece index After checking the
        movement legality according to the type of piece Legality checking is done
        through confirmation of piece's movements rules. Piece.confirmMoveLegality
        method is responsible for this role
         */
        Piece DraggedPiece = fetchPieceByIndex(PieceNo);
        if (DraggedPiece.confirmMoveLegality(next, CaptureRequest)) {
            DraggedPiece.setPosition(next);
            return true;
        } else {
            return false;
        }

    }

    public void undoMovement(int Index) {
        fetchPieceByIndex(Index).backtrack();
    }

    public boolean isPositionOccupied(Position position) {
        /*
        Method to check if the given position is occupied by avaliable piece or not
        returning true if occupied, false if empty. 
         */
        Piece piece = fetchPieceByPosition(position);
        if (piece != null) {
            if (piece.getAvailability()) {
                return true;
            }
        }
        return false;

    }

    public boolean isPieceKnight(int PieceIndex) {
        /*
        Method to check if the type of matched piece for a given piece index knight or not.
        returning true for knight, false for any other type.
         */
        String DraggedPieceName = fetchPieceName(PieceIndex).toLowerCase();
        if (DraggedPieceName.equals("knight")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean requestCastling(int column) {
        /*
        Method to check the ability of castling for the player using the next position.
        column should be either 3 or 7.
        returning true for the ability of castling, false for disability.
         */
        Piece King = fetchPieceByIndex(15);
        if (King.getMovementsCount() > 1) {
            return false;
        }
        Piece Rook = null;
        if (column == 7) {
            Rook = fetchPieceByIndex(9);
        } else if (column == 3) {
            Rook = fetchPieceByIndex(8);
        } else {
            return false;
        }

        if (Rook.getMovementsCount() == 1) {
            return true;
        } else {
            return false;
        }

    }

    public boolean commitCastling(Position RookPosition) {
        /*
        Method to commit castling in terms of changing the positions according to
        castling rules
         */
        int RookPieceIndex = fetchPieceIndex(RookPosition);
        if (RookPieceIndex == -1) {
            return false;
        }

        Piece King = fetchPieceByIndex(15);
        Piece Rook = fetchPieceByIndex(RookPieceIndex);
        if (Rook.getMovementsCount() == 1 && King.getMovementsCount() == 1) {
            System.out.println("i'm in comitcastling");
            if (RookPosition.getColumn() == 8) {
                Rook.setPosition(new Position(6, RookPosition.getRow()));
                King.setPosition(new Position(7, RookPosition.getRow()));
            } else if (RookPosition.getColumn() == 1) {
                Rook.setPosition(new Position(4, RookPosition.getRow()));
                King.setPosition(new Position(3, RookPosition.getRow()));
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean requestPromotion(int PieceIndex) {
        /*
	Method to check the ability of a piece to be promoted
         */
        Piece MovedPiece = fetchPieceByIndex(PieceIndex);
        if (MovedPiece.getPieceName().toLowerCase().equals("pawn") && MovedPiece.getMovementsCount() > 1) {
            return true;
        }
        return false;
    }

    public void commitPromotion(int PieceIndex, String PieceType) {
        /*
	Method to commit a promotion for a pawn which is satisfied to be promoted
        Promotion is done by changing the given pawn piece type to be a queen, rook,
	knight,bishop on the same position.
         */
        Piece PromotedPiece = null;
        Position PiecePosition = fetchPiecePosition(PieceIndex);
        switch (PieceType) {
            case "queen":
                PromotedPiece = new Queen(PiecePosition);
                break;
            case "rook":
                PromotedPiece = new Rook(PiecePosition);
                break;
            case "knight":
                PromotedPiece = new Knight(PiecePosition);
                break;
            case "bishop":
                PromotedPiece = new Bishop(PiecePosition);
                break;
        }

        Pieces.set(PieceIndex, PromotedPiece);
    }

    protected Piece fetchPieceByPosition(Position position) {
        for (Piece piece : Pieces) {
            if (piece.getPosition().equals(position)) {
                return piece;
            }
        }
        return null;
    }

    public Position fetchPiecePosition(int Index) {
        return fetchPieceByIndex(Index).getPosition();
    }

    public String fetchPieceName(int Index) {
        return fetchPieceByIndex(Index).getClass().getSimpleName();
    }

    public ArrayList<Integer> fetchPiecesCanMoveTo(Position KingPosition, boolean captureRequest) {
        /*
	method aims to collect pieces that can move to a given position according to
	their move legality. The method is necessary for check confirmation at the first
	place
         */

        ArrayList<Integer> PossiblePieces = new ArrayList<>();
        int count = 0;
        for (Piece piece : Pieces) {
            if (piece.getAvailability()) {
                {
                    if (piece.confirmMoveLegality(KingPosition, captureRequest)) {
                        PossiblePieces.add(count);
                    }
                }
            }
            count++;
        }
        return PossiblePieces;
    }

    public int fetchPieceIndex(Position Pos) {
        int count = 0;
        for (Piece piece : Pieces) {
            if (Pos.equals(piece.getPosition())) {
                return count;
            }
            count++;
        }
        return -1;
    }

    public ArrayList<Position> fetchPossibleMovesOfTheKing() {
        King myKing = (King) fetchPieceByIndex(15);
        return myKing.generateEscapePositions();
    }

    protected Piece fetchPieceByIndex(int index) {
        return Pieces.get(index);
    }

    public ArrayList<PieceInfo> fetchPiecesWithPositions() {
        ArrayList<PieceInfo> PiecesInfo = new ArrayList<>();
        PieceInfo temp;
        int i = 0;
        for (Piece piece : Pieces) {
            temp = new PieceInfo(i, piece.getPieceName(), piece.getPosition());
            PiecesInfo.add(temp);
            i++;
        }
        return PiecesInfo;
    }

}
