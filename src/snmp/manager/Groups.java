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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import snmp.manager.Users.User;



public class Groups implements Serializable{
    
    public static class Group implements Serializable{
        
         private String name;
         //not serializable
         private transient StringProperty nameProperty;
         //constructor
         public Group(String name)
         {
           this.name=name;
           nameProperty=new SimpleStringProperty(name);
         }
         //setters and getters
         public void setName(String name)
         {
           this.name=name;
           this.nameProperty.set(name);
         }
         public String getName()
         {
           return name;
         }
         public StringProperty getNameProperty()
         {
           return nameProperty;
         }
         
        
         
    }
    
    private static ArrayList<Group> groupsList = new ArrayList<>() ; 
    
     public static void importGroups(){
        //importing serialized groups file
        try{
            //use buffering
            FileInputStream file = new FileInputStream("Groups.ser");
            ObjectInputStream input = new ObjectInputStream (file);
            try{
              //deserialize the List
              groupsList = (ArrayList<Group>)input.readObject();
              //display its data
              for(Group group: groupsList){
                    System.out.println("Recovered group: " + group.getName());
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
            saveGroupsFile();
            System.err.println("new file Groups.ser created");
        }
    }
    
    /**
     * creating / updating the "Groups.ser" file that contains the serialized value of ArrayList<Group> groupsList
     */
    public static void saveGroupsFile(){
        try{
            //use buffering
            FileOutputStream file = new FileOutputStream("Groups.ser");
            ObjectOutputStream output = new ObjectOutputStream(file);
            try{
              output.writeObject(groupsList);
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
     * adding a new group to the groups list 
     *
     * @param name the name of the group
     * @return 1 if created successfully, -1 if group exists
     */
    
    public static int addGroup(String name){
        Group newGroup = getGroup(name);
        if(newGroup==null){
            
            groupsList.add(new Group(name));
        }
        else{
            System.err.println("name already exists");
            return -1;
        }
        return 1;
    }
    
    /**
     * looking for a group by its name
     *
     * @param name the name of the group
     * @return Group
     */
    public static Group getGroup(String name){
        int i;
        for(i=0; (i<groupsList.size())?!groupsList.get(i).getName().equals(name):false; i++){ 
                System.out.println("searching for group "+i+"/"+groupsList.size());
            }
        if(i==groupsList.size()){
            System.out.println("group not found");
            return null;
        }
        else
            return groupsList.get(i);
               
    }
    /**
     * get all the groups list
     * @return 
     */
    public static ArrayList<Group> getGroupsList() {
        return groupsList;
    }
    
    /**
     *
     * @return int : groups number
     */
    public static int getGrousCount(){
        return groupsList.size();
    }
    
    
   
    public static void main(String args[])
    {
        addGroup("admin");
        addGroup("g1");
        addGroup("g2");
        saveGroupsFile();
        importGroups();
        getGroup("admin");
    }
}
