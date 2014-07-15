package com.cccrps.main;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

import com.cccrps.gui.GuiManager;
import com.cccrps.io.SocketServer;

public class Main
{
	static SocketServer mServer;
	
	public static void main(String[] args) throws FileNotFoundException, UnknownHostException 
	{
		
		GuiManager GuiM = new GuiManager();
		GuiM.initialize();
		GuiM.getFrame().setVisible(true);
		
		
		SocketServer mServer = new SocketServer(); 
	
}}
