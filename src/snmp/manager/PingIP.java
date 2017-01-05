/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.manager;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class PingIP {

    private String ip;
    private boolean replied = true;
    private ArrayList<String>  output = new ArrayList();
  public static void runSystemCommand(String command) {

		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(p.getInputStream()));

			String s = "";
			// reading output stream of the command
			while ((s = inputStream.readLine()) != null) {
				System.out.println(s);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    public PingIP(String ip) {
        this.ip = ip;
    }
    public PingIP() {
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public String getAnswer() {
        return (output.size()>1)?output.get(2): output.get(1);
    }
    
    public String getAnswerLine(int n) {
        return ((n<output.size())?output.get(n):"error line doesnt exist");
    }

    public boolean isReplied() {
        return replied;
    }
    
        
    public String run(){
        int i =0;
        String outputStram = "";
        try {
			Process p = Runtime.getRuntime().exec("ping " + ip);
			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(p.getInputStream()));

			String s = "";
			// reading output stream of the command
			while ((s = inputStream.readLine()) != null && replied) {
                            output.add(s);
                            outputStram= (outputStram+s);
                            if(i==3)
                                replied=output.get(3).substring(0, 5).matches("Reply");
                            i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
        return outputStram;
    }
    public String ping(String ipAddress){
        int i =0;
        String outputStram = "";
        try {
			Process p = Runtime.getRuntime().exec("ping " + ipAddress);
			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(p.getInputStream()));

			String s = "";
			// reading output stream of the command
			while ((s = inputStream.readLine()) != null && replied) {
                            output.add(s);
                            outputStram= (outputStram+s);
                            if(i==3)
                                replied=output.get(3).substring(0, 5).matches("Reply");
                            i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
        return outputStram;
    }
    
    
        
	public static void main(String[] args) {
		
            String ip = "192.168.56.106";
            PingIP p = new PingIP(ip);
//            System.out.println(p.run());
            p.run();
//            output.stream().forEach((line) -> {
//                System.out.println(line);
//        });
//            for (int i = 0; i<output.size();i++)
//                System.out.println(i+". "+output.get(i));
//	           
//            System.out.println(output.get(3).substring(0, 5)+ " "+ replied);
            
	}
        
}