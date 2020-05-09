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
public final class Constants {
    private Constants () { // private constructor
    }
    //Query to fetch Teams from database
    public static String QueryTOLKPPLayerTeam="Select TeamId,TeamName from lkpPlayerTeam Where IsActive=1";
    
    //
    public static String URLToOpenMYSQLDatabase="jdbc:mysql://localhost:3307/IPLWorld";
}
