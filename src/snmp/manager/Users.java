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
    
    private static class User implements Serializable {
        public String username;
        public byte password[];          //hached password
        
        //other caracteristics to be added later
    }
    
    private static ArrayList<User> usersList = new ArrayList<>() ; 
    
    /**
     * importing the "Users.ser" file that contains the serialized value of ArrayList<User> usersList
     */
    public static void importUsers(){
        //importing serialized users file
        try{
            //use buffering
            FileInputStream file = new FileInputStream("quarks.ser");
            ObjectInputStream input = new ObjectInputStream (file);
            try{
              //deserialize the List
              usersList = (ArrayList<User>)input.readObject();
              //display its data
              for(User user: usersList){
                    System.out.println("Recovered Quark: " + user.username);
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
        }
    }
    
    /**
     * creating / updating the "Users.ser" file that contains the serialized value of ArrayList<User> usersList
     */
    public static void saveUsersFile(){
        try{
            //use buffering
            FileOutputStream file = new FileOutputStream("quarks.ser");
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
     */
    public static void addUser(String username, String password ){
        try {
            User newUser = new User();
            // haching the sting password
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            newUser.username = username;
            newUser.password = md.digest();
            usersList.add(newUser);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
            // haching the sting password
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            if(user.password == md.digest())
                return true;
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
        for(i=0 ; !usersList.get(i).username.equals(username) && i<usersList.size();i++){ 
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
        addUser("admin","admin");
        saveUsersFile();

//        importUsers();
//                for(User user: usersList){
//                    System.out.println("Recovered user: " + user.username);
//                }
    }
    
}

