package com.cccrps.main;

import java.io.IOException;

import com.cccrps.gui.GuiManager;
import com.cccrps.sockets.Server;

public class Main
{
	
	public static void main(String[] args) throws IOException
	{
		
		GuiManager GuiM = new GuiManager();
		GuiM.initialize();
		GuiM.getFrame().setVisible(true);
		
		final Server server = new Server(6666);
		Runnable r = new Runnable() {
			public void run() {
				server.start();
			};
		};
		new Thread(r).start();
		
	}
		
}
