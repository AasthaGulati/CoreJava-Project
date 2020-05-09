/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iplworld;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLTeamController implements Initializable {

    @FXML
    private JFXComboBox<String> cbTeamName;
    @FXML
    private JFXTextField txtTrophyCount;
    ObservableList<String> mPlayerTeam;
    @FXML
    private TextField txtUpdatedTeamName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mPlayerTeam = FXCollections.observableArrayList();
        fillPlayerTeam();
        fillTrophyCount();
       
        
    } 
     private Connection makeMySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3307/IPLWorld", "root", "Pass@123");
        } catch (Exception ex) {
            return null;
        }
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
            cbTeamName.setItems(mPlayerTeam);
          
            conIPLWorld.close();
        
          
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void fillTrophyCount(){
        try {
          //  int TeamId=cbTeamName.getSelectionModel().getSelectedIndex()+1;
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psfetchTrophyCount = conIPLWorld.prepareStatement("Select TrophyCount,TeamName from lkpPlayerTeam where TeamId=?");
            psfetchTrophyCount.setInt(1,cbTeamName.getSelectionModel().getSelectedIndex()+1 );
            ResultSet rsfetchTrophyCount = psfetchTrophyCount.executeQuery();
            if(rsfetchTrophyCount.next()){
            txtTrophyCount.setText(rsfetchTrophyCount.getString("TrophyCount"));
            txtUpdatedTeamName.setText(rsfetchTrophyCount.getString("TeamName"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            
        }
    }

        
         
         private void updateTeamDetails(){
         try {
             int TeamId=cbTeamName.getSelectionModel().getSelectedIndex()+1;
            //Make Connection to Database
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psUpdateTeamDetails = conIPLWorld.prepareStatement("Update lkpPlayerTeam set TrophyCount=?,TeamName=? where TeamId=?");
            psUpdateTeamDetails.setString(1, txtTrophyCount.getText());
            psUpdateTeamDetails.setString(2, txtUpdatedTeamName.getText());
            psUpdateTeamDetails.setInt(3, TeamId);
            psUpdateTeamDetails.executeUpdate();
            fillPlayerTeam();
            resetFunction();
         }catch(Exception ex){
             System.out.println(ex.getMessage());
         }
        
    }

    @FXML
    private void fillTrophyCounts(ActionEvent event) {
        fillTrophyCount();
    }
    
   
    
      private void updateConfirmation() {
 
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Update Team Details");
      alert.setHeaderText("Are you sure you want to update?");
      Optional<ButtonType> option = alert.showAndWait();
      if (option.get() == ButtonType.OK) {
          
          updateTeamDetails();
          
      }  
     }

      private void resetFunction(){
          txtTrophyCount.setText(null);
          txtUpdatedTeamName.setText(null);
          cbTeamName.getSelectionModel().clearSelection();
      
      }
    @FXML
    private void exitfunction(ActionEvent event) {
        System.exit(0);
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
    private void updateTeamDetail(ActionEvent event) {
         updateConfirmation();
    }

    
}
