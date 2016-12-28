/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;
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


public class Network {
    
   String subnet="192.168.1"; 
   private ArrayList<Device> Devices = new ArrayList<Device>(); //contains the network devices
   
   public void setSubnet(String subnet)
   {
       this.subnet=subnet;
   }
   
   public void checkHostsPing() throws UnknownHostException, IOException{ //discover hosts using ping
   int timeout=1000;
   for (int i=1;i<255;i++){
       String host=subnet + "." + i;
       if (InetAddress.getByName(host).isReachable(timeout)){
           System.out.println(host + " is reachable");
           Devices.add(new Device(host)); //add reachable devices
           
       }
    }
   }
   public void checkHostsSnmp() throws UnknownHostException, IOException{ //discover hosts using ping
   int timeout=1000;
   
   for (int i=1;i<7;i++){
       
       String host=subnet + "." + i;
       String port = "161";
// OID of MIB RFC 1213; Scalar Object = .iso.org.dod.internet.mgmt.mib-2.system.sysDescr.0
       String oidValue = "1.3.6.1.2.1.1.5.0"; // ends with 0 for scalar object
       int snmpVersion = SnmpConstants.version1;
       String community="public";
       
        // Create Target Address object
        CommunityTarget comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString(community));
        comtarget.setVersion(snmpVersion);
        comtarget.setAddress(new UdpAddress(host + "/" + port));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);

        // Create the PDU object
        PDU pdu = new PDU();
        for (String oid : Device.ScanOIDs.values()) { 
             pdu.add(new VariableBinding(new OID(oid))); 
        } 
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));

        // Create Snmp object for sending data to Agent
        Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
        ResponseEvent response = snmp.send(pdu, comtarget);

        // Process Agent Response
        if (response.getResponse() != null){

            PDU responsePDU = response.getResponse();
            if (responsePDU != null){

                int errorStatus = responsePDU.getErrorStatus();
                int errorIndex = responsePDU.getErrorIndex();
                String errorStatusText = responsePDU.getErrorStatusText();

                if (errorStatus == PDU.noError){

                    System.out.println(host+" added");
                    Device d= new Device(host);
                    Vector vbs = responsePDU.getVariableBindings(); 
                    for (int j = 0; j < vbs.size(); j++) { 
                        VariableBinding vb = (VariableBinding) vbs.get(i); 
                        d.oids.put(vb.getOid().toString(), vb.getVariable().toString()); 
                     }
                    Devices.add(d); //add reachable devices
                }
               
            }
          
        }
      
        snmp.close();
    }
    
   }
   
   //Main class
   public static void main (String args[]) throws IOException
   {
       Network n= new Network();
       n.setSubnet("192.168.1");
       //n.checkHostsPing();
       n.checkHostsSnmp();
       
   }
}

