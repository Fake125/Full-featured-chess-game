package com.mycompany.chessgame.full_chess;

public class Bishop extends Piece {

    public Bishop(Position NewPosition) {
        this.setPosition(NewPosition);
    }

    @Override
    public boolean confirmMoveLegality(Position NextPos, boolean CaptureRequest) {
        if (MovementBehaviour.moveDiagonal(getPosition(), NextPos)) {
            return true;
        } else {
            return false;
        }
    }

}
