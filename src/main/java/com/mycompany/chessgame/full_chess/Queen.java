package com.mycompany.chessgame.full_chess;

public class Queen extends Piece {

    public Queen(Position NewPosition) {
        this.setPosition(NewPosition);

    }

    @Override
    public boolean confirmMoveLegality(Position NextPos, boolean CaptureRequest) {
        boolean DiagonalRule = MovementBehaviour.moveDiagonal(getPosition(), NextPos);
        boolean StraightRule = MovementBehaviour.moveStraight(getPosition(), NextPos);
        if (StraightRule || DiagonalRule) {
            return true;
        } else {
            return false;
        }

    }

}
