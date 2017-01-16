/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.*;
import snmp.manager.Groups.Group;


/**
 *
 * @author Taha
 */
public class Users implements Serializable {
    
    public static class User implements Serializable {
        public String username;
        public byte password[]; //hached password    
        public ArrayList<Group> groupHeBelongsTo = new ArrayList<>() ;//contains refrences to the groups user belongs to  
        
        //other caracteristics to be added later
    }
    
    private static ArrayList<User> usersList = new ArrayList<>() ; 
    public static User currentUser; // the user currently logged in the session
    
    /**
     * importing the "Users.ser" file that contains the serialized value of ArrayList<User> usersList
     */
    public static void importUsers(){
        //importing serialized users file
        try{
            //use buffering
            FileInputStream file = new FileInputStream("Users.ser");
            ObjectInputStream input = new ObjectInputStream (file);
            try{
              //deserialize the List
              usersList = (ArrayList<User>)input.readObject();
              //display its data
              for(User user: usersList){
                    System.out.println("Recovered user: " + user.username);
                    for(int i=0; i<user.groupHeBelongsTo.size(); i++)
                    {
                        System.out.println("  groups: ");
                        System.out.println("  "+user.groupHeBelongsTo.get(i).getName());
                    }
                    System.out.println();
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
    
    /**
     * creating / updating the "Users.ser" file that contains the serialized value of ArrayList<User> usersList
     */
    public static void saveUsersFile(){
        try{
            //use buffering
            FileOutputStream file = new FileOutputStream("Users.ser");
            ObjectOutputStream output = new ObjectOutputStream(file);
            try{
              output.writeObject(usersList);
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
     * adding a new user to the users list 
     *
     * @param username
     * @param password
     * @return 1 if created successfully, -1 if user exists
     */
    public static int addUser(String username, String password){
        try {
            
            User newUser = getUser(username);
            if(newUser==null){
                newUser=new User();
                // haching the sting password
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                newUser.username = username;
                newUser.password = md.digest();
                usersList.add(newUser);
            }
            else{
                System.err.println("username already exists");
                return -1;
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
    
    /**
     *  check the authentification of a given username and password
     * 
     * @param username 
     * @param password
     * @return  true if the (username, password) combination exists in the users list, else false  
     */
    public static boolean checkUser(String username, String password){
        User user = getUser(username);
        if(user!=null){
            try {
                System.out.println(user.username);
                // haching the sting password
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                if(Arrays.toString(user.password).equals(Arrays.toString(md.digest()))){
                    currentUser = user;
                    // the logged in user is placed in the head og the list, this helps knowing who is the last user who used the program
                    usersList.remove(user);
                    usersList.add(0, user);
                    return true;
                }
            else
                return false;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            
        }
        else
            return false;
    }
    
    /**
     * looking for a user by its username
     *
     * @param username the username of the user to get
     * @return User
     */
    public static User getUser(String username){
        int i;
        for(i=0; (i<usersList.size())?!usersList.get(i).username.equals(username):false; i++){ 
                System.out.println("searching for user "+i+"/"+usersList.size());
            }
        if(i==usersList.size()){
            System.out.println("user not found");
            return null;
        }
        else
            return usersList.get(i);
    }
    /**
     * add user to a group
     *
     * @param username
     * @param groupName
     */
    public static void addUserToGroup(String userName, String groupName)
    {
        User user=getUser(userName);
        if(user!=null)
        {
            Group group=Groups.getGroup(groupName);
            if(group!=null)
            {
                user.groupHeBelongsTo.add(group);
                System.out.println(userName+" added to the group "+groupName);
            }
            else
            {
                System.out.println("The group doesn't exist");
              
            }
            
        }
    }
     /**
     * remove user from a group
     *
     * @param username
     * @param groupName
     */
    public static void removeUserFromAGroup(String userName, String groupName)
    {
        User user=getUser(userName);
        if(user!=null)
        {
            int i;
            for(i=0; (i<user.groupHeBelongsTo.size())?!user.groupHeBelongsTo.get(i).getName().equals(groupName):false; i++){ 
                System.out.println("searching for group");
            }
            if(i!=user.groupHeBelongsTo.size()){
               user.groupHeBelongsTo.remove(i);
               System.out.println(userName+"removed from the group"+groupName);
            }
            else
               System.out.println("group not found");
            }
    }
     /**
     * return members of a a group
     *
     * @param groupName
     * @return 
     */
     
    public static ArrayList<User> getGroupmembers(String groupName)
         {
             ArrayList<User> groupMembers = new ArrayList<>() ; 
            for(int i=0;i<usersList.size(); i++)
            {
                int j;
                for(j=0; (j<usersList.get(i).groupHeBelongsTo.size())?!usersList.get(i).groupHeBelongsTo.get(j).getName().equals(groupName):false; i++){ 
                }
                 if(j!=usersList.get(i).groupHeBelongsTo.size()){
                    groupMembers.add(usersList.get(i));
                  }
                
            }
            return groupMembers;
         }
    
    /**
     *
     * @return int : number of created users
     */
    public static int getUsersCount(){
        return usersList.size();
    }
    
    public static String getLastUser(){
        return usersList.get(0).username;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    
    
    
    public static void main(String[] args){
        
         Groups.importGroups();         
         /*addUser("admin","admin");
         addUserToGroup("admin","admin");
         saveUsersFile();*/

         importUsers();
       
 //        checkUser("admin", "admin");
//       
//        System.out.println(checkUser("admin", "admin"));
        
    }
    
}

