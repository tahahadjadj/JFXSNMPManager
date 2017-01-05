/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableView<Element> devicesTable;
    @FXML
    private TableColumn statusCol, nameCol, oidCol, ipCol, testCol, mibCol;
    @FXML
    private ComboBox tests;
    @FXML
    private TextField ipField, aliasField;
    @FXML
    private ObservableList<Element> tableLines = FXCollections.observableArrayList();
    private String testValue = "Ping";
    private boolean noStop = true;
    
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
        testCol.setCellValueFactory(
                new PropertyValueFactory<>("ping"));
        ipCol.setCellValueFactory(
                new PropertyValueFactory<>("IP"));
        testCol.setCellValueFactory(
                new PropertyValueFactory<>("TestResult"));
        devicesTable.setItems(tableLines);
        
        tableLines.add(new Element("ubuntu server", "192.168.56.104", "N/A", testValue));
        tableLines.add(new Element("cisco router", "192.168.56.105", "N/A", testValue));
        tableLines.add(new Element("host", "192.168.56.102", "N/A", testValue));
        tableLines.add(new Element("unknown", "192.168.56.103", "N/A", testValue));
        tableLines.add(new Element("not assigned", "192.168.56.106", "N/A", testValue));
        
        tests.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                testValue=ov.getValue().toString();
                System.out.println(ov.getValue().toString());
            }
        });
    }
    
    public class Element {
        private SimpleStringProperty name;
        private SimpleStringProperty IP;
        private SimpleStringProperty status;
        private SimpleStringProperty OID;
        private SimpleStringProperty TestResult;
        private SimpleStringProperty MIB;

        public Element(String name, String IP, String status) {
            this.name = new SimpleStringProperty(name);
            this.IP = new SimpleStringProperty(IP);
            this.status = new SimpleStringProperty(status);
        }
        public Element(String name, String IP, String status, String testType) {
            this.name = new SimpleStringProperty(name);
            this.IP = new SimpleStringProperty(IP);
            this.status = new SimpleStringProperty(status);
            this.TestResult = new SimpleStringProperty(testType);
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

        public SimpleStringProperty getMIB() {
            return MIB;
        }

        public SimpleStringProperty getOID() {
            return OID;
        }

        public String getTestResult() {
            return TestResult.get();
        }

        public void setMIB(String MIB) {
            this.MIB = new SimpleStringProperty(MIB);
        }

        public void setOID(String OID) {
            this.OID = new SimpleStringProperty(OID);
        }

        public void setTestResult(String TestResult) {
            this.TestResult = new SimpleStringProperty(TestResult);
        }
        
        
        
        
    }
    
    @FXML
    private void addDeviceButtonAction(ActionEvent ae){
        if(testValue!=""){
            System.out.println("adding : "+aliasField.getText()+" "+ipField.getText()+" "+testValue);
            tableLines.add(new Element(aliasField.getText(), ipField.getText(), "N/A", testValue));
        }
    }
    @FXML
    private void runTestButtonAction(ActionEvent ae){
        if(testValue.matches("Ping")){
            noStop = true;
            tableLines.parallelStream().forEach(line -> {
                PingIP p = new PingIP(line.getIP());
                System.out.println("start ping "+line.getIP());
                p.run();
                line.setStatus((p.isReplied())?"online":"offline");
                line.setTestResult(p.getAnswer());
                System.out.println("end ping "+line.getIP()+" result "+p.isReplied()+ " answer "+p.getAnswer());
                devicesTable.refresh();
            });
        }else{
            //snmpGet test comming soon
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("erreur");
            alert.setHeaderText(null);
            alert.setContentText("SNMP Get not test not implemented yet");

            alert.showAndWait();
        }
    }
    @FXML
    private void stopTestButtonAction(ActionEvent ae){
        
    }
    @FXML
    private void clrTestButtonAction(ActionEvent ae){
        tableLines.clear();
        devicesTable.refresh();
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
