package com.cccrps.io;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;



/**
 * @author Prashant Adesara
 * The class extends the Thread class so we can receive and send messages at the
 * same time
 */
public class SocketServer
{
	
	   static final int PORT = 34762;

	    @SuppressWarnings("resource")
		public SocketServer() {
	        ServerSocket serverSocket = null;
	        Socket socket = null;

	        try {
	            serverSocket = new ServerSocket(PORT);
	            System.out.println("SocketServer Created");
	        } catch (IOException e) {
	        	System.out.println("ServerSetupError:" + e);
	            e.printStackTrace();
	            System.exit(0);
	        }
	        while (true) {
	            try {
	                socket = serverSocket.accept();
	                System.out.println("NewClient");
	            } catch (IOException e) {
	                System.out.println("I/O error: " + e);
	            }
	            // new threa for a client
	            
	            SocketConnection SConnection = new SocketConnection(socket,new SocketConnection.OnMessageReceived()
				{
					@Override
					public void messageReceived(SocketConnection client,String message)
					{
						System.out.println("Msg Recieved: "+ message);

						
						if(message.matches("RequestList")==false){      
					        try {
							    boolean recursive = true;
							    
							    String RootPath = "C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\AMD Catalyst Control Center\\";
							    Collection files = FileUtils.listFiles(new File(RootPath), null, recursive);
							    String fileName = message;
							    System.out.println(RootPath+fileName);
							    for (Iterator iterator = files.iterator(); iterator.hasNext();) {
							        File file = (File) iterator.next();
							        if (file.getName().equals(fileName)){
							        	System.out.println(file.getAbsolutePath());
							        	ProcessBuilder pb = new ProcessBuilder("cmd", "/c", file.getAbsolutePath());						        	
							        	Process process = pb.start();	        	
							        }

							    }
							} catch (Exception e) {
							    e.printStackTrace();
							}
							
							
						}
						
						if(message.matches("RequestList")){
							client.sendMessage("StartRecords");
							boolean recursive = true;
							
							try {
							String RootPath = "C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\AMD Catalyst Control Center";
						    Collection files = FileUtils.listFiles(new File(RootPath), null, recursive);
						    for (Iterator iterator = files.iterator(); iterator.hasNext();) {
						        File file = (File) iterator.next();
						        if (true){
						        	System.out.println(file.getAbsolutePath());
						        	client.sendMessage(file.getName());        	
						        }

						    }
						} catch (Exception e) {
						    e.printStackTrace();
						}
							
							client.sendMessage("StopRecords");
						}
					}
					
				});
	            
	            SConnection.start();
				
	        }
	    }
	
}
