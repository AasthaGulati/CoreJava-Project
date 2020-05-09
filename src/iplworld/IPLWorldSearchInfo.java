/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iplworld;

/**
 *
 * @author Ramit
 */
public class IPLWorldSearchInfo {
     String playerName,playerDOB,playerRole,teamName;
    Integer playerId;

    public IPLWorldSearchInfo(String playerName, String playerDOB, String playerRole, String teamName, Integer playerId) {
        this.playerName = playerName;
        this.playerDOB = playerDOB;
        this.playerRole = playerRole;
        this.teamName = teamName;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }
    

    
}
