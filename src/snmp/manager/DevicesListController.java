/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class DevicesListController implements Initializable {

    @FXML
    private Label username;
    @FXML
    private FlowPane devicesHolder;
    
    
    public Button b = new Button();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setText(Users.getCurrentUser().getUsername());
        Devices.getDevicesList().stream().forEach(device -> {
            DeviceIcon deviceIcon = new DeviceIcon(device);
            devicesHolder.getChildren().add(deviceIcon);
        });
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
    
    private class DeviceIcon extends Button{
        private Device device;
        public DeviceIcon(Device device) {
            super(device.getAlias());
            this.device = device;
            this.setText(device.getAlias()+"\n"+device.getIpAdress()+"\n"+device.getStatus());
        }
        
    }
    
    @FXML
    private void discoveryAction(ActionEvent ae){
        
    }
}
