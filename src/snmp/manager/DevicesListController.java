/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class DevicesListController implements Initializable {

    @FXML
    private Label username;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setText(Users.getCurrentUser().username);
        // TODO
    }    
    
    
    @FXML
    private void returnButtonAction(ActionEvent ae){
        System.out.println("devicesList");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("DashBoard.fxml"));
            Scene scene = new Scene(root);
            Stage stage = SNMPManager.mainStage;
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SNMPManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
