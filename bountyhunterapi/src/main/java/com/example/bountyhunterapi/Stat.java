package com.example.bountyhunterapi;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Stat {
    @SerializedName("id")
    private int id;
    @SerializedName("gid")
    private UUID gid;
    @SerializedName("uid")
    private UUID uid;
    @SerializedName("points")
    private int points;
    @SerializedName("won")
    private boolean won;

    public Stat(int id, UUID gid, UUID uid, int points, boolean won) {
        this.id = id;
        this.gid = gid;
        this.uid = uid;
        this.points = points;
        this.won = won;
    }

    public int getId() {
        return this.id;
    }

    public UUID getGid() {
        return this.gid;
    }

    public UUID getUid() {
        return this.uid;
    }

    public int getPoints() {
        return this.points;
    }

    public boolean isWon() {
        return this.won;
    }

}
