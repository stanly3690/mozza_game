package com.board.stann.myapplication.db;

/**
 * Created by User on 17/01/2015.
 */
public class Player {

    private String name;
    private long wins;
    private long losses;
    private long draws;
    private long lastNumMovements;
    private String lastElapsedTime;

    public Player(String name, long wins, long losses, long lastNumMovements, String elapsedTime) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.lastNumMovements = lastNumMovements;
        this.lastElapsedTime = elapsedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public long getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public long getDraws() {
        return draws;
    }

    public void setDraws(long draws) {
        this.draws = draws;
    }

    public long getLastNumMovements() {
        return lastNumMovements;
    }

    public void setLastNumMovements(long lastNumMovements) {
        this.lastNumMovements = lastNumMovements;
    }

    public String getLastElapsedTime() {
        return lastElapsedTime;
    }

    public void setLastElapsedTime(String lastElapsedTime) {
        this.lastElapsedTime = lastElapsedTime;
    }
}