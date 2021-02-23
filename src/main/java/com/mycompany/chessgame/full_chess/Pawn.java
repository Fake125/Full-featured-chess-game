package com.mycompany.chessgame.full_chess;

public class Pawn extends Piece {

    public Pawn(Position NewPosition) {
        this.setPosition(NewPosition);

    }

    @Override
    public boolean confirmMoveLegality(Position NextPos, boolean CaptureRequest) {
        /*
        Pawn has special capturing behaviour, capturing behaviour for pawn is forward diagonal
        to left or right. while pawn has diagonal movement behaviour, pawn can move only straight forward
        if it won't capture. Also, pawn has a special movement which is moving two positions straight 
        forward instead of regular one straight forward mvoement. This special movement can only be done
        when the pawn is at initial position.
        */
        int CurrentColumn = getPosition().getColumn();
        int CurrentRow = getPosition().getRow();
        boolean increasing = (fetchMovementFromHistory(0).getRow() == 2) ? true : false;
        boolean CaptureLegalityRule = (MovementBehaviour.moveDiagonal(getPosition(), NextPos)
                && (Math.abs(CurrentRow - NextPos.getRow()) == 1));
        CaptureLegalityRule = (CaptureRequest) ? CaptureLegalityRule : false;
        boolean ForwardSameColumnRule = NextPos.getColumn() == CurrentColumn
                && MovementBehaviour.moveStraight(getPosition(), NextPos);
        ForwardSameColumnRule = (CaptureRequest) ? false : ForwardSameColumnRule;

        if (CaptureLegalityRule || ForwardSameColumnRule) {
            if (increasing) {
                if (CurrentRow < NextPos.getRow()) {
                    if (NextPos.getRow() - CurrentRow == 1) {
                        return true;
                    } else if (NextPos.getRow() - CurrentRow == 2 && getMovementsCount() == 1) {
                        return true;
                    }
                }
            } else {
                if (CurrentRow > NextPos.getRow()) {
                    if (CurrentRow - NextPos.getRow() == 1) {
                        return true;
                    } else if ((Math.abs(NextPos.getRow() - CurrentRow) == 2) && getMovementsCount() == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
