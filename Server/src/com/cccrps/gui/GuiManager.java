package com.cccrps.gui;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemColor;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.io.IOUtils;

import com.cccrps.main.Main;
import com.cccrps.main.SimpleUpdater;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class GuiManager {

	private JFrame frmCccrps;


	/**
	 * Create the application.
	 * @throws FileNotFoundException 
	 */
	public GuiManager() throws FileNotFoundException {
		
	}

	public JFrame getFrame(){
		return frmCccrps;	
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 * @throws FileNotFoundException 
	 * @throws UnknownHostException 
	 * @wbp.parser.entryPoint
	 */
	
	public void initialize() throws FileNotFoundException, UnknownHostException {
		

		
		frmCccrps = new JFrame();
		Image icon = new ImageIcon(this.getClass().getResource("/images/icon.png")).getImage();
		frmCccrps.setIconImage(icon);
		frmCccrps.addWindowListener(new WindowAdapter() {
			
            public void windowOpened(WindowEvent e){
            	
            	
            	final Preferences prefs;
            	prefs = Preferences.userNodeForPackage(this.getClass());
        		boolean isminimized=Boolean.parseBoolean(prefs.get("checkbminimized", ""));
            	System.out.println(isminimized);
            	if(isminimized){
            		System.out.println("Minimizing");
            		frmCccrps.setExtendedState(Frame.ICONIFIED);
            	}
            	
            	
            	System.out.println("WindowOpened");
            	
            }
            
           @Override
           public void windowClosing(WindowEvent e){
        	   frmCccrps.setVisible(false);              
        	   displayTrayIcon();
           }
           
           public void windowClosed(WindowEvent e){}
           public void windowIconified(WindowEvent e){     
        	   frmCccrps.setVisible(false);              
                displayTrayIcon();
           }
           public void windowDeiconified(WindowEvent e){}
           public void windowActivated(WindowEvent e){
                System.out.println("windowActivated");
           }
           public void windowDeactivated(WindowEvent e){}
		});

    	final Preferences prefs;
    	prefs = Preferences.userNodeForPackage(this.getClass());
		
		
		frmCccrps.setResizable(false);
		frmCccrps.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmCccrps.setTitle("CRP");
		frmCccrps.setBounds(100, 100, 240, 310);
		
		
		JPanel panel = new JPanel();
		frmCccrps.getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Catalyst Remote Profile Server");
		panel.add(lblNewLabel);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(SystemColor.menu);
		frmCccrps.getContentPane().add(desktopPane, BorderLayout.CENTER);
		
		final JCheckBox chckbxStartMinimized = new JCheckBox("Start Minimized");
		chckbxStartMinimized.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				prefs.put("checkbminimized", String.valueOf(chckbxStartMinimized.isSelected()));
			}
		});

		chckbxStartMinimized.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxStartMinimized.setBounds(10, 80, 212, 25);
		desktopPane.add(chckbxStartMinimized);
		
		    	
		boolean isminimized=Boolean.parseBoolean(prefs.get("checkbminimized", ""));
    	System.out.println(isminimized);
    	if(isminimized){
    		System.out.println("Minimizing");
    		chckbxStartMinimized.setSelected(true);
    		frmCccrps.setExtendedState(Frame.ICONIFIED);
    	}
    	
    	JButton btnCloseServer = new JButton("Shutdown Server");
    	btnCloseServer.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			System.exit(0);
    		}
    	});
    	btnCloseServer.setBounds(10, 177, 212, 30);
    	desktopPane.add(btnCloseServer);
    	
    	JButton btnStartOnWindows = new JButton("Website(Instructions & About)");
    	btnStartOnWindows.setForeground(new Color(255, 0, 0));
    	btnStartOnWindows.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			try {
					java.awt.Desktop.getDesktop().browse(new URI("http://goo.gl/uihUNy"));
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	});
    	btnStartOnWindows.setBounds(10, 28, 212, 43);
    	desktopPane.add(btnStartOnWindows);
    	
    	JLabel lblIp = new JLabel("");
    	lblIp.setText("IP:     "+InetAddress.getLocalHost().getHostAddress());
    	lblIp.setHorizontalAlignment(SwingConstants.CENTER);
    	lblIp.setBounds(10, 148, 210, 16);
    	desktopPane.add(lblIp);
    	
    	final JCheckBox checkBoxAutoStart = new JCheckBox("Autostart");
    	checkBoxAutoStart.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			if(checkBoxAutoStart.isSelected()){
    			JOptionPane.showMessageDialog(null,"!Warning, if ServerFile(.jar) gets moved , Autostart has to be applied again!");
    			}
    		}
    	});
    	
    	checkBoxAutoStart.addItemListener(new ItemListener() {
    		public void itemStateChanged(ItemEvent arg0) {
    			
    			
    			
    			prefs.put("autostart", String.valueOf(checkBoxAutoStart.isSelected()));
    			String targetpath = "C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\CRPServerAutostarter.bat";
			    if(checkBoxAutoStart.isSelected()){
    				Writer writer = null;
    				try {
    					writer = new BufferedWriter(new OutputStreamWriter(
    				          new FileOutputStream(targetpath), "utf-8"));
    					File f = new File(System.getProperty("java.class.path"));
    					File dir = f.getAbsoluteFile().getParentFile();
    					String path = dir.toString();
    				    writer.write("start /min javaw -jar \""+path+"\\"+f.getName()+"\"");
    				} catch (IOException ex) {
    				  // report
    				} finally {
    				   try {writer.close();} catch (Exception ex) {}
    				   
    				}
    				
    			}else{
    				try{
    					 
    		    		File file = new File(targetpath);
    		 
    		    		file.delete();
    		 
    		    	}catch(Exception e){
    		 
    		    		e.printStackTrace();
    		 
    		    	}
    			}
    		}
    	});
    	if(Boolean.parseBoolean(prefs.get("autostart", ""))){
    		checkBoxAutoStart.setSelected(true);
    	}else{
    		checkBoxAutoStart.setSelected(false);
    	}
    	    	
    	checkBoxAutoStart.setHorizontalAlignment(SwingConstants.CENTER);
    	checkBoxAutoStart.setBounds(10, 110, 212, 25);
    	desktopPane.add(checkBoxAutoStart);
    	
    	JLabel lblVersion = new JLabel("Version Undefined");
    	lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
    	lblVersion.setBounds(41, 0, 154, 16);
    	lblVersion.setText("Version: "+Main.getVersion());
    	desktopPane.add(lblVersion);
    	
    	JButton btnCheckForUpdates = new JButton("Check For Updates");
    	btnCheckForUpdates.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			
    			try {
    				int NewVersion=Integer.parseInt(IOUtils.toString(new URL("https://flothinkspi.github.io/Catalyst-PresetSwitcher/version.json")).replace("\n", "").replace("\r", ""));
					if(NewVersion>Integer.parseInt(Main.getVersion())){
						int dialogResult = JOptionPane.showConfirmDialog (null, "Update found from "+Main.getVersion()+" to "+NewVersion+" ,Update now ?","CRPServer Updater",JOptionPane.YES_NO_OPTION);
						if(dialogResult == JOptionPane.YES_OPTION){
							String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
							String decodedPath = URLDecoder.decode(path, "UTF-8");
							System.out.println(decodedPath);
							SimpleUpdater.update(new URL("https://flothinkspi.github.io/Catalyst-PresetSwitcher/download/CRPServer.jar"),new File(decodedPath) , "updated");
							System.exit(0); 
						}
					}else{
						JOptionPane.showConfirmDialog (null, "No Updates , You have the latest CRPServer(Version:"+Main.getVersion()+")","CRPServer Updater",JOptionPane.PLAIN_MESSAGE);
						}
				} catch (NumberFormatException | HeadlessException
						| IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    		}
    	});
    	btnCheckForUpdates.setBounds(10, 213, 212, 30);
    	desktopPane.add(btnCheckForUpdates);
		
		frmCccrps.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        
		    	frmCccrps.setExtendedState(Frame.ICONIFIED);
		    	
		    }
		});
		
		
	}	
		
	



public void displayTrayIcon(){
    final TrayIcon trayIcon;
    if (SystemTray.isSupported()) {
        final SystemTray tray = SystemTray.getSystemTray(); 
        Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/icon.gif"));
       
        PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(image, "CCCRPS", popup);    
        trayIcon.setImageAutoSize(true);
        //trayIcon.addMouseListener(mouseListener);
        
        MenuItem defaultItem = new MenuItem("Stop Server");
        defaultItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });
        popup.add(defaultItem);
        
        trayIcon.addMouseListener(
                new MouseAdapter() {
                   public void mouseClicked(MouseEvent e) {
                	   if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                		   frmCccrps.setVisible(true);
                		   frmCccrps.setState ( Frame.NORMAL );
                		   tray.remove(trayIcon);
                	   }
                   }
                });
          

        


            try {
                  tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
    } else {
        System.err.println("System tray is currently not supported.");
    }          
  }
}










