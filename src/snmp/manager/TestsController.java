/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

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
    private TableColumn statusCol, nameCol, oidCol, ipCol, testCol, mibCol, rawCol;
    @FXML
    private ComboBox tests;
    @FXML
    private CheckBox addToDevicesList;
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
        rawCol.setCellValueFactory(
                new PropertyValueFactory<>("number"));
        oidCol.setCellValueFactory(
                new PropertyValueFactory<>("OID"));
        devicesTable.setItems(tableLines);
        
        
        Devices.getDevicesList().stream().forEach(device ->{
            tableLines.add(new Element(device.getAlias(), device.getIpAdress(), device.getStatus(), testValue, device));
        });
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
        private SimpleStringProperty number;
        private SimpleStringProperty name;
        private SimpleStringProperty IP;
        private SimpleStringProperty status;
        private SimpleStringProperty OID = new SimpleStringProperty("1.3.6.1.2.1.1.1.0") ;
        private SimpleStringProperty TestResult;
        private SimpleStringProperty MIB;
        private Device device;
        private boolean registered = false;

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
            this.number = new SimpleStringProperty(String.valueOf(tableLines.size()+1));
        }
        public Element(String name, String IP, String status, String testType, Device device) {
            this.name = new SimpleStringProperty(name);
            this.IP = new SimpleStringProperty(IP);
            this.status = new SimpleStringProperty(status);
            this.TestResult = new SimpleStringProperty(testType);
            this.number = new SimpleStringProperty(String.valueOf(tableLines.size()+1));
            this.device = device;
            registered = true;
        }

        public boolean isRegistered() {
            return registered;
        }

        public void setDevice(Device device) {
            this.device = device;
            registered = true;
        }

        public Device getDevice() {
            return device;
        }
        
        public String getIP() {
            return IP.get();
        }

        public void setIP(String IP) {
            this.IP = new SimpleStringProperty(IP);
        }
        
        public String getNumber() {
            return number.get();
        }

        public void setNumber(String nb) {
            this.number = new SimpleStringProperty(nb);
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

        public String getOID() {
            return OID.get();
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
            if (addToDevicesList.isSelected())
                tableLines.add(new Element(aliasField.getText(), ipField.getText(), "N/A", testValue,Devices.addDevice(ipField.getText(),aliasField.getText())));
            else
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
                if(line.isRegistered())
                    if(p.isReplied())line.device.setStatus("online");else line.device.setStatus("offline");
                line.setTestResult(p.getAnswer());
                System.out.println("end ping "+line.getIP()+" result "+p.isReplied()+ " answer "+p.getAnswer());
                devicesTable.refresh();
            });
        }else{
            //snmpGet test comming soon
            snmpGet("1.3.6.1.2.1.1.1.0");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("erreur");
//            alert.setHeaderText(null);
//            alert.setContentText("SNMP Get not test not implemented yet");
//
//            alert.showAndWait();
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
    
    
    public void snmpGet(String oidValue){
        String port = "161";
        int snmpVersion = SnmpConstants.version2c;
        String community="public";
        
        
            tableLines.parallelStream().forEach((Element line) -> {
                try {
                    line.setOID(oidValue);
                    // Create Target Address object
                    CommunityTarget comtarget = new CommunityTarget();
                    comtarget.setCommunity(new OctetString(community));
                    comtarget.setVersion(snmpVersion);
                    comtarget.setAddress(new UdpAddress(line.getIP() + "/" + port));
                    comtarget.setRetries(2);
                    comtarget.setTimeout(1000);

                    // Create the PDU object
                    PDU pdu = new PDU();
                    pdu.add(new VariableBinding(new OID(oidValue)));
                    pdu.setType(PDU.GET);
                    pdu.setRequestID(new Integer32(1));
                    
                    
                    // Create Snmp object for sending data to Agent
                    Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
                    snmp.listen();
                    System.out.println("Sending Request to Agent...");
                    ResponseEvent response = snmp.send(pdu, comtarget);

                    // Process Agent Response
                    if (response.getResponse() != null){

                        System.out.println("Got Response from Agent");
                        PDU responsePDU = response.getResponse();
                        if (responsePDU != null){

                            int errorStatus = responsePDU.getErrorStatus();
                            int errorIndex = responsePDU.getErrorIndex();
                            String errorStatusText = responsePDU.getErrorStatusText();

                            if (errorStatus == PDU.noError){
                                line.setTestResult(responsePDU.getVariableBindings().toString().substring(responsePDU.getVariableBindings().toString().indexOf("=")+1));
                                System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
                                line.setStatus("online");
                            }
                            else{
                                line.setTestResult("Error: Request Failed \n"+"Error Status = " + errorStatus+"\n"+"Error Index = " + errorIndex+"\n"+"Error Status Text = " + errorStatusText);
                                System.out.println("Error: Request Failed" );
                                System.out.println("Error Status = " + errorStatus);
                                System.out.println("Error Index = " + errorIndex);
                                System.out.println("Error Status Text = " + errorStatusText);
                                line.setStatus("warning"); 
                                if(line.isRegistered())
                                    if(!line.device.getStatus().matches("offline"))
                                        line.device.setStatus("warning");
                            }
                        }
                        else{
                            line.setTestResult("Error: Response PDU is null");
                            System.out.println("Error: Response PDU is null");
                            line.setStatus("warning"); 
                            if(line.isRegistered())
                                if(!line.device.getStatus().matches("offline"))
                                    line.device.setStatus("warning");
                        }
                    }
                    else{
                        line.setTestResult("Error: Agent Timeout... ");
                        System.out.println("Error: Agent Timeout... ");
                        line.setStatus("warning"); 
                        if(line.isRegistered())
                            if(!line.device.getStatus().matches("offline"))
                              line.device.setStatus("warning");
                    }
                    snmp.close();
                    
                } catch (IOException ex) {
                    Logger.getLogger(TestsController.class.getName()).log(Level.SEVERE, null, ex);
                }
        });
        devicesTable.refresh();
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
