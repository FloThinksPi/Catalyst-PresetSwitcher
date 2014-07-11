package com.cccrps.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import com.cccrps.gui.GuiManager;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

public class Main
{
	
	public static void main(String[] args) throws FileNotFoundException, UnknownHostException 
	{
		
		GuiManager GuiM = new GuiManager();
		GuiM.initialize();
		GuiM.getFrame().setVisible(true);
		
		Configuration config = new Configuration();
	    config.setHostname("10.110.109.41");
	    config.setPort(6666);

	    final SocketIOServer server = new SocketIOServer(config);
	   
	    

	    server.addConnectListener(new ConnectListener() {
	        @Override
	        public void onConnect(SocketIOClient client) {

	        	server.addEventListener("message", String.class, new DataListener<String>() {
	    	    	
	    			@Override
	    			public void onData(SocketIOClient Client, String Data, AckRequest arg2)
	    					throws Exception {
	    				
	    				synchronized (System.out) {
	    				    System.out.println("Sending List");
	    				    System.out.flush();
	    				}
	    				Client.sendEvent("recievelistitem", "Work");
	    				Client.sendEvent("recievelistitem", "xbox");
	    			
	    			
	    				
	    			}
	    	    });	
	        	
	        }
	    });

	    server.addDisconnectListener(new DisconnectListener() {
	        @Override
	        public void onDisconnect(SocketIOClient client) {

	        	
	        }
	    });

	    server.start();
	    System.out.println("Socket Server Started");
	    
	}
	
}
