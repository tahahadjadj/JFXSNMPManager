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

/**
 * FXML Controller class
 *
 * @author HP
 */
public class SNMPManagerController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;
    
    
    @FXML
    private void loginButtonAction(ActionEvent ae){
        System.out.println("checking user");
        if(Users.checkUser(username.getText(), password.getText())){
            try {
                message.setText("access granted");
                Parent root = FXMLLoader.load(getClass().getResource("DashBoard.fxml"));
                Scene scene = new Scene(root);
                Stage stage = SNMPManager.mainStage;
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(SNMPManagerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
            message.setText("access denied");
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Users.importUsers();
    }    
    
}
