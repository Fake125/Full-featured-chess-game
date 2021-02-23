package com.mycompany.chessgame.full_chess;

public class Knight extends Piece {

    public Knight(Position NewPosition) {
        this.setPosition(NewPosition);

    }

    @Override
    public boolean confirmMoveLegality(Position NextPos, boolean CaptureRequest) {
        if (MovementBehaviour.moveAsKnight(getPosition(), NextPos)) {
            return true;
        } else {
            return false;
        }
    }

}
