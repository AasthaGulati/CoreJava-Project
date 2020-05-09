
package iplworld;

import java.sql.ResultSet;


public class Top10Bowlers {
     Integer Rank;
    Integer playerId;
    String playerName;
    Float bowlingEconomy,bowlingAverage;

   /* public Top10Bowlers(Integer Rank, Integer playerId, String playerName, Integer bowlingEconomy, Integer bowlingAverage) {
        this.Rank = Rank;
        this.playerId = playerId;
        this.playerName = playerName;
        this.bowlingEconomy = bowlingEconomy;
        this.bowlingAverage = bowlingAverage;
    }*/
    
    public void setvalues(ResultSet rsPlayerDetails){
        try{
        this.Rank = rsPlayerDetails.getInt("PlayerRank");
        this.playerId = rsPlayerDetails.getInt("PlayerId");
        this.playerName = rsPlayerDetails.getString("PlayerName");
        this.bowlingEconomy = rsPlayerDetails.getFloat("BowlingEconomy");
        this.bowlingAverage = rsPlayerDetails.getFloat("BowlingAverage");
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

    public Float getBowlingEconomy() {
        return bowlingEconomy;
    }

    public void setBowlingEconomy(Float bowlingEconomy) {
        this.bowlingEconomy = bowlingEconomy;
    }

    public Float getBowlingAverage() {
        return bowlingAverage;
    }

    public void setBowlingAverage(Float bowlingAverage) {
        this.bowlingAverage = bowlingAverage;
    }
    

    
}
