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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static snmp.manager.Devices.importDevices;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class DashBoardController implements Initializable {

    @FXML
    private Label devicesNb, usersNb, lastUser, username;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        importDevices();
        devicesNb.setText(Integer.toString(Devices.getDevicesCount()));
        usersNb.setText(Integer.toString(Users.getUsersCount()));
        lastUser.setText(Users.getLastUser());
        username.setText(Users.getCurrentUser().getUsername());
    }    
    
    @FXML
    private void testsButtonAction(ActionEvent ae){
        System.out.println("checking user");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Tests.fxml"));
            Scene scene = new Scene(root);
            Stage stage = SNMPManager.mainStage;
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SNMPManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    private void devicessButtonAction(ActionEvent ae){
        System.out.println("devicesList");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("DevicesList.fxml"));
            Scene scene = new Scene(root);
            Stage stage = SNMPManager.mainStage;
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SNMPManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     @FXML
    private void usersButtonAction(ActionEvent ae){
        System.out.println("usersList");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UsersManagement.fxml"));
            Scene scene = new Scene(root);
            Stage stage = SNMPManager.mainStage;
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SNMPManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
