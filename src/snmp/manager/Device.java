/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import snmp.manager.Groups.Group;


public class Device implements Serializable{
    
     private String ipAddress;
     private String alias;
     private String status = "offline";
     private Group group;
     //not serializable
//     private transient SimpleStringProperty ipAddressProperty;
//     private transient SimpleStringProperty aliasProperty;
//     private transient SimpleStringProperty statusProperty = new SimpleStringProperty("offline");
    
    Map<String, String> oids= new HashMap<String, String>(); //oids values
     public static final Map<String, String> ScanOIDs = new HashMap<String, String>(); 
    static { 
    // some general OIDs that are valid in almost every MIB 
    ScanOIDs.put("Device name: ", "1.3.6.1.2.1.1.5.0"); 
    ScanOIDs.put("Description: ", "1.3.6.1.2.1.1.1.0"); 
    ScanOIDs.put("Location: ", "1.3.6.1.2.1.1.6.0"); 
     };
    
    //constructors
    public Device(String ip)
    {
        ipAddress=ip;
//        ipAddressProperty=new SimpleStringProperty(ip);
    }
     public Device(String ip, String alias)
    {
        ipAddress=ip;
//        ipAddressProperty=new SimpleStringProperty(ip);
        this.alias=alias;
//        this.aliasProperty=new SimpleStringProperty(alias);
    }
      public Device(String ip, String alias, String groupName)
    {
        this.ipAddress=ip;
//        ipAddressProperty=new SimpleStringProperty(ip);
        this.alias=alias;
//        this.aliasProperty=new SimpleStringProperty(alias);
        group=new Group(groupName);
    }
      //getters and setters
      public void setGroup(String groupName)
      {
          group=new Group(groupName);
      }
       public String getGroup()
      {
          return group.getName();
      }
       public StringProperty getGroupProperty()
      {
          return group.getNameProperty();
      }
      /*  public StringProperty getGroupProperty()
      {
          return group.getNameProperty();
      }*/
       public String getIpAdress(){
           return ipAddress;
       }
//        public StringProperty getIpAdressProperty(){
//           return ipAddressProperty;
//       }
       public void setIpAdress(String ip){
           ipAddress=ip;
//           this.ipAddressProperty.set(ip);
       }
       public String getAlias(){
           return alias;
       }
//       public StringProperty getAliasProperty(){
//           return aliasProperty;
//       }
       public void setAlias(String alias){
           this.alias=alias;
//           this.aliasProperty.set(alias);
       }
       public String getStatus(){
           return status;
       }
//       public StringProperty getStatusProperty(){
//           return statusProperty;
//       }
       public void setStatus(String status){
           this.status=status;
//           this.statusProperty.set(status);
       }
    
    
    
    public void printDeviceInformations()
    {
        for (Map.Entry entry : oids.entrySet()) {
                    System.out.println(entry.getKey() + ", " + entry.getValue());
       } 
    }
    
}
