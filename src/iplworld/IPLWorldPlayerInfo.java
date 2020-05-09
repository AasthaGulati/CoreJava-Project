
package iplworld;

public class IPLWorldPlayerInfo { 
    String playerName,playerDOB,playerRole;
    Integer playerId;

    public IPLWorldPlayerInfo(String playerName, String playerDOB, String playerRole, Integer playerId) {
        this.playerName = playerName;
        this.playerDOB = playerDOB;
        this.playerRole = playerRole;
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerDOB() {
        return playerDOB;
    }

    public void setPlayerDOB(String playerDOB) {
        this.playerDOB = playerDOB;
    }

    public String getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(String playerRole) {
        this.playerRole = playerRole;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }
    
    
}
