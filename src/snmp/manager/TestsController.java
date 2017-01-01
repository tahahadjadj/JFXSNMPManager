/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class TestsController implements Initializable {

    @FXML
    private Label username;
    @FXML
    private TableView DevicesTable;
    @FXML
    private TableColumn statusCol, nameCol, oidCol, ipCol, msgCol, mibCol;
    @FXML
    private ComboBox tests;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        username.setText(Users.getCurrentUser().username);
        
        tests.getItems().addAll("Ping","SNMPGet");
        
        statusCol.setCellValueFactory(
                new PropertyValueFactory<>("status"));
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        msgCol.setCellValueFactory(
                new PropertyValueFactory<>("ping"));
    }
    
    public static class Element {
        private SimpleStringProperty name;
        private SimpleStringProperty IP;
        private SimpleStringProperty status;

        public Element(String name, String IP, String status) {
            this.name = new SimpleStringProperty(name);
            this.IP = new SimpleStringProperty(IP);
            this.status = new SimpleStringProperty(status);
        }

        public String getIP() {
            return IP.get();
        }

        public void setIP(String IP) {
            this.IP = new SimpleStringProperty(IP);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name = new SimpleStringProperty(name);
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status = new SimpleStringProperty(status);
        }
        
        
    }
    
    
    
    //<editor-fold defaultstate="collapsed" desc="code for cell color changement">
    /*
    @FXML
     private void addEntity() {

      data.add(new Inventory(codeTemp.getText(), articleNameTemp.getText(), Integer.parseInt(amountTemp.getText()), dcTemp.isSelected() ? true:false, stTemp.isSelected()?true:false, Utilities.toRGBCode(colorTemp.getValue()), informationTemp.getText(), data.size()+1));
      inventoryTable.setItems(data);

     }
    Callback<TableColumn<Inventory, String>, TableCell<Inventory, String>> cellFactory =
    new Callback<TableColumn<Inventory, String>, TableCell<Inventory, String>>() {
    public TableCell call(TableColumn p) {
    TableCell cell = new TableCell<Person, String>() {
    @Override
    public void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);
    setText(empty ? null : getString());
    setStyle("-fx-background-color:"+getString());
    }
    
    private String getString() {
    return getItem() == null ? "" : getItem().toString();
    }
    };
    
    
    return cell;
    }
    };*/
//</editor-fold>
}
