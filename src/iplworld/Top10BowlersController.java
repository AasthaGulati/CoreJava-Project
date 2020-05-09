
package iplworld;

import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class Top10BowlersController implements Initializable {

    @FXML
    private TableView<Top10Bowlers> tvTop10Bowlers;
    @FXML
    private TableColumn<Top10Bowlers, Integer> tcPlayerId;
    @FXML
    private TableColumn<Top10Bowlers, Integer> tcPlayerRank;
    @FXML
    private TableColumn<Top10Bowlers, String> tcPlayerName;
    @FXML
    private TableColumn<Top10Bowlers, Float> tcPlayerBowlingEconomy;
    @FXML
    private TableColumn<Top10Bowlers, Float> tcPlayerBowlingAverage;
    @FXML
    private ImageView ivPlayerProfilePic;
    @FXML
    private Label lblPlayerName;
    @FXML
    private Label lblBestBowlingFigure;
    @FXML
    private Label lblTotalWicketsTaken;
    @FXML
    private Label lblPlayerTeam;

     ObservableList<Top10Bowlers> mTop10Bowlers;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         mTop10Bowlers = FXCollections.observableArrayList();
         tvTop10Bowlers.setItems(mTop10Bowlers);
         tcPlayerRank.setCellValueFactory(new PropertyValueFactory<>("Rank"));
         tcPlayerName.setCellValueFactory(new PropertyValueFactory<>("PlayerName"));
         tcPlayerId.setCellValueFactory(new PropertyValueFactory<>("PlayerId"));
         tcPlayerBowlingEconomy.setCellValueFactory(new PropertyValueFactory<>("BowlingEconomy"));
         tcPlayerBowlingAverage.setCellValueFactory(new PropertyValueFactory<>("BowlingAverage"));
         fillTop10BowlersTable();
        
    }    
    
    private Connection makeMySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3307/IPLWorld", "root", "Pass@123");
        } catch (Exception ex) {
            return null;
        }
    }
    
    private void fillTop10BowlersTable(){
         try {
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psPlayerDetails = conIPLWorld.prepareStatement("SELECT *,Row_number() over (order by BowlingAverage asc) as PlayerRank from (select \n" +
            "player.PlayerId,PlayerName,(TotalRunsGiven/TotalWickets)AS BowlingAverage,\n" +
            "(TotalRunsGiven*6/ TotalBallsBowled)AS BowlingEconomy from player inner join PlayerStats on \n" +
            "player.PlayerId=PlayerStats.PlayerId and player.IsActive=1 where TotalWickets>10) AS P Where BowlingAverage!=0 order by BowlingAverage asc ,PlayerRank LIMIT 10;  ");
                ResultSet rsPlayerDetails = psPlayerDetails.executeQuery();
            while (rsPlayerDetails.next()) {
                // Top10Batsmen top10Batsmen = new Top10Batsmen(rsPlayerDetails.getInt("PlayerRank"),rsPlayerDetails.getInt("PlayerId"), rsPlayerDetails.getString("PlayerName"),rsPlayerDetails.getInt("BattingStrikeRate"),rsPlayerDetails.getInt("BattingAverage"));
                //Alternative way of doing this and preferable way when parameters are large.
                Top10Bowlers top10Bowlers=new Top10Bowlers();
                top10Bowlers.setvalues(rsPlayerDetails);
                mTop10Bowlers.add(top10Bowlers);
            }
            tvTop10Bowlers.getSelectionModel().selectFirst();
            selectdefaultvalue(); //will set first as default value

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
      private void showPlayerData(int playerId){
        try {
            Connection conIPLWorld = makeMySqlConnection();
            PreparedStatement psPlayerDetails = conIPLWorld.prepareStatement(" select PlayerName,BestBowlingFigure,TotalWickets,TeamName,player.Image from player\n" +
            "inner join PlayerStats on player.PlayerId=PlayerStats.PlayerId inner join lkpPlayerTeam\n" +
            "on player.TeamId=lkpPlayerTeam.TeamId where Player.PlayerId="+playerId);
          //  psPlayerDetails.setInt(1,playerId);
            ResultSet rsPlayerDetails=psPlayerDetails.executeQuery();
            if(rsPlayerDetails.next())
            {
              lblPlayerName.setText(rsPlayerDetails.getString("PlayerName"));
              lblBestBowlingFigure.setText(rsPlayerDetails.getString("BestBowlingFigure"));
              lblTotalWicketsTaken.setText(rsPlayerDetails.getString("TotalWickets"));
              lblPlayerTeam.setText(rsPlayerDetails.getString("TeamName"));
            
            Blob playerProfilePic = rsPlayerDetails.getBlob("Image");
                InputStream inputStream = playerProfilePic.getBinaryStream();
                Image playerPic = new Image(inputStream) ; 
                ivPlayerProfilePic.setImage(playerPic);
                inputStream.close();
            }
           
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
      
       private void selectdefaultvalue(){
        
            Top10Bowlers player = tvTop10Bowlers.getSelectionModel().getSelectedItem();
            showPlayerData(player.playerId);
}
  
    
    @FXML
    private void exitFunction(ActionEvent event) {
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
    private void mouseRelease(MouseEvent event) {
        selectdefaultvalue();
    }
    }
    

