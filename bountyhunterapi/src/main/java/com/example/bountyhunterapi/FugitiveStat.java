package com.example.bountyhunterapi;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class FugitiveStat extends Stat {
    @SerializedName("challengesComplete")
    private int challengesCompleted;
    @SerializedName("challengesFailed")
    private int challengesFailed;

    public FugitiveStat(int id, UUID gid, UUID uid, int points, int challengesCompleted, int chanllengesFailed, boolean won) {
        super(id,gid,uid,points,won);
        this.challengesCompleted = challengesCompleted;
        this.challengesFailed = chanllengesFailed;
    }

    public int getChallengesCompleted() {
        return this.challengesCompleted;
    }

    public int getChallengesFailed() {
        return this.challengesFailed;
    }

}
