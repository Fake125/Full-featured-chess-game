package com.mycompany.chessgame.full_chess;

public class Position {

    private int column;
    private int row;

    public Position() {
        column = 0;
        row = 0;
    }

    public Position(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return (column);
    }

    public int getRow() {
        return (row);
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setValues(Position NewPosition) {
        if (!checkBorders(NewPosition)) {
            return;
        }
        this.column = NewPosition.getColumn();
        this.row = NewPosition.getRow();
    }

    public boolean equals(Position Pos) {
        if (this.getColumn() == Pos.getColumn() && this.getRow() == Pos.getRow()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkBorders(Position Next) {
        /*
        A static method to check if the position is a possible position inside chess
        board or not
         */
        boolean ColumnCheck = (Next.getColumn() <= 8 && Next.getColumn() >= 1);
        boolean RowCheck = (Next.getRow() <= 8 && Next.getRow() >= 1);

        if (ColumnCheck && RowCheck) {
            return true;
        } else {
            return false;
        }

    }

    public String toString() {
        return column + " - " + row;
    }

}
