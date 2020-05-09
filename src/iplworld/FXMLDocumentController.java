
package iplworld;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class FXMLDocumentController implements Initializable {
    File selectedFile=null;
    @FXML
    private JFXComboBox<String> cbPlayerTeam;
    @FXML
    private JFXTextField txtPlayerName;
    @FXML
    private JFXTextField txtTotalMatches;
    @FXML
    private DatePicker dpPlayerDOB;
    @FXML
    private JFXRadioButton rbIsAllRounder;
    @FXML
    private JFXRadioButton rbIsBatsmen;
    @FXML
    private JFXRadioButton rbIsBowler;
    @FXML
    private JFXTextField txtTotalInnings;
    @FXML
    private JFXTextField txtTotalRuns;
    @FXML
    private JFXTextField txtTotalWickets;
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
    private ImageView ivPlayerProfilePic;
    @FXML
    private JFXTextField txtTotalNoOfNotOuts;
    @FXML
    private JFXTextField txtTotalBallsBowled;
    @FXML
    private JFXTextField txtTotalRunsGiven;
    ObservableList<String> mPlayerTeam;
    @FXML
    private ToggleGroup tg;
    @FXML
    private JFXTextField txtTotalNoOfBallsFaced;
    @FXML
    private AnchorPane rfvtxtTotalMatchesPlayed;
    @FXML
    private RequiredFieldValidator rfvtxtTotalMatches;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillPlayerTeam();
    }
    public void bindTeamRole(Stage s) {
        /*  RadioButton rb=new RadioButton("Team1");
        
        HBox hbox=new Hbox(rb);
        Scene scene=new Scene(hbox,128.0,179.0);
        primaryStage.setScene(Scene);
        primaryStage.show();
         */

    }
    //Fills Select Team Comobo Box
    private void fillPlayerTeam() {
        try {
            //Make Connection to Database
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psAllTeams = conIPLWorld.prepareStatement(Constants.QueryTOLKPPLayerTeam);
            ResultSet rsAllTeams = psAllTeams.executeQuery();
            mPlayerTeam = FXCollections.observableArrayList();
            //fetch teams from datbase and add into combobox
            while (rsAllTeams.next()) {
                mPlayerTeam.add(rsAllTeams.getInt("TeamId") - 1, rsAllTeams.getString("TeamName"));
            }
            cbPlayerTeam.setItems(mPlayerTeam);
            conIPLWorld.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    //Creates MySql connection
    private Connection makeMySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(Constants.URLToOpenMYSQLDatabase, "root", "Pass@123");
        } catch (Exception ex) {
            return null;
        }
    }

    @FXML
    private void exitfunction(ActionEvent event) {
        System.exit(0);
    }
    @FXML
    //enter data into player table
    private void savePlayerDetails(ActionEvent event) {
        try {
            Connection conIPLWorld = makeMySqlConnection();
            String savePlayerDataQuery = "insert into player(PlayerName,DateOfBirth,PlayerRoleId,TeamId,IsActive,LastUpdated,image)" + "values(?,?,?,?,?,?,?)";
            PreparedStatement psSavePlayerData = conIPLWorld.prepareStatement(savePlayerDataQuery, Statement.RETURN_GENERATED_KEYS);
            psSavePlayerData.setString(1, txtPlayerName.getText());
            psSavePlayerData.setString(2, dpPlayerDOB.getValue().toString());
            if (rbIsAllRounder.isSelected()) {
                psSavePlayerData.setString(3, "1");
            } else if (rbIsBatsmen.isSelected()) {
                psSavePlayerData.setString(3, "2");
            } else {
                psSavePlayerData.setString(3, "3");
            }
            int playerTeamId = cbPlayerTeam.getSelectionModel().getSelectedIndex() + 1;
            psSavePlayerData.setInt(4, playerTeamId);
            psSavePlayerData.setString(5, "1");
            psSavePlayerData.setString(6, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
          
              if(selectedFile!=null){
                FileInputStream fin = new FileInputStream(selectedFile);
                psSavePlayerData.setBinaryStream(7, fin, (int)selectedFile.length());
            }
            else{
              psSavePlayerData.setString(7,"");
            }
            psSavePlayerData.executeUpdate();
             
            ResultSet rsPlayerData = psSavePlayerData.getGeneratedKeys();
            if (rsPlayerData.next()) {
                int playerId = rsPlayerData.getInt(1);
                //enters data into PlayerTeamMapping   
                savePlayerTeamMapping(conIPLWorld, playerId, playerTeamId);
                //enters data into table PlayerStats
                savePlayerStats(conIPLWorld, playerId);
                
            }
        } catch (Exception ex) {
         showError(ex);
//    System.out.println(ex.getStackTrace());
        }
    }
    //enters data into PlayerTeamMapping table 
    private void savePlayerTeamMapping(Connection conIPLWorld, int playerId, int playerTeamId) {
        try {
            String savePlayerTeamDataQuery = "insert into PlayerTeamMapping(PlayerId,TeamId,IsActive,CreatedOn,LastUpdated)" + "values(?,?,?,?,?)";
            PreparedStatement psSavePlayerTeamData = conIPLWorld.prepareStatement(savePlayerTeamDataQuery);
            psSavePlayerTeamData.setInt(1, playerId);
            psSavePlayerTeamData.setInt(2, playerTeamId);
            psSavePlayerTeamData.setString(3, "1");
            psSavePlayerTeamData.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            psSavePlayerTeamData.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            psSavePlayerTeamData.executeUpdate();
            
        } catch (Exception ex) {
         showError(ex);   
        // System.out.println(ex.getMessage());
        }
    }
    //enters data into table PlayerStats
    private void savePlayerStats(Connection conIPLWorld, int playerId) {
        try {
            String savePlayerStatsDataQuery = "insert into PlayerStats(PlayerId,TotalMatches,TotalInnings,TotalRuns,TotalWickets,TotalBallsBowled,TotalRunsGiven,TotalNoOfNotOuts,TotalHundreds,TotalFifties,BestScore,BestBowlingFigure,TotalDucks,IsActive,LastUpdated,TotalNoOfBallsFaced )" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement psSavePlayerStatsData = conIPLWorld.prepareStatement(savePlayerStatsDataQuery);
            psSavePlayerStatsData.setInt(1, playerId);
            psSavePlayerStatsData.setString(2, txtTotalMatches.getText());
            psSavePlayerStatsData.setString(3, txtTotalInnings.getText());
            psSavePlayerStatsData.setString(4, txtTotalRuns.getText());
            psSavePlayerStatsData.setString(5, txtTotalWickets.getText());
            psSavePlayerStatsData.setString(6, txtTotalBallsBowled.getText());
            psSavePlayerStatsData.setString(7, txtTotalRunsGiven.getText());
            psSavePlayerStatsData.setString(8, txtTotalNoOfNotOuts.getText());
            psSavePlayerStatsData.setString(9, txtTotalHundreds.getText());
            psSavePlayerStatsData.setString(10, txtTotalFifties.getText());
            psSavePlayerStatsData.setString(11, txtBestScore.getText());
            psSavePlayerStatsData.setString(12, txtBestBowlingFigure.getText());
            psSavePlayerStatsData.setString(13, txtTotalDucks.getText());
            psSavePlayerStatsData.setString(14, "1");
            psSavePlayerStatsData.setString(15, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            psSavePlayerStatsData.setString(16, txtTotalNoOfBallsFaced.getText());
            int count=psSavePlayerStatsData.executeUpdate();
            if(count>0)
            showMessage("Saved");
        txtPlayerName.setText(null);
        dpPlayerDOB.setValue(null);
        cbPlayerTeam.getSelectionModel().clearSelection();
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
        ivPlayerProfilePic.setImage(null);
       
                
        } catch (Exception ex) {
            showError(ex);
           // System.out.println(ex.getMessage());
        }

    }



    @FXML
    private void selectPlayerImage(ActionEvent event) {
        try{
            FileChooser filechooser=new FileChooser();
            selectedFile=filechooser.showOpenDialog(null);
            Image playerImage=new Image(new FileInputStream(selectedFile));
            ivPlayerProfilePic.setImage(playerImage);
        }
        catch(Exception ex){
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
    private void resetPlayerDetails(ActionEvent event) {
        txtPlayerName.setText(null);
        dpPlayerDOB.setValue(null);
        cbPlayerTeam.getSelectionModel().clearSelection();
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
        ivPlayerProfilePic.setImage(null);
            
          }
    
    
      private void showMessage(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("IPL World");
        a.setContentText(msg);
        a.show();

    }

    private void showError(Exception e) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("IPL World");
        a.setContentText(e.toString());
        a.show();

    
    }

    
   
    
}
    