package com.mycompany.chessgame.full_chess;

import java.util.ArrayList;

public abstract class Piece {

    private ArrayList<Position> Movements;
    protected Position CurrentPosition;
    private boolean Available;

    public Piece() {
        CurrentPosition = new Position(0, 0);
        Available = true;
        Movements = new ArrayList<>();
    }

    public void setPosition(Position NextPosition) {
        CurrentPosition.setValues(NextPosition);
        Movements.add(NextPosition);
    }

    public void setAvailability(boolean available) {
        this.Available = available;
    }

    public Position getPosition() {
        return CurrentPosition;
    }

    public boolean getAvailability() {
        return Available;
    }

    public Position fetchMovementFromHistory(int index) {
        return Movements.get(index);
    }

    public int getMovementsCount() {
        return Movements.size();
    }

    public String getPieceName() {
        return getClass().getSimpleName();
    }

    public void backtrack() {
        /*
	Method to remove the recent added position and reset the current position to the previous one
         */
        int MovesCount = Movements.size();
        if (MovesCount > 1) {
            Position PreviousPosition = Movements.get(Movements.size() - 2);
            setPosition(PreviousPosition);
        }
    }

    /*
    confirmMoveLegality is responsible for satisfying rules of movement according to piece's type.
    */
    public abstract boolean confirmMoveLegality(Position NextPos, boolean CaptureRequest);

}
