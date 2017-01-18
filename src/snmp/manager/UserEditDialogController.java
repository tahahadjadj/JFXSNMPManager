/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import snmp.manager.Groups.Group;
import snmp.manager.Users.User;

/**
 * FXML Controller class
 *
 * @author 
 */
public class UserEditDialogController implements Initializable {
    
    @FXML
    private TextField username;
    @FXML
    private TextField passwd;
    @FXML
    private TextField address;
    @FXML
    private TextField telephone;
    @FXML
    private CheckBox addTo;
    @FXML
    private CheckBox removeFrom;
    @FXML
    private ComboBox<Group> myComboBox;
    private ObservableList<Group> myComboBoxData = FXCollections.observableArrayList();
  
    private User tempUser=new User();
    private boolean okClicked=false;
    private Stage dialogStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           
         for(int i=0; i<Groups.getGroupsList().size(); i++)
        {    
            myComboBoxData.add(Groups.getGroupsList().get(i));
        }
         myComboBox.setItems(myComboBoxData);
        // TODO
    }    
     public void setUser(User user){
        tempUser=user;
        System.out.println(tempUser.getUsername());
        username.setText(tempUser.getUsername());
        address.setText(tempUser.getAddress());
        telephone.setText(tempUser.getTelephone());
        if(tempUser.getUsername()==null){
            removeFrom.setDisable(true);
        }
        
    }
    public boolean okClicked (){
        return okClicked;
    }
     @FXML
    private void handleOk() throws IOException, NoSuchAlgorithmException{
      
        if(tempUser.getUsername()==null){  //new
            
            User user=Users.getUser(username.getText());
            if(user==null){
                if(!(passwd.getText().equals(""))&&(!address.getText().equals(""))&&(!telephone.getText().equals(""))){
                    okClicked=true;        
                    tempUser.setUsername(username.getText());
                    tempUser.setPassword(passwd.getText());
                    tempUser.setAddress(address.getText());
                    tempUser.setTelephone(telephone.getText());
                    Users.addUser(tempUser);
                     if(addTo.isSelected()){
                        Group selectedGroup = myComboBox.getSelectionModel().getSelectedItem();
                        if(selectedGroup!=null){
                            Users.addUserToGroup(tempUser.getUsername(), selectedGroup.getName());
                        }
                    }
                    Users.saveUsersFile();
                    dialogStage.close();
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(SNMPManager.mainStage);//to check later
                    alert.setTitle("Invalid input");
                    alert.setHeaderText("Empty fields");
                    alert.setContentText("Please fill the fields.");

                    alert.showAndWait();
                 }
            
            }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(SNMPManager.mainStage);//to check later
                    alert.setTitle("Invalid input");
                    alert.setHeaderText("username already exists");
                    alert.setContentText("Please enter user name again.");

                    alert.showAndWait();
                 }
            
        }else{   //edit
                 
                 if(!(passwd.getText().equals(""))&&(!address.getText().equals(""))&&(!telephone.getText().equals(""))){
                    okClicked=true;        
                    tempUser.setUsername(username.getText());
                    tempUser.setPassword(passwd.getText());
                    tempUser.setAddress(address.getText());
                    tempUser.setTelephone(telephone.getText());
                    if((addTo.isSelected())&&(removeFrom.isSelected())){
                         Alert alert = new Alert(Alert.AlertType.WARNING);
                         alert.initOwner(SNMPManager.mainStage);//to check later
                         alert.setTitle("Invalid input");
                         alert.setHeaderText("Multiple selection");
                         alert.setContentText("Please select add to or remove from.");

                         alert.showAndWait();
                    }else{
                        if(addTo.isSelected()){
                            Group selectedGroup = myComboBox.getSelectionModel().getSelectedItem();
                            if(selectedGroup!=null){
                               Users.addUserToGroup(tempUser.getUsername(), selectedGroup.getName());
                             }
                        }else{
                            if(removeFrom.isSelected()){
                               Group selectedGroup = myComboBox.getSelectionModel().getSelectedItem();
                               if(selectedGroup!=null){
                               Users.removeUserFromAGroup(tempUser.getUsername(), selectedGroup.getName());
                               }
                            }
                        
                        }
                        Users.saveUsersFile();
                        dialogStage.close();
                    }
                 }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(SNMPManager.mainStage);//to check later
                    alert.setTitle("Invalid input");
                    alert.setHeaderText("Empty fields");
                    alert.setContentText("Please fill the fields.");

                    alert.showAndWait();
                 }
            
        }
        
            
    }
    @FXML
    private void cancelButtonAction(ActionEvent ae){
      dialogStage.close();
    }
    public void setDialogStage(Stage dialogStage){
        this.dialogStage=dialogStage;
    }
}
