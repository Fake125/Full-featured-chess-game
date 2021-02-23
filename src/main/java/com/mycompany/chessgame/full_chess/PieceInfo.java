package com.mycompany.chessgame.full_chess;

public class PieceInfo {
    
    private String Type;
    private int Index;
    private Position position;
    
    public PieceInfo(int index,String type, Position position){
        Type = type;
        Index = index;
        this.position = position;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public void setIndex(int Index) {
        this.Index = Index;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getType() {
        return Type;
    }

    public int getIndex() {
        return Index;
    }

    public Position getPosition() {
        return position;
    }
    
    
    
    
}
