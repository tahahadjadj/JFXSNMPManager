/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class SNMPManager extends Application {
    
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("SNMPManager.fxml"));
        
        Scene scene = new Scene(root);
        mainStage = stage;
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    //
    public static void main(String[] args) {
        launch(args);
    }
    
}
