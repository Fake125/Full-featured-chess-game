package com.mycompany.chessgame.full_chess;

import java.util.ArrayList;

public final class MovementBehaviour {

    private MovementBehaviour() {

    }

    public static boolean moveDiagonal(Position Current, Position Next) {
        /*
	 A static method to confirm that a movement which represented by current
	 position and next position is legal in terms of diagonal moving basis or not
         */
        int ColumnsDifference = Math.abs(Current.getColumn() - Next.getColumn());
        int RowsDifference = Math.abs(Current.getRow() - Next.getRow());
        if (ColumnsDifference == RowsDifference && RowsDifference > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean moveAsKnight(Position Current, Position Next) {
        /*
         A static method to confirm that a movement which represented by current
         position and next position is legal in terms of knight movement rule
         */
        ArrayList<Position> PossiblePositions = new ArrayList<>();
        int CurrentRow = Current.getRow();
        int CurrentColumn = Current.getColumn();
        PossiblePositions.add(new Position((CurrentColumn + 2), (CurrentRow + 1)));
        PossiblePositions.add(new Position((CurrentColumn - 2), (CurrentRow + 1)));
        PossiblePositions.add(new Position((CurrentColumn + 2), (CurrentRow - 1)));
        PossiblePositions.add(new Position((CurrentColumn - 2), (CurrentRow - 1)));
        PossiblePositions.add(new Position((CurrentColumn + 1), (CurrentRow + 2)));
        PossiblePositions.add(new Position((CurrentColumn - 1), (CurrentRow + 2)));
        PossiblePositions.add(new Position((CurrentColumn + 1), (CurrentRow - 2)));
        PossiblePositions.add(new Position((CurrentColumn - 1), (CurrentRow - 2)));
        for (Position Possible : PossiblePositions) {
            if (Position.checkBorders(Possible)) {
                if (Next.equals(Possible)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean moveStraight(Position Current, Position Next) {
        /*
	 A static method to confirm that a movement which represented by current
	 position and next position is legal in terms of straight moving basis or not
         */
        if (Current.getColumn() == Next.getColumn() || Current.getRow() == Next.getRow()) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<Position> generateDiagonalPath(Position Current, Position Next) {
        /*
	 A static method to generate all positions in which the piece pass over them
	 from current to next positions according to the diagonal moving basis
         */
        ArrayList<Position> Path = new ArrayList<>();
        int FormulaFirstPart;
        int FirstLegalColumn;
        int SecondLegalColumn;
        int LegalColumn;
        int i = Current.getRow();
        while (i != Next.getRow()) {
            i = (Current.getRow() > Next.getRow()) ? i - 1 : i + 1;
            if (i == Next.getRow()) {
                break;
            }
            FormulaFirstPart = Math.abs(Current.getRow() - i);
            FirstLegalColumn = Math.abs(FormulaFirstPart + Current.getColumn());
            SecondLegalColumn = Math.abs(FormulaFirstPart - Current.getColumn());
            LegalColumn = (Current.getColumn() > Next.getColumn()) ? SecondLegalColumn : FirstLegalColumn;
            Path.add(new Position(LegalColumn, i));
        }
        return Path;
    }

    public static ArrayList<Position> generateStraightPathPositions(Position Current, Position Next) {
        /*
	 A static method to generate all positions in which the piece pass over them
	 from current to next positions according to the straight moving basis
         */
        ArrayList<Position> Path = new ArrayList<>();
        if (Current.getColumn() == Next.getColumn()) {
            int i = Current.getRow();
            while (i != Next.getRow()) {
                i = (Current.getRow() > Next.getRow()) ? i - 1 : i + 1;
                if (i == Next.getRow()) {
                    break;
                }
                Path.add(new Position(Current.getColumn(), i));

            }

        } else {
            int i = Current.getColumn();
            while (i != Next.getColumn()) {
                i = (Current.getColumn() > Next.getColumn()) ? i - 1 : i + 1;
                if (i == Next.getColumn()) {
                    break;
                }
                Path.add(new Position(i, Current.getRow()));
            }
        }

        return Path;
    }

    public static ArrayList<Position> getPath(Position Current, Position Next) {
        /*
	 A static method to detect the pattern of movement, either to be straight or
	 diagonal Then generate all positions in which the piece pass over them from
	 current to next positions according to the pattern moving basis
         */
        if (moveStraight(Current, Next)) {
            return generateStraightPathPositions(Current, Next);
        } else if (moveDiagonal(Current, Next)) {
            return generateDiagonalPath(Current, Next);
        }
        return null;
    }

}
