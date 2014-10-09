package com.myemr.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;


public class EMRServer {
	
	private static final int SERVER_PORT = 50030;   
	private static final String SERVER_KET_PASSWORD = "123456";  // password for file ksever.keystore which stores the private key of the server
	private static final String SERVER_AGREEMENT = "SSL"; 
	private static final String SERVER_KEY_MANAGER = "SunX509";   
	private static final String SERVER_KEY_KEYSTORE = "JKS";    
	
	private static final int LOGIN = 1;
	private static final int MAIN =2;
	
	private static final String SUCCESS= "successful login";
	private static final String NOUSER = "Username does not exists";
	private static final String PASSWORDERROR="password is not correct";
	
	private SSLServerSocket serverSocket;
	private Socket server;
	private Connection connection;
	private Statement st;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EMRServer server = new EMRServer();
		server.init();
		server.start();
	}
	
	private void init(){
    	try{
    		SSLContext sslContext = SSLContext.getInstance(SERVER_AGREEMENT);  
  	      	KeyManagerFactory keyManager = KeyManagerFactory.getInstance(SERVER_KEY_MANAGER);  
  	      	KeyStore kks= KeyStore.getInstance(SERVER_KEY_KEYSTORE);   
  	      	kks.load(new FileInputStream("/kserver.keystore"),SERVER_KET_PASSWORD.toCharArray());   
  	      	keyManager.init(kks,SERVER_KET_PASSWORD.toCharArray());   
  	      	sslContext.init(keyManager.getKeyManagers(),null,null);  
  	      	serverSocket = (SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket(SERVER_PORT);
  	      }catch (Exception e) {  
  	    	  System.out.println(e.getMessage());
      	  } 
	}
	
	private void start(){
		System.out.println("Server is running...");
		while(true){
			try{
				server=serverSocket.accept();
				Thread thread = new Thread(new ClientThread());
				thread.start();
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	class ClientThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
				String line;
				line = in.readLine();
				System.out.println(line);
				switch(line.charAt(0)-'0'){
				case LOGIN:
					login(line.substring(1));
					break;
				case MAIN:
					searchdata(line.substring(1));
				}
    	    }catch(Exception e){
    	    	System.out.println(e.getMessage());
    	    }
		}
	}
	//Test username and password
	private void login(String s){
		String[] result = s.split(",");
		String response="";
		if(result==null||result.length!=2){
			System.out.println("Error");
			return;
		}
		connection=getConnection();
        try {  
            String sql = "select * from login where username="+"'"+result[0]+"'";     
            st = (Statement) connection.createStatement();     
            ResultSet rs = st.executeQuery(sql);    
            if(rs.first()){
            	if(rs.getString("hashvalue").equals(result[1]))
            		response=SUCCESS;
            	else
            		response=PASSWORDERROR;
            }else
            	response=NOUSER;
            
            connection.close();  
            sendbacktoclient(response);   
        } catch (SQLException e){  
            System.out.println(e.getMessage());
        }
	}
        
	
	private void searchdata(String name){
		String[] result = name.split(",");
		String response="";
		if(result==null||result.length!=2){
			System.out.println("Error");
			return;
		}
		connection=getConnection();
		try{
			String sql="select * from "+result[0]+" where username ="+"'"+result[1]+"'";
			st=(Statement)connection.createStatement();
			ResultSet rs=st.executeQuery(sql);
			if(!rs.first()){
				System.out.println("Error in the database");
				return;
			}
			if(result[0].equals("basicinfo"))
				response=rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getDate(5).toString()+","
			+rs.getString(6)+","+rs.getString(7);
			else
				response=rs.getString(2)+","+String.valueOf(rs.getInt(3))+","+String.valueOf(rs.getInt(4))+","+rs.getDate(5).toString();
            connection.close();  
            sendbacktoclient(response);  
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
		
	}
	
	private void sendbacktoclient(String res){
		 try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())),true);
				out.println(res);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	private Connection getConnection(){
		Connection con = null;  
        try {  
            Class.forName("com.mysql.jdbc.Driver");                
            con = DriverManager.getConnection(  
                    "jdbc:mysql://localhost:3306/myemr", "root", "root");  
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        }  
        return con;   
	}
}
