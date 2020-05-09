
package iplworld;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;


public class IPLWorld extends Application {
      static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage=stage;
        try{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
        Scene scene = new Scene(root);
        /*RadioButton r1=new RadioButton("All Rounder");
        RadioButton r2=new RadioButton("Batsmen");
        RadioButton r3=new RadioButton("Bowler");
         TilePane r=new TilePane();
         r.getChildren().add(r1);
         r.getChildren().add(r2);
         r.getChildren().add(r3);*/
        stage.setScene(scene);
        stage.show();
        }
        catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
