package com.mycompany.chessgame.full_chess;

public class Rook extends Piece {

    public Rook(Position NewPosition) {
        this.setPosition(NewPosition);

    }

    @Override
    public boolean confirmMoveLegality(Position NextPos, boolean CaptureRequest) {
        if (MovementBehaviour.moveStraight(getPosition(), NextPos)) {
            return true;
        } else {
            return false;
        }
    }

}
