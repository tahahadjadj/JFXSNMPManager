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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import snmp.manager.Users.User;

/**
 * FXML Controller class
 *
 * @author 
 */
public class UsersManagementController implements Initializable {
    @FXML
    private Label username;
    @FXML
    private Label userN;
    @FXML
    private Label address;
    @FXML
    private Label telephone;
    @FXML
    private Label groups;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn nCol= new TableColumn("#");;
    @FXML
    private TableColumn<User, String> nameCol;
    @FXML
    private TableColumn<User, String> addrCol;
    @FXML
    private TableColumn<User, String> telephCol;
    @FXML
    private TableColumn<User, String> groupCol;
    @FXML
    private ObservableList<User> tableLines = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         username.setText(Users.getCurrentUser().getUsername());
         
         userN.setText("");
         address.setText("");
         telephone.setText("");
         groups.setText("");
         
         nCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
         @Override public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> d) {
         return new ReadOnlyObjectWrapper(usersTable.getItems().indexOf(d.getValue()) + "");
         }
        });   
        nCol.setSortable(false);
        nameCol.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());
        addrCol.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        telephCol.setCellValueFactory(cellData -> cellData.getValue().getTelephoneProperty());
        groupCol.setCellValueFactory(cellData -> cellData.getValue().groupHeBelongsTo());
        usersTable.setItems(tableLines);
          
        for(int i=0; i<Users.getUsersList().size(); i++)
        {    
            tableLines.add(Users.getUsersList().get(i));
        }
        
        // Listen for selection changes and show the user details when changed.
        usersTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showUserDetails(newValue));
    }    
    private void showUserDetails(User user) {
    if (user != null) {
        // Fill the labels with info from the user object.
        usersTable.refresh();
        userN.setText(user.getUsername());
        address.setText(user.getAddress());
        telephone.setText(user.getTelephone());
        groups.setText(user.groupHeBelongsTo.toString());
        
    } else {
        // user is null, remove all the text.
        userN.setText("");
        address.setText("");
        telephone.setText("");
        groups.setText("");
    }
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
    @FXML
    private void handleDeleteUser() {
         int selectedIndex = usersTable.getSelectionModel().getSelectedIndex();
         if (selectedIndex >= 0) {
             User u=usersTable.getItems().get(selectedIndex);
             Users.getUsersList().remove(u);
             Users.saveUsersFile();
             usersTable.getItems().remove(selectedIndex);
         } else {
              Alert alert = new Alert(AlertType.WARNING);
              alert.initOwner(SNMPManager.mainStage);//to check later
              alert.setTitle("No Selection");
              alert.setHeaderText("No user Selected");
              alert.setContentText("Please select a user in the table.");

              alert.showAndWait();
         }
   
    }
    @FXML
    private void handleNewUser() throws IOException{
        User tempUser = new User();
        boolean okClicked = showUserEditDialog(tempUser);
        if (okClicked) {
            tableLines.add(tempUser);
          
        }      
    }
    @FXML
    private void handleEditUser() throws IOException{
         User selectedUser = usersTable.getSelectionModel().getSelectedItem();
         if (selectedUser != null) {
            boolean okClicked = showUserEditDialog(selectedUser);
            if (okClicked) {
               System.out.println(selectedUser.getUsername());
               showUserDetails(selectedUser);
              
            }
         } else {
        // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(SNMPManager.mainStage);
            alert.setTitle("No Selection");
            alert.setHeaderText("No user Selected");
            alert.setContentText("Please select a user in the table.");

            alert.showAndWait();
    }
        
    }
    public boolean showUserEditDialog(User user) throws IOException {
         // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserEditDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit User");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(SNMPManager.mainStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
       
        // Set the user into the controller.
        UserEditDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setUser(user);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
        return controller.okClicked();
     
    }
        
   
}



