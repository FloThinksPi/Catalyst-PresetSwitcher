package com.cccrps.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

import com.cccrps.gui.GuiManager;
import com.cccrps.io.SocketServer;

public class Main
{
	static SocketServer mServer;
	static String Version="0004";
	
	public static void main(String[] args) throws Exception
	{
		
		
		if(args.length > 0) {
			if(args[0].contains("updated")) {
				JOptionPane.showConfirmDialog (null, "Update successful, you are now on Version: "+Main.getVersion(),"CRPServer Updater",JOptionPane.PLAIN_MESSAGE);						
			} 
		}
		
		int NewVersion=Integer.parseInt(IOUtils.toString(new URL("https://flothinkspi.github.io/Catalyst-PresetSwitcher/version.json")).replace("\n", "").replace("\r", ""));
		if(NewVersion>Integer.parseInt(Main.getVersion())){
			int dialogResult = JOptionPane.showConfirmDialog (null, "Update found from "+Main.getVersion()+" to "+NewVersion+" ,Update now ?","CRPServer Updater",JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				String decodedPath = URLDecoder.decode(path, "UTF-8");
				System.out.println(decodedPath);
				SimpleUpdater.update(new URL("https://flothinkspi.github.io/Catalyst-PresetSwitcher/download/CRPServer.jar"),new File(decodedPath) , "-updated");
				System.exit(0); 
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
