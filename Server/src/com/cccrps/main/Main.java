package com.cccrps.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

import com.cccrps.gui.GuiManager;
import com.cccrps.io.SocketServer;

public class Main
{
	static SocketServer mServer;
	static String Version="0002";
	
	public static void main(String[] args) throws Exception
	{		
		
		if(Integer.parseInt(IOUtils.toString(new URL("https://flothinkspi.github.io/Catalyst-PresetSwitcher/version.json")).replace("\n", "").replace("\r", ""))>Integer.parseInt(Version)){
			int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to update the CatalystPresetSwitcher now?","CPS Update Available",JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				SimpleUpdater.update(new URL("https://flothinkspi.github.io/Catalyst-PresetSwitcher/download/CRPServer.jar"),new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"\\CRPServer.jar") , "");
			}
		}
			
		
		GuiManager GuiM = new GuiManager();
		GuiM.initialize();
		GuiM.getFrame().setVisible(true);
		
		
		SocketServer mServer = new SocketServer(); 
	
	}
	
	public static String getRemoteVersion(final String URL) throws Exception {
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(URL));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		return content;
	}
	
	public static String getVersion(){
		return Version;
	}
}
