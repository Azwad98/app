package com.example.bountyhunterapi;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class BountyHunterStat extends Stat {
    @SerializedName("captures")
    private int captures;

    public BountyHunterStat(int id, UUID gid, UUID uid, int points, int captures, boolean won) {
        super(id,gid,uid,points,won);
        this.captures = captures;
    }

    public int getCaptures() {
        return this.captures;
    }

}
