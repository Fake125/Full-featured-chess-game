package com.mycompany.chessgame.full_chess;

import java.util.ArrayList;

public class King extends Piece {

    public King(Position NewPosition) {
        this.setPosition(NewPosition);

    }

    @Override
    public boolean confirmMoveLegality(Position NextPos, boolean CaptureRequest) {
        boolean DiagonalMoveRule = MovementBehaviour.moveDiagonal(getPosition(), NextPos);
        boolean StraightMoveRule = MovementBehaviour.moveStraight(getPosition(), NextPos);
        if (DiagonalMoveRule || StraightMoveRule) {
            boolean RowsRangeRule = (Math.abs(NextPos.getRow() - getPosition().getRow()) <= 1);
            boolean ColumnsRangeRule = (Math.abs(NextPos.getColumn() - getPosition().getColumn()) <= 1);
            if (RowsRangeRule && ColumnsRangeRule) {
                return true;
            }
        }

        return false;
    }

    private void addPositionToList(ArrayList<Position> List, Position position) {
        if (Position.checkBorders(position)) {
            List.add(position);
        }
    }

    public ArrayList<Position> generateEscapePositions() {
        /*
		 * Method to generate possible positions where the king can move to
         */
        ArrayList<Position> PossibleMovements = new ArrayList<>();
        int CurrentColumn = getPosition().getColumn();
        int CurrentRow = getPosition().getRow();
        Position PossibleMove;

        // Adding possible at the same row movements
        PossibleMove = new Position(CurrentColumn + 1, CurrentRow);
        addPositionToList(PossibleMovements, PossibleMove);
        PossibleMove = new Position(CurrentColumn - 1, CurrentRow);
        addPositionToList(PossibleMovements, PossibleMove);

        // Adding possible forward movements
        PossibleMove = new Position(CurrentColumn, CurrentRow + 1);
        addPositionToList(PossibleMovements, PossibleMove);
        PossibleMove = new Position(CurrentColumn + 1, CurrentRow + 1);
        addPositionToList(PossibleMovements, PossibleMove);
        PossibleMove = new Position(CurrentColumn - 1, CurrentRow + 1);
        addPositionToList(PossibleMovements, PossibleMove);

        // Adding possible backward movements
        PossibleMove = new Position(CurrentColumn, CurrentRow - 1);
        addPositionToList(PossibleMovements, PossibleMove);
        PossibleMove = new Position(CurrentColumn + 1, CurrentRow - 1);
        addPositionToList(PossibleMovements, PossibleMove);
        PossibleMove = new Position(CurrentColumn - 1, CurrentRow - 1);
        addPositionToList(PossibleMovements, PossibleMove);

        return PossibleMovements;

    }

}
