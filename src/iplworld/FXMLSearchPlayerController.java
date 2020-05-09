/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iplworld;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ramit
 */
public class FXMLSearchPlayerController implements Initializable {

  
    
   
    @FXML
    private TableView<IPLWorldSearchInfo> tvPlayerDetails;
    @FXML
    private TableColumn<IPLWorldSearchInfo, String> tcPlayerName;
    @FXML
    private TableColumn<IPLWorldSearchInfo, String> tcPlayerDOB;
    @FXML
    private TableColumn<IPLWorldSearchInfo, String> tcPlayerRole;
    @FXML
    private TableColumn<IPLWorldSearchInfo, Integer> tcPlayerId;
    @FXML
    private ImageView ivIPlayerProfilePic;
    @FXML
    private JFXTextField txtSearchPlayerName;
    ObservableList<IPLWorldSearchInfo> mPlayerDetails;
    @FXML
    private Label lblBattingAverage;
    @FXML
    private Label lblBowlingAverage;
    @FXML
    private Label lblBattingStrikeRate;
    @FXML
    private Label lblBowlingEconomy;
    @FXML
    private Label lblTotalMatches;
    @FXML
    private Label lblTotalInnings;
    @FXML
    private Label lblTotalHundreds;
    @FXML
    private Label lblTotalFifties;
    @FXML
    private Label lblTotalRuns;
    @FXML
    private Label lblTotalNoOfNotOuts;
    @FXML
    private Label lblBestScore;
    @FXML
    private Label lblTotalNoOfBallsFaced;
    @FXML
    private Label lblTotalRunsGiven;
    @FXML
    private Label lblTotalWickets;
    @FXML
    private Label lblBestBowlingFigure;
    @FXML
    private Label lblTotalBallsBowled;
    @FXML
    private Label lblTotalDucks;
    @FXML
    private Label lblPlayerName;
    @FXML
    private TableColumn<IPLWorldSearchInfo, String> tcPlayerTeam;
    @FXML
    private Label lblPlayerTeam;
    @FXML
    private Label lblPlayerDOB;
    @FXML
    private Label lblPlayerRole;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //  mPlayerTeam = FXCollections.observableArrayList();//we allocate memory to mPlayerTeam and mPlayerDetails
        mPlayerDetails = FXCollections.observableArrayList();
        tvPlayerDetails.setItems(mPlayerDetails);   //this will set values in table view 
    //    fillPlayerTeam();//calls fillPlayerTeam Function
        tcPlayerName.setCellValueFactory(new PropertyValueFactory<>("PlayerName"));//connect table view to database
        tcPlayerDOB.setCellValueFactory(new PropertyValueFactory<>("PlayerDOB"));
        tcPlayerRole.setCellValueFactory(new PropertyValueFactory<>("PlayerRole"));
        tcPlayerTeam.setCellValueFactory(new PropertyValueFactory<>("TeamName"));
        tcPlayerId.setCellValueFactory(new PropertyValueFactory<>("PlayerId"));

        fillPlayerDetails();// calls fillPlayerDetails
   
    }    
    
    
    
     private Connection makeMySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(Constants.URLToOpenMYSQLDatabase, "root", "Pass@123");
        } catch (Exception ex) {
            return null;
        }
    }


    private void fillPlayerAllDetails(ActionEvent event) {
         fillPlayerDetails();
    }
    
     private void fillPlayerDetails() {
        try {
            Connection conIPLWorld = makeMySqlConnection();
           // int playerTeamId = cbPlayerTeam.getSelectionModel().getSelectedIndex()+1;
            mPlayerDetails.clear();
            PreparedStatement psPlayerDetails = conIPLWorld.prepareStatement("select PlayerName,DateOfBirth,PlayerRoleName,PlayerId,p.Image,TeamName from player p inner join lkpPlayerRole lkpPR on p.PlayerRoleId=lkpPR.PlayerRoleId inner join lkpPlayerTeam lkpPT on p.TeamId=lkpPT.TeamId where p.IsActive=1;");
            ResultSet rsPlayerDetails = psPlayerDetails.executeQuery();
             
            while (rsPlayerDetails.next()) {
                IPLWorldSearchInfo player = new IPLWorldSearchInfo(rsPlayerDetails.getString(1), rsPlayerDetails.getString(2), rsPlayerDetails.getString(3),rsPlayerDetails.getString(6), rsPlayerDetails.getInt(4));
                mPlayerDetails.add(player);
            }
                 tvPlayerDetails.getSelectionModel().selectFirst();
                 selectdefaultvalue();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void mouseRelease(MouseEvent event) {
        
        fetchPlayerData();
    
    }
    
    private void fetchesPlayerStats(int playerID) {
        try {
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psfetchesPlayerStats = conIPLWorld.prepareStatement("select TotalMatches,TotalInnings,TotalRuns,TotalWickets,TotalBallsBowled,TotalRunsGiven,TotalNoOfNotOuts,TotalHundreds,TotalFifties,BestScore,BestBowlingFigure,TotalDucks,TotalNoOfBallsFaced,Image from PlayerStats PS INNER JOIN Player P ON P.PlayerId=PS.PlayerId where P.PlayerId=? and P.IsActive=1");
            psfetchesPlayerStats.setInt(1, playerID);

            ResultSet rsfetchesPlayerStats = psfetchesPlayerStats.executeQuery();
            if(rsfetchesPlayerStats.next()) {
                //Code to show image from database
                Blob playerProfilePic = rsfetchesPlayerStats.getBlob("Image");
                InputStream inputStream = playerProfilePic.getBinaryStream();
                Image playerPic = new Image(inputStream) ; 
                ivIPlayerProfilePic.setImage(playerPic);
                inputStream.close();
                //////////////////////////////////////////////////////////
                lblTotalMatches.setText(rsfetchesPlayerStats.getString(1));
                lblTotalInnings.setText(rsfetchesPlayerStats.getString(2));
                lblTotalRuns.setText(rsfetchesPlayerStats.getString(3));
                lblTotalWickets.setText(rsfetchesPlayerStats.getString(4));
                lblTotalBallsBowled.setText(rsfetchesPlayerStats.getString(5));
                lblTotalRunsGiven.setText(rsfetchesPlayerStats.getString(6));
                lblTotalNoOfNotOuts.setText(rsfetchesPlayerStats.getString(7));
                lblTotalHundreds.setText(rsfetchesPlayerStats.getString(8));
                lblTotalFifties.setText(rsfetchesPlayerStats.getString(9));
                lblBestScore.setText(rsfetchesPlayerStats.getString(10));
                lblBestBowlingFigure.setText(rsfetchesPlayerStats.getString(11));
                lblTotalDucks.setText(rsfetchesPlayerStats.getString(12));
                lblTotalNoOfBallsFaced.setText(rsfetchesPlayerStats.getString(13));
                int TotalInnings=rsfetchesPlayerStats.getInt(2);
                int TotalRuns=rsfetchesPlayerStats.getInt(3);
                int TotalNoOfNotOuts=rsfetchesPlayerStats.getInt(7);
                int TotalRunsGiven=rsfetchesPlayerStats.getInt(6);
                int TotalWickets=rsfetchesPlayerStats.getInt(4);
                int TotalBallsBowled=rsfetchesPlayerStats.getInt(5);
                int TotalNoOfBallsFaced= rsfetchesPlayerStats.getInt(13);
                float battingAvg=TotalInnings!=0 && (TotalInnings-TotalNoOfNotOuts>0)?(TotalRuns/((TotalInnings- TotalNoOfNotOuts))):0;
                lblBattingAverage.setText(String.valueOf(battingAvg));
                float bowlingAvg=TotalWickets!=0 &&TotalWickets!=0 ? (TotalRunsGiven/TotalWickets):0;
                lblBowlingAverage.setText(String.valueOf(bowlingAvg));
                float bowlingEconomy=TotalRunsGiven!=0 && TotalBallsBowled!=0 ? (TotalRunsGiven*6/ TotalBallsBowled):0;
                lblBowlingEconomy.setText(String.valueOf(bowlingEconomy));
                float battingStrikeRate=TotalRuns!=0 &&TotalNoOfBallsFaced!=0 ?(TotalRuns*100/TotalNoOfBallsFaced):0;
                lblBattingStrikeRate.setText(String.valueOf(battingStrikeRate));
            }
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
        private void fetchPlayerData(){
             IPLWorldSearchInfo player = tvPlayerDetails.getSelectionModel().getSelectedItem();
            lblPlayerName.setText(player.getPlayerName());
            lblPlayerDOB.setText(player.getPlayerDOB());
       
            lblPlayerTeam.setText(player.getTeamName());
            if (player.getPlayerRole().equals("All Rounder")) {
            lblPlayerRole.setText(player.getPlayerRole());
            } else if (player.getPlayerRole().equals("Batsman")) {
             lblPlayerRole.setText(player.getPlayerRole());
            } else {
             lblPlayerRole.setText(player.getPlayerRole());
            }
         

        // int playerID=player.getPlayerId();
        fetchesPlayerStats(player.getPlayerId());
        
    }
     private void selectdefaultvalue(){
        
            IPLWorldSearchInfo player  = tvPlayerDetails.getSelectionModel().getSelectedItem();
            fetchPlayerData();
            fetchesPlayerStats(player.playerId);
}
   @FXML
    private void searchPlayerByName(KeyEvent event) {
         try{
          mPlayerDetails.clear();
          Connection conIPLWorld = makeMySqlConnection();
          PreparedStatement psSearchedPlayer = conIPLWorld.prepareStatement("Select PlayerName,DateOfBirth,PlayerRoleName,PlayerId,p.Image,TeamName from player p inner join lkpPlayerRole lkpPR on p.PlayerRoleId=lkpPR.PlayerRoleId inner join lkpPlayerTeam lkpPT on p.TeamId=lkpPT.TeamId Where PlayerName LIKE '%"+ txtSearchPlayerName.getText()+ "%' and p.IsActive=1");
            ResultSet rsSearchedPlayer = psSearchedPlayer.executeQuery();
            while(rsSearchedPlayer.next()){
                IPLWorldSearchInfo player = new IPLWorldSearchInfo(rsSearchedPlayer.getString(1), rsSearchedPlayer.getString(2), rsSearchedPlayer.getString(3), rsSearchedPlayer.getString(6), rsSearchedPlayer.getInt(4));
                mPlayerDetails.add(player);
                
                
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
}
    }

    @FXML
    private void backToMainMenu(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLIPLWorldMenu.fxml"));/* Exception */
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
             System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void exitfunction(ActionEvent event) {
        System.exit(0);
    }
}