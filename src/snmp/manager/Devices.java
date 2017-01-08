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
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Taha
 */
public class Devices  implements Serializable {
    
    private static ArrayList<Device> devicesList = new ArrayList<>() ; 

    /**
     * importing the "Devices.ser" file that contains the serialized value of ArrayList<Device> devicesList
     */
    public static void importDevices(){
        //importing serialized users file
        try{
            //use buffering
            FileInputStream file = new FileInputStream("Devices.ser");
            ObjectInputStream input = new ObjectInputStream (file);
            try{
              //deserialize the List
              devicesList = (ArrayList<Device>)input.readObject();
              //display its data
              for(Device device: devicesList){
                    System.out.println("Recovered Device: " + device.ipAdress);
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
            System.err.println("Cannot perform input."+ ex);
            saveDevicesFile();
            System.err.println("new file Users.ser created");
        }
    }
    
    /**
     * creating / updating the "Devices.ser" file that contains the serialized value of ArrayList<Device> devicesList
     */
    public static void saveDevicesFile(){
        try{
            //use buffering
            FileOutputStream file = new FileOutputStream("Devices.ser");
            ObjectOutputStream output = new ObjectOutputStream(file);
            try{
              output.writeObject(devicesList);
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
    
    /**
     * adding a new device to the devices list 
     *
     * @param username
     * @param password
     * @return 1 if created successfully, -1 if device exists
     */
    public static int addDevice(String IP ){
        Device newDevice = getDevice(IP);
        if(newDevice==null){
            newDevice=new Device(IP);
            devicesList.add(newDevice);
        }
        else{
            System.err.println("IP already exists");
            return -1;
        }
        return 1;
    }
    
    public static Device addDevice(String alias, String IP ){
        Device newDevice = getDevice(IP);
        if(newDevice==null){
            newDevice=new Device(IP);
            devicesList.add(newDevice);
        }
        newDevice.alias=alias;
        return newDevice;
    }
    
    /**
     * looking for a device by its IP address
     *
     * @param IP the IP address of the device to get
     * @return User
     */
    public static Device getDevice(String IP){
        int i;
        for(i=0; (i<devicesList.size())?!devicesList.get(i).ipAdress.equals(IP):false; i++){ 
                System.out.println("searching for device "+i+"/"+devicesList.size());
            }
        if(i==devicesList.size()){
            System.out.println("device not found");
            return null;
        }
        else
            return devicesList.get(i);
    }
    
    /**
     * get all the devices list
     * @return 
     */
    public static ArrayList<Device> getDevicesList() {
        return devicesList;
    }
    
    /**
     *
     * @return int : devices number
     */
    public static int getDevicesCount(){
        return devicesList.size();
    }
    
    
    public static void main(String[] args){

        importDevices();
        
    }
    
    
}
