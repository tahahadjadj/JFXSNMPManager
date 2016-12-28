/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.util.HashMap;
import java.util.Map;


public class Device {
    
    String ipAdress;
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
    
   
    
}
