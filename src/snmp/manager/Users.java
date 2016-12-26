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


/**
 *
 * @author Taha
 */
public class Users implements Serializable {
    
    public static class User implements Serializable {
        public String username;
        public byte password[];          //hached password
        
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
    public static int addUser(String username, String password ){
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
    
    
    public static void main(String[] args){
//        addUser("admin","admin");
//        saveUsersFile();

        importUsers();
        checkUser("admin", "admin");
//        
//        System.out.println(checkUser("admin", "admin"));
        
    }
    
}

