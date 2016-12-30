/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static snmp.manager.Users.addUser;
import static snmp.manager.Users.currentUser;
import static snmp.manager.Users.importUsers;
import static snmp.manager.Users.saveUsersFile;


public class Network {
    
  
   IPv4 ip;
   String subnet;
   private ArrayList<Device> Devices = new ArrayList<Device>(); //contains the network devices
   
   public Network(String ipS, String netMask)
   {
       ip=new IPv4(ipS,netMask);
       subnet=ip.getCIDR();
   }
   
   public void checkHostsPing() throws UnknownHostException, IOException{ //discover hosts using ping
   int timeout=1000;
   long numberOfHosts= ip.getNumberOfHosts();
   List<String> availableIPs= ip.getAvailableIPs((int)(long)(numberOfHosts));
       for (int i=0;i<availableIPs.size();i++){
       if (InetAddress.getByName(availableIPs.get(i)).isReachable(timeout)){
           System.out.println(availableIPs.get(i) + " is reachable");
           Devices.add(new Device(availableIPs.get(i))); //add reachable devices
           
       }
    }
   }
   /*public void checkHostsSnmp() throws UnknownHostException, IOException{ //discover hosts using ping
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
                        VariableBinding vb = (VariableBinding) vbs.get(j); 
                        d.oids.put(vb.getOid().toString(), vb.getVariable().toString()); 
                     }
                    Devices.add(d); //add reachable devices
                }
               
            }
          
        }
      
        snmp.close();
    }
   }*/
   
    public void importDevices(){
        //importing serialized devices file
        try{
            //use buffering
            FileInputStream file = new FileInputStream("Devices.ser");
            ObjectInputStream input = new ObjectInputStream (file);
            try{
              //deserialize the List
              Devices = (ArrayList<Device>)input.readObject();
              //display its data
              for(Device dv: Devices){
                    System.out.println("Recovered device: " + dv.ipAdress);
              }
            }
            finally{
                input.close();
                file.close();
            }
        }
        catch(ClassNotFoundException ex){
            System.err.println("Cannot perform input. Class not found."+ ex);
        }
        catch(IOException ex){
            //if the file Users.ser doesnt exist in case of a 1st execution for example the file will be created with the user admin 
            System.err.println("Cannot perform input."+ ex);
            addUser("admin","admin");
            saveUsersFile();
            System.err.println("new file Users.ser created");
        }
    }
    
   
     public void saveDeviceFile(){
        try{
            //use buffering
            FileOutputStream file = new FileOutputStream("Devices.ser");
            ObjectOutputStream output = new ObjectOutputStream(file);
            try{
              output.writeObject(Devices);
            }
            finally{
              output.close();
              file.close();
            }
        }   
        catch(IOException ex){
            System.err.println("Cannot perform output."+ ex);
        }

    }
      public static int addDevice(Network n,String ip, String alias){

            
            Device dv = n.getDevice(ip);
            if(dv==null){
                dv=new Device(ip, alias);
                return 1;
            }
            else{
                System.err.println("Device already exists");
                return -1;
            }
       
    }
    
    
    /**
     * looking for a device by its ip address
     *
     * 
     */
    public Device getDevice(String ip){
        int i;
        for(i=0; (i<Devices.size())?!Devices.get(i).ipAdress.equals(ip):false; i++){ 
                System.out.println("searching for device "+i+"/"+Devices.size());
            }
        if(i==Devices.size()){
            System.out.println("device not found");
            return null;
        }
        else
            return Devices.get(i);
    }
    
    /**
     *
     * @return int : number of created devices
     */
    public int getDevicesCount(){
        return Devices.size();
    }
    
    public String getLastDevice(){
        return Devices.get(0).ipAdress;
    }
    
   //Main class
   public static void main (String args[]) throws IOException
   {
       Network n= new Network("192.168.1.2","255.255.255.0");
       n.checkHostsPing();
       //n.checkHostsSnmp();
       //test
      // n.importDevices();
       for(int i=0;i<n.Devices.size();i++)
       {
           n.Devices.get(i).printDeviceInformations();
       }
       n.saveDeviceFile();
   }
}

