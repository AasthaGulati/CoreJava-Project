/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iplworld;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ramit
 */
public class FXMLTeamsBarChartController implements Initializable {

    @FXML
    private NumberAxis trophyCountAxis;
    @FXML
    private CategoryAxis teamAxis;
    @FXML
    private BarChart<ResultSet, ResultSet> teamsBarChart;

    /**
     * Initializes the controller class.
     */
    
     private Connection makeMySqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3307/IPLWorld", "root", "Pass@123");
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
        Connection conIPLWorld = makeMySqlConnection();
        PreparedStatement psTeamsBarChart = conIPLWorld.prepareStatement("select TeamName,TrophyCount from lkpPlayerTeam where IsActive=1");
        ResultSet rsTeamsBarChart =psTeamsBarChart.executeQuery();
        XYChart.Series barChart = new XYChart.Series();//different series or bars bnane k liye h and series k liye barchart use kra h
        barChart.setName("upto 2018");  //series ka name set kra h
        while(rsTeamsBarChart.next())
        barChart.getData().add(new XYChart.Data(rsTeamsBarChart.getString(1), rsTeamsBarChart.getInt(2)));
        teamsBarChart.getData().add(barChart);
           
        } catch(Exception ex){   
            System.out.println(ex.getMessage());
    }
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
}
