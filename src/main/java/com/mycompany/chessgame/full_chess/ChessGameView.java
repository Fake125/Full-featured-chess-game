package com.mycompany.chessgame.full_chess;

import com.mycompany.chessgame.full_chess.ChessGame;
import com.mycompany.chessgame.full_chess.Position;

public abstract class ChessGameView {

    private ChessGame Model;

    public void setModel(ChessGame Model) {
        this.Model = Model;
    }

    /*
    changePiecePositionOnBoard method is responsible for allowing to the model to change a piece position
    on the view of chess board. The method should be accurately implemented to apply a visual position
    changing for a located piece at PositionBefore to PositionAfter.
     */
    public abstract void changePiecePositionOnBoard(Position PositionBefore, Position PositionAfter);

    /*
    once a player's pawn reachs to the last row on his enemy side, promptPromotionOptions will be called  
    by the model then the method is responsible for prompting promotion options to the user. Implementation 
    of the method must include applying a visual changing for the located piece at PiecePosition according
    to the chosen type of promotion which will be decided by the user. 
     */
    public abstract String promptPromotionOptions(Position PiecePosition);

}
