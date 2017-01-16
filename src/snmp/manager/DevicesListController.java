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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<Device> devicesTable;
    @FXML
    private TableColumn nCol= new TableColumn("#");;
    @FXML
    private TableColumn<Device, String> deviceCol;
    @FXML
    private TableColumn<Device, String> ipCol;
    @FXML
    private TableColumn<Device, String> groupCol;
    @FXML
    private TableColumn<Device, String> statusCol;
    @FXML
    private ObservableList<Device> tableLines = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setText(Users.getCurrentUser().username);
        
        nCol.setCellValueFactory(new Callback<CellDataFeatures<Device, String>, ObservableValue<String>>() {
         @Override public ObservableValue<String> call(CellDataFeatures<Device, String> d) {
         return new ReadOnlyObjectWrapper(devicesTable.getItems().indexOf(d.getValue()) + "");
         }
        });   
        nCol.setSortable(false);
        deviceCol.setCellValueFactory(cellData -> cellData.getValue().getAliasProperty());
        ipCol.setCellValueFactory(cellData -> cellData.getValue().getIpAdressProperty());
        statusCol.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
        groupCol.setCellValueFactory(cellData -> cellData.getValue().getGroupProperty());
        devicesTable.setItems(tableLines);
        
    
        System.out.println(Users.getCurrentUser().groupHeBelongsTo.size());
        
        for(int i=0; i<Users.getCurrentUser().groupHeBelongsTo.size(); i++)
        {
            tableLines.add(new Device("test","192.168.1.3","g2"));
            String group=Users.getCurrentUser().groupHeBelongsTo.get(i).getName();
            ArrayList<Device> devices=Devices.getGroupDevices(group);
            devices.stream().forEach((Device device) ->{
            tableLines.add(new Device(device.getIpAdress(), device.getAlias(),group));
            
        });
        }
       
       
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
