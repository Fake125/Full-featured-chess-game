package com.mycompany.chessgame.full_chess;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChessGame {

    private Player FirstPlayer;
    private Player SecondPlayer;
    private Player ActivePlayer;
    private Player Winner;
    private int TheTurn;
    public static int FIRSTPLAYER = 1;
    public static int SECONDPLAYER = 2;
    private ChessGameView View;

    public ChessGame(String FirstPlayerName, String SecondPlayerName, int FirstPlayerColor, int SecondPlayerColor) {
        if ((FirstPlayerColor != Player.LIGHT_COLOR && FirstPlayerColor != Player.DARK_COLOR)
                || (SecondPlayerColor != Player.LIGHT_COLOR && SecondPlayerColor != Player.DARK_COLOR)) {
            FirstPlayerColor = Player.LIGHT_COLOR;
            SecondPlayerColor = Player.DARK_COLOR;
        }

        FirstPlayer = new Player(FirstPlayerName, Player.UPPER_SIDE, FirstPlayerColor);
        SecondPlayer = new Player(SecondPlayerName, Player.LOWER_SIDE, SecondPlayerColor);
        TheTurn = ChessGame.FIRSTPLAYER;
        ActivePlayer = FirstPlayer;
        Winner = null;
    }

    public void setView(ChessGameView View) {
        this.View = View;
    }

    private ChessGameView getView() {
        return View;
    }

    public boolean checkTheTurn(int Player) {
        if (Player == TheTurn) {
            return true;
        } else {
            return false;
        }
    }

    private Player getInactivePlayer() {
        if (TheTurn == ChessGame.FIRSTPLAYER) {
            return SecondPlayer;
        } else {
            return FirstPlayer;
        }
    }

    private void changeTheTurn() {
        if (TheTurn == ChessGame.FIRSTPLAYER) {
            TheTurn = ChessGame.SECONDPLAYER;
            ActivePlayer = SecondPlayer;
        } else {
            TheTurn = ChessGame.FIRSTPLAYER;
            ActivePlayer = FirstPlayer;
        }
    }

    private boolean confirmCastlingRequest(Position NextPosition) {
        if (ActivePlayer.getCheckStauts()) {
            return false;
        }
        if (ActivePlayer.isPositionOccupied(NextPosition) || getInactivePlayer().isPositionOccupied(NextPosition)) {
            return false;
        }
        Position KingPosition = ActivePlayer.fetchPiecePosition(15);
        int column = (NextPosition.getColumn() == 3) ? 1 : 8;
        Position RookPosition = new Position(column, NextPosition.getRow());
        if (checkPathEmptiness(KingPosition, RookPosition)) {
            ArrayList<Position> CastlingPath = MovementBehaviour.generateStraightPathPositions(KingPosition,
                    NextPosition);
            CastlingPath.add(NextPosition);
            for (Position PathPosition : CastlingPath) {
                if (checkForChecking(PathPosition, getInactivePlayer()).size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean handleDraggedPiece(int PlayerNo, int PieceNo, Position NextPos) {
        /*
        Method is responsible to object's external communication(e.g view). The method is essential for 
        movements committing. Decision of either a movement being acceptable or not(returning true or false)
        will be done through passing the movement to collection of sequential conditions.
         */
        if (!checkTheTurn(PlayerNo)) {
            return false;
        }
        boolean confirm = false;
        Position PieceCurrentPosition = new Position(ActivePlayer.fetchPiecePosition(PieceNo).getColumn(),
                ActivePlayer.fetchPiecePosition(PieceNo).getRow());
        // Castling Requesting detection
        if (PieceNo == 15 && (NextPos.getColumn() == 3 || NextPos.getColumn() == 7)) {
            if (ActivePlayer.requestCastling(NextPos.getColumn())) {
                if (confirmCastlingRequest(NextPos)) {
                    int column = (NextPos.getColumn() == 3) ? 1 : 8;
                    Position RookPositionBefore = new Position(column, NextPos.getRow());
                    column = (RookPositionBefore.getColumn() == 1) ? 4 : 6;
                    Position RookPositionAfter = new Position(column, RookPositionBefore.getRow());
                    ActivePlayer.commitCastling(RookPositionBefore);
                    confirm = true;
                    getView().changePiecePositionOnBoard(RookPositionBefore, RookPositionAfter);
                }
            }
        }

        if (!confirm) {
            confirm = satisfyRules(false, ActivePlayer, getInactivePlayer(), PieceNo, NextPos);
        }
        if (confirm) {
            ActivePlayer.setCheckStatus(false);
            if (NextPos.getRow() == 8 || NextPos.getRow() == 1) {
                if (ActivePlayer.requestPromotion(PieceNo)) {
                    String PieceType = getView().promptPromotionOptions(PieceCurrentPosition);
                    ActivePlayer.commitPromotion(PieceNo, PieceType);
                }
            }
            ArrayList<Integer> InactiveCheckers = checkForChecking(getInactivePlayer().fetchPiecePosition(15),
                    ActivePlayer);
            boolean InactivePlayerCheckStatus = InactiveCheckers.size() > 0;
            if (InactivePlayerCheckStatus) {
                getInactivePlayer().setCheckStatus(true);
                if (checkForCheckmate(InactiveCheckers)) {
                    System.out.println("checkmate");
                    Winner = ActivePlayer;
                }
            }
            changeTheTurn();
        }

        return confirm;

    }

    protected boolean satisfyRules(boolean testing, Player Active, Player Inactive, int PieceIndex, Position Next) {
        /*
        Method to check legality of any movement. Movement legaility can be tested without actual position changing
        if testing value is true. The method widely used in movement legality checking. it forms the core
        of handleDraggedPiece and also for testing legal movements purpose.
         */
        if (!Position.checkBorders(Next)) {
            return false;
        }
        boolean CaptureRequest = false;
        boolean confirm = false;
        boolean isDraggedPieceMoved = false;

        if (Active.isPositionOccupied(Next)) {
            return false;
        } else if (Inactive.isPositionOccupied(Next)) {
            CaptureRequest = true;

        }

        Position CurrentPos = new Position();
        CurrentPos.setValues(Active.fetchPiecePosition(PieceIndex));

        if (!Active.isPieceKnight(PieceIndex)) {
            confirm = checkPathEmptiness(CurrentPos, Next);
        } else {
            confirm = true;
        }
        if (confirm) {
            confirm = Active.makeMove(PieceIndex, Next, CaptureRequest);
        }
        if (confirm) {
            isDraggedPieceMoved = true;
        }
        if (CaptureRequest && confirm) {
            Inactive.setPieceState(Next, false);
        }
        boolean ActivePlayerCheckStatus = checkForChecking(Active.fetchPiecePosition(15), Inactive).size() > 0;
        if (ActivePlayerCheckStatus) {
            confirm = false;
        }

        if (isDraggedPieceMoved && !confirm) {
            Active.undoMovement(PieceIndex);
            if (CaptureRequest) {
                Inactive.setPieceState(Next, true);
            }
        }

        if (confirm) {
            if (testing) {
                Active.undoMovement(PieceIndex);
                if (CaptureRequest) {
                    Inactive.setPieceState(Next, true);
                }
            } else {
                Active.setCheckStatus(false);
            }
            return confirm;
        }
        return false;

    }

    private boolean checkPathEmptiness(Position Current, Position Next) {
        /*
        Checking if the path(either it was straight or diagonal) between Current position and Next position is empty or not.
         */
        ArrayList<Position> Positions = null;
        if (MovementBehaviour.moveStraight(Current, Next)) {
            Positions = MovementBehaviour.generateStraightPathPositions(Current, Next);
        } else if (MovementBehaviour.moveDiagonal(Current, Next)) {
            Positions = MovementBehaviour.generateDiagonalPath(Current, Next);
        } else {
            return false;
        }

        for (Position position : Positions) {
            if (FirstPlayer.isPositionOccupied(position) || SecondPlayer.isPositionOccupied(position)) {
                return false;
            }
        }

        return true;
    }

    private ArrayList<Integer> checkForChecking(Position KingPosition, Player CheckingPlayer) {
        /*
        Method to collect real pieces that checking the enemy's king.
         */
        ArrayList<Integer> PossibleCheckers = CheckingPlayer.fetchPiecesCanMoveTo(KingPosition, true);
        ArrayList<Integer> RealCheckers = getRealPiecesCanMoveTo(CheckingPlayer, PossibleCheckers, KingPosition);
        return RealCheckers;
    }

    private ArrayList<Integer> getRealPiecesCanMoveTo(Player Active, ArrayList<Integer> Candidates, Position Target) {
        /*
        Method to collect pieces indices that can legally move to some position for a given player
        according to path emptiness.
         */
        ArrayList<Integer> RealCandidates = new ArrayList<>();
        for (Integer candidate : Candidates) {
            if (Active.isPieceKnight(candidate)) {
                RealCandidates.add(candidate);
            } else {
                if (checkPathEmptiness(Active.fetchPiecePosition(candidate), Target)) {
                    RealCandidates.add(candidate);
                }
            }

        }
        return RealCandidates;
    }

    private boolean checkForCheckmate(ArrayList<Integer> Checkers) {
        /*
        Given a list of pieces indices, the method decides that the checked player's king either dead or has
        a possible escaping movement.
         */

        Player CheckedPlayer = getInactivePlayer();
        Position KingPosition = CheckedPlayer.fetchPiecePosition(15);
        ArrayList<Position> CheckersPositions = new ArrayList<>();

        for (int checker : Checkers) {
            CheckersPositions.add(ActivePlayer.fetchPiecePosition(checker));
        }

        ArrayList<Position> PossibleKingMovements = CheckedPlayer.fetchPossibleMovesOfTheKing();

        for (Position PossibleMove : PossibleKingMovements) {
            if (satisfyRules(true, CheckedPlayer, ActivePlayer, 15, PossibleMove)) {
                return false;
            }
        }

        ArrayList<Integer> PossiblePiecesToFreeKing;
        for (Position CheckerPosition : CheckersPositions) {
            PossiblePiecesToFreeKing = getRealPiecesCanMoveTo(CheckedPlayer,
                    CheckedPlayer.fetchPiecesCanMoveTo(CheckerPosition, true), CheckerPosition);
            for (Integer PossiblePieceIndex : PossiblePiecesToFreeKing) {
                if (satisfyRules(true, CheckedPlayer, ActivePlayer, PossiblePieceIndex, CheckerPosition)) {
                    return false;
                }
            }
        }

        ArrayList<Position> Path;
        ArrayList<Integer> PossibleObstructers;
        for (Position checkerPosition : CheckersPositions) {
            Path = MovementBehaviour.getPath(checkerPosition, KingPosition);
            for (Position PathPosition : Path) {
                PossibleObstructers = getRealPiecesCanMoveTo(CheckedPlayer,
                        CheckedPlayer.fetchPiecesCanMoveTo(PathPosition, false), PathPosition);
                for (Integer PossibleObstructer : PossibleObstructers) {
                    if (satisfyRules(true, CheckedPlayer, ActivePlayer, PossibleObstructer, PathPosition)) {
                        return false;
                    }
                }

            }
        }

        return true;

    }

    public String getActivePlayerName() {
        return ActivePlayer.getName();
    }

    public String getActivePlayerColor() {
        return ActivePlayer.getColor();
    }

    public String getTheWinner() {
        if (Winner != null) {
            return Winner.getName();
        } else {
            return "";
        }
    }

    public String getCheckedPlayer() {
        if (FirstPlayer.getCheckStauts()) {
            String color = FirstPlayer.getColor();
            return "- " + color + " is Under Check";
        } else if (SecondPlayer.getCheckStauts()) {
            String color = SecondPlayer.getColor();
            return "- " + color + " is Under Check";
        } else {
            return "";
        }
    }

    public String getPlayerColor(int Player) {
        if (Player == FIRSTPLAYER) {
            return FirstPlayer.getColor();
        } else if (Player == SECONDPLAYER) {
            return SecondPlayer.getColor();
        }
        return "";
    }

    public String getPlayerName(int Player) {
        if (Player == FIRSTPLAYER) {
            return FirstPlayer.getName();
        } else if (Player == SECONDPLAYER) {
            return SecondPlayer.getName();
        }
        return "";
    }

    public ArrayList<PieceInfo> getPlayerPieces(int player) {
        if (player == 1) {
            return FirstPlayer.fetchPiecesWithPositions();
        } else if (player == 2) {
            return SecondPlayer.fetchPiecesWithPositions();
        } else {
            return null;
        }

    }

}
