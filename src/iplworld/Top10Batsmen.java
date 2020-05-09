package iplworld;

import java.sql.ResultSet;

public class Top10Batsmen {
    Integer Rank;
    Integer playerId;
    String playerName;
    Float battingStrikeRate,battingAverage;

  /*  public Top10Batsmen(Integer Rank, Integer playerId, String playerName, Integer battingStrikeRate, Integer battingAverage) {
        this.Rank = Rank;
        this.playerId = playerId;
        this.playerName = playerName;
        this.battingStrikeRate = battingStrikeRate;
        this.battingAverage = battingAverage;
    }*/
    public void setvalues(ResultSet rsPlayerDetails){
        try{
        this.Rank = rsPlayerDetails.getInt("PlayerRank");
        this.playerId = rsPlayerDetails.getInt("PlayerId");
        this.playerName = rsPlayerDetails.getString("PlayerName");
        this.battingStrikeRate = rsPlayerDetails.getFloat("BattingStrikeRate");
        this.battingAverage = rsPlayerDetails.getFloat("BattingAverage");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
    }
    }
    public Integer getRank() {
        return Rank;
    }

    public void setRank(Integer Rank) {
        this.Rank = Rank;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Float getBattingStrikeRate() {
        return battingStrikeRate;
    }

    public void setBattingStrikeRate(Float battingStrikeRate) {
        this.battingStrikeRate = battingStrikeRate;
    }

    public Float getBattingAverage() {
        return battingAverage;
    }

    public void setBattingAverage(Float battingAverage) {
        this.battingAverage = battingAverage;
    }

    
   
}
