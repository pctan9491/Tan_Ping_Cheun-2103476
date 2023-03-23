package com.example.tan_ping_cheun_2103476;

public class ScoreInfo {
    private String name;
    private int score;

    public ScoreInfo(String name, int score){
        this.name=name;
        this.score=score;
    }

    public String getName(){
        return name;
    }
    public int getScore(){
        return score;
    }
}
