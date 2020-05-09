package iplworld;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLIPLWorld1Controller implements Initializable {
    
    private Label label;
     File selectedFile=null;

    @FXML
    private JFXComboBox<String> cbPlayerTeam;
    @FXML
    private TableView<IPLWorldPlayerInfo> tvPlayerDetails;
    @FXML
    private TableColumn<IPLWorldPlayerInfo, String> tcPlayerName;
    @FXML
    private TableColumn<IPLWorldPlayerInfo, String> tcPlayerDOB;
    @FXML
    private TableColumn<IPLWorldPlayerInfo, String> tcPlayerRole;
    
    ObservableList<String> mPlayerTeam;
    ObservableList<IPLWorldPlayerInfo> mPlayerDetails;
    
    @FXML
    private JFXTextField txtPlayerName;
    @FXML
    private JFXTextField txtTotalMatches;
    @FXML
    private JFXTextField txtTotalInnings;
    @FXML
    private JFXTextField txtTotalRuns;
    @FXML
    private JFXTextField txtTotalWickets;
    @FXML
    private JFXTextField txtTotalBallsBowled;
    @FXML
    private JFXTextField txtTotalRunsGiven;
    @FXML
    private JFXTextField txtTotalNoOfNotOuts;
    @FXML
    private JFXTextField txtTotalHundreds;
    @FXML
    private JFXTextField txtTotalFifties;
    @FXML
    private JFXTextField txtBestScore;
    @FXML
    private JFXTextField txtBestBowlingFigure;
    @FXML
    private JFXTextField txtTotalDucks;
    @FXML
    private DatePicker dpPlayerDOB;
    @FXML
    private JFXRadioButton rbIsAllRounder;
    @FXML
    private ToggleGroup tg;
    @FXML
    private JFXRadioButton rbIsBatsmen;
    @FXML
    private JFXRadioButton rbIsBowler;
    @FXML
    private TableColumn<IPLWorldPlayerInfo, Integer> tcPlayerId;
    @FXML
    private Label txtBattingAverage;
    @FXML
    private Label txtBowlingAverage;
    @FXML
    private Label txtBattingStrikeRate;
    @FXML
    private Label txtBowlingEconomy;
    @FXML
    private JFXTextField txtTotalNoOfBallsFaced;
    @FXML
    private ImageView ivIPlayerProfilePic;
    @FXML
    private JFXComboBox<String> cbupdatePlayerTeam;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mPlayerTeam = FXCollections.observableArrayList();//we allocate memory to mPlayerTeam and mPlayerDetails
        mPlayerDetails = FXCollections.observableArrayList();
        tvPlayerDetails.setItems(mPlayerDetails);   //this will set values in table view 
        fillPlayerTeam();//calls fillPlayerTeam Function
        tcPlayerName.setCellValueFactory(new PropertyValueFactory<>("PlayerName"));//connect table view to database
        tcPlayerDOB.setCellValueFactory(new PropertyValueFactory<>("PlayerDOB"));
        tcPlayerRole.setCellValueFactory(new PropertyValueFactory<>("PlayerRole"));
        tcPlayerId.setCellValueFactory(new PropertyValueFactory<>("PlayerId"));

        fillPlayerDetails();// calls fillPlayerDetails

    }

    //Fills Select Team Comobo Box
    private void fillPlayerTeam() {
        try {
            //Make Connection to Database
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psAllTeams = conIPLWorld.prepareStatement("Select TeamId,TeamName from lkpPlayerTeam Where IsActive=1");
            ResultSet rsAllTeams = psAllTeams.executeQuery();
            mPlayerTeam = FXCollections.observableArrayList();
            //fetch teams from datbase and add into combobox
            while (rsAllTeams.next()) {
                mPlayerTeam.add(rsAllTeams.getInt("TeamId") - 1, rsAllTeams.getString("TeamName"));
            }
            cbPlayerTeam.setItems(mPlayerTeam);
            cbupdatePlayerTeam.setItems(mPlayerTeam);
            conIPLWorld.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    //Creates MySql connection
    private Connection makeMySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3307/IPLWorld", "root", "Pass@123");
        } catch (Exception ex) {
            return null;
        }
    }

    //fill player details in tableview
    private void fillPlayerDetails() {
        try {
            Connection conIPLWorld = makeMySqlConnection();
            int playerTeamId = cbPlayerTeam.getSelectionModel().getSelectedIndex()+1;
            mPlayerDetails.clear();
            PreparedStatement psPlayerDetails = conIPLWorld.prepareStatement("select PlayerName,DateOfBirth,PlayerRoleName,PlayerId,Image from player p inner join lkpPlayerRole lkpPR on p.PlayerRoleId=lkpPR.PlayerRoleId where TeamId="+playerTeamId+" AND p.IsActive=1");
            ResultSet rsPlayerDetails = psPlayerDetails.executeQuery();
             
            while (rsPlayerDetails.next()) {
                IPLWorldPlayerInfo player = new IPLWorldPlayerInfo(rsPlayerDetails.getString(1), rsPlayerDetails.getString(2), rsPlayerDetails.getString(3), rsPlayerDetails.getInt(4));
                mPlayerDetails.add(player);
                 tvPlayerDetails.getSelectionModel().selectFirst();
                selectdefaultvalue();
            }
              if(tvPlayerDetails.getSelectionModel().getSelectedItem()==null){
             resetFunction();
         }
       
          
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
// fills data into text fields on selection from table view

    @FXML
    private void mouseRelease(MouseEvent event) {

        IPLWorldPlayerInfo player = tvPlayerDetails.getSelectionModel().getSelectedItem();
        txtPlayerName.setText(player.getPlayerName());
        dpPlayerDOB.setValue(LocalDate.parse(player.getPlayerDOB()));
        if (player.getPlayerRole().equals("All Rounder")) {
            rbIsAllRounder.setSelected(true);
        } else if (player.getPlayerRole().equals("Batsman")) {
            rbIsBatsmen.setSelected(true);
        } else {
            rbIsBowler.setSelected(true);
        }
        cbupdatePlayerTeam.getSelectionModel().select(cbPlayerTeam.getSelectionModel().getSelectedItem());
        // int playerID=player.getPlayerId();
        fetchesPlayerStats(player.getPlayerId());
    }

    //fills player stats on basis of respective player id
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
                txtTotalMatches.setText(rsfetchesPlayerStats.getString(1));
                txtTotalInnings.setText(rsfetchesPlayerStats.getString(2));
                txtTotalRuns.setText(rsfetchesPlayerStats.getString(3));
                txtTotalWickets.setText(rsfetchesPlayerStats.getString(4));
                txtTotalBallsBowled.setText(rsfetchesPlayerStats.getString(5));
                txtTotalRunsGiven.setText(rsfetchesPlayerStats.getString(6));
                txtTotalNoOfNotOuts.setText(rsfetchesPlayerStats.getString(7));
                txtTotalHundreds.setText(rsfetchesPlayerStats.getString(8));
                txtTotalFifties.setText(rsfetchesPlayerStats.getString(9));
                txtBestScore.setText(rsfetchesPlayerStats.getString(10));
                txtBestBowlingFigure.setText(rsfetchesPlayerStats.getString(11));
                txtTotalDucks.setText(rsfetchesPlayerStats.getString(12));
                txtTotalNoOfBallsFaced.setText(rsfetchesPlayerStats.getString(13));
                int TotalInnings=rsfetchesPlayerStats.getInt(2);
                int TotalRuns=rsfetchesPlayerStats.getInt(3);
                int TotalNoOfNotOuts=rsfetchesPlayerStats.getInt(7);
                int TotalRunsGiven=rsfetchesPlayerStats.getInt(6);
                int TotalWickets=rsfetchesPlayerStats.getInt(4);
                int TotalBallsBowled=rsfetchesPlayerStats.getInt(5);
                int TotalNoOfBallsFaced= rsfetchesPlayerStats.getInt(13);
                float battingAvg=TotalInnings!=0 && (TotalInnings-TotalNoOfNotOuts>0)?(TotalRuns/((TotalInnings- TotalNoOfNotOuts))):0;
                txtBattingAverage.setText(String.valueOf(battingAvg));
                float bowlingAvg=TotalWickets!=0 &&TotalWickets!=0 ? (TotalRunsGiven/TotalWickets):0;
                txtBowlingAverage.setText(String.valueOf(bowlingAvg));
                float bowlingEconomy=TotalRunsGiven!=0 && TotalBallsBowled!=0 ? (TotalRunsGiven*6/ TotalBallsBowled):0;
                txtBowlingEconomy.setText(String.valueOf(bowlingEconomy));
                float battingStrikeRate=TotalRuns!=0 &&TotalNoOfBallsFaced!=0 ?(TotalRuns*100/TotalNoOfBallsFaced):0;
                txtBattingStrikeRate.setText(String.valueOf(battingStrikeRate));
            }
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    @FXML
    private void exitfunction(ActionEvent event) {
        System.exit(0);
    }


    @FXML
    private void fillPlayerAllDetails(ActionEvent event) {
        fillPlayerDetails();
    }

  
    @FXML
    private void deleteTeamPlayer(ActionEvent event) {
        showConfirmation();
    }
    
    private void deletePlayer(){
         try{
            int playerId=tvPlayerDetails.getSelectionModel().getSelectedItem().getPlayerId();
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psPlayerData = conIPLWorld.prepareStatement("update player set IsActive=0 where PlayerId=?");
            psPlayerData.setInt(1, playerId);
            psPlayerData.executeUpdate();
            deletePlayerStatstableData(conIPLWorld,playerId);
            deletePlayerTeamMappingTableData(conIPLWorld,playerId);
            
    }catch(Exception ex){
             System.out.println(ex.getMessage());
    }
    }
    
    
     private void deletePlayerStatstableData(Connection conIPLWorld,int playerId){
        try {
             PreparedStatement psPlayerStatsData = conIPLWorld.prepareStatement("update PlayerStats set IsActive=0 where PlayerId=?");
             psPlayerStatsData.setInt(1, playerId); 
             psPlayerStatsData.executeUpdate();
           //  mPlayerDetails.clear();
             
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
}
    
    
      private void deletePlayerTeamMappingTableData(Connection conIPLWorld,int playerId){
        try {
             PreparedStatement psPlayerTeamMappingData = conIPLWorld.prepareStatement("update PlayerTeamMapping set IsActive=0 where PlayerId=?");
             psPlayerTeamMappingData.setInt(1, playerId); 
         
             psPlayerTeamMappingData.executeUpdate();
             
             showMessage("Player deleted successfully");
             fillPlayerDetails();
            // resetFunction();
             
             //mPlayerDetails.clear();
             
        } catch (Exception ex) {
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
    private void updatePlayerDetails(ActionEvent event) {
         updateConfirmation();
    }
    
    private void updatePlayer(){
        try {
            int playerId=tvPlayerDetails.getSelectionModel().getSelectedItem().getPlayerId();
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psPlayerData = conIPLWorld.prepareStatement("update player set PlayerName=?,Image=?,DateOfBirth=?,PlayerRoleId=?,TeamId=? where PlayerId=?");
           // psPlayerData.setInt(1, playerId);
            psPlayerData.setString(1, txtPlayerName.getText());
            if(selectedFile!=null){
                FileInputStream fin = new FileInputStream(selectedFile);
                psPlayerData.setBinaryStream(2, fin, (int)selectedFile.length());
            }
            else{
              psPlayerData.setString(2,"");
            }
          //  FileInputStream fin = new FileInputStream(selectedFile);
           // psPlayerData.setBinaryStream(2, fin, (int)selectedFile.length());
            psPlayerData.setString(3,dpPlayerDOB.getValue().toString());
            if(rbIsAllRounder.isSelected())
            psPlayerData.setString(4,"1");
            if(rbIsBatsmen.isSelected())
            psPlayerData.setString(4,"2");
            if(rbIsBowler.isSelected())
            psPlayerData.setString(4,"3");
            psPlayerData.setInt(5, cbupdatePlayerTeam.getSelectionModel().getSelectedIndex()+1);
            psPlayerData.setInt(6, playerId);
            psPlayerData.executeUpdate();
            updatePlayerStats(conIPLWorld,playerId);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
     private void updatePlayerStats(Connection conIPLWorld,int playerId){  
           try{
           PreparedStatement psPlayerData = conIPLWorld.prepareStatement("update PlayerStats set TotalMatches=?,TotalInnings=?,TotalRuns=?,TotalWickets=?,TotalBallsBowled=?,TotalRunsGiven=?,TotalNoOfNotOuts=?,TotalHundreds=?,TotalFifties=?,BestScore=?,BestBowlingFigure=?,TotalDucks=?,TotalNoOfBallsFaced=? where PlayerId=?");
            psPlayerData.setString(1,txtTotalMatches.getText());
            psPlayerData.setString(2,txtTotalInnings.getText());
            psPlayerData.setString(3,txtTotalRuns.getText());
            psPlayerData.setString(4,txtTotalWickets.getText());
           psPlayerData.setString(5,txtTotalBallsBowled.getText());
           psPlayerData.setString(6,txtTotalRunsGiven.getText());
           psPlayerData.setString(7,txtTotalNoOfNotOuts.getText());
           psPlayerData.setString(8,txtTotalHundreds.getText());
           psPlayerData.setString(9,txtTotalFifties.getText());
           psPlayerData.setString(10,txtBestScore.getText());
           psPlayerData.setString(11,txtBestBowlingFigure.getText());
           psPlayerData.setString(12,txtTotalDucks.getText());
           psPlayerData.setString(13,txtTotalNoOfBallsFaced.getText());
           psPlayerData.setInt(14, playerId);
           psPlayerData.executeUpdate();
            showMessage("Player updated successfully");
           }catch(Exception ex){
               System.out.println(ex.getMessage());
       }
       }
     
    /* private void PlayerTeamMapping(Connection conIPLWorld,int playerId){
         try {
             PreparedStatement psPlayerTeamData = conIPLWorld.prepareStatement("insert into PlayerTeamMapping(TeamId) where);
         } catch (Exception e) {
         }
         
     }*/
      private void showMessage(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("IPL World");
        a.setContentText(msg);
        a.show();
      }
     
     
     private void showConfirmation() {
 
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Delete Player");
      alert.setHeaderText("Are you sure you want to delete this Player?");
     // alert.setContentText("C:/MyFile.txt");
 
      // option != null.
      Optional<ButtonType> option = alert.showAndWait();
 
      if (option.get() == ButtonType.OK) {
       deletePlayer();
      }  
     }
     
     private void updateConfirmation() {
 
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Update Player");
      alert.setHeaderText("Are you sure you want to update this Player?");
      Optional<ButtonType> option = alert.showAndWait();
      if (option.get() == ButtonType.OK) {
       updatePlayer();
      }  
     }

    @FXML
    private void selectPlayerImage(ActionEvent event) {
         try{
            FileChooser filechooser=new FileChooser();
            selectedFile=filechooser.showOpenDialog(null);
            Image playerImage=new Image(new FileInputStream(selectedFile));
            ivIPlayerProfilePic.setImage(playerImage);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void resetPlayerDetails(ActionEvent event) {
         if(tvPlayerDetails.getSelectionModel().getSelectedItem()==null){
             resetFunction();
         }
         else{
            fillAllPlayerDetails();
         }
    }
    private void resetFunction(){
          txtPlayerName.setText(null);
        dpPlayerDOB.setValue(null);
        cbPlayerTeam.getSelectionModel().clearSelection();
        cbupdatePlayerTeam.getSelectionModel().clearSelection();
// Clear value of ComboBox because clearSelection() does not do it
//cbPlayerTeam.setValue(null);
        rbIsAllRounder.setSelected(false);
        rbIsBatsmen.setSelected(false);
        rbIsBowler.setSelected(false);
        txtTotalMatches.setText(null);
        txtTotalInnings.setText(null);
        txtTotalHundreds.setText(null);
        txtTotalFifties.setText(null);
        txtTotalRuns.setText(null);
        txtTotalNoOfNotOuts.setText(null);
        txtBestScore.setText(null);
        txtTotalNoOfBallsFaced.setText(null);
        txtTotalDucks.setText(null);
        txtTotalRunsGiven.setText(null);
        txtTotalWickets.setText(null);
        txtBestBowlingFigure.setText(null);
        txtTotalBallsBowled.setText(null);
        txtBattingAverage.setText("NA");
        txtBattingStrikeRate.setText("NA");
        txtBowlingAverage.setText("NA");
        txtBowlingEconomy.setText("NA");
        ivIPlayerProfilePic.setImage(null);
       
}
     private void selectdefaultvalue(){
        
            IPLWorldPlayerInfo player = tvPlayerDetails.getSelectionModel().getSelectedItem();
            fillAllPlayerDetails();
}
     
     private void fillAllPlayerDetails(){
        IPLWorldPlayerInfo player = tvPlayerDetails.getSelectionModel().getSelectedItem();
        txtPlayerName.setText(player.getPlayerName());
        dpPlayerDOB.setValue(LocalDate.parse(player.getPlayerDOB()));
        if (player.getPlayerRole().equals("All Rounder")) {
            rbIsAllRounder.setSelected(true);
        } else if (player.getPlayerRole().equals("Batsman")) {
            rbIsBatsmen.setSelected(true);
        } else {
            rbIsBowler.setSelected(true);
        }
         cbupdatePlayerTeam.getSelectionModel().select(cbPlayerTeam.getSelectionModel().getSelectedItem());
        // int playerID=player.getPlayerId();
        fetchesPlayerStats(player.getPlayerId());
    
     }

}