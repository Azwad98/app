package com.example.bountyhunterapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BountyHunterStatList {
    //Maps to the list of fruit objects
    @SerializedName("stats")
    private List<BountyHunterStat> stats;

    /**
     * @return list of fruit objects
     */
    public List<BountyHunterStat> getStats() {
        return this.stats;
    }
}
