package com.example.bountyhunterapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FugitiveStatList {
    //Maps to the list of fruit objects
    @SerializedName("stats")
    private List<FugitiveStat> stats;

    /**
     * @return list of fruit objects
     */
    public List<FugitiveStat> getStats() {
        return this.stats;
    }
}
