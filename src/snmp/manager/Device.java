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
import snmp.manager.Groups.Group;


public class Device implements Serializable{
    
    String ipAdress;
    String alias;
    private Group group;
    public String status = "offline";
    
    Map<String, String> oids; //oids values
     public static final Map<String, String> ScanOIDs = new HashMap<String, String>(); 
    static { 
    // some general OIDs that are valid in almost every MIB 
    ScanOIDs.put("Device name: ", "1.3.6.1.2.1.1.5.0"); 
    ScanOIDs.put("Description: ", "1.3.6.1.2.1.1.1.0"); 
    ScanOIDs.put("Location: ", "1.3.6.1.2.1.1.6.0"); 
     };
    
    public Device(String ip)
    {
        ipAdress=ip;
        oids = new HashMap<String, String>();
    }
     public Device(String ip, String alias)
    {
        ipAdress=ip;
        this.alias=alias;
        oids = new HashMap<String, String>();
    }
      public Device(String ip, String alias, String groupName)
    {
        ipAdress=ip;
        this.alias=alias;
        oids = new HashMap<String, String>();
        group=new Group(groupName);
    }
      public void setGroup(String groupName)
      {
          group=new Group(groupName);
      }
       public String getGroup()
      {
          return group.getName();
      }
    
    
    
    public void printDeviceInformations()
    {
        for (Map.Entry entry : oids.entrySet()) {
                    System.out.println(entry.getKey() + ", " + entry.getValue());
       } 
    }
    
}
