package com.cccrps.gui;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
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
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


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
		frmCccrps.setTitle("CCCRPS");
		frmCccrps.setBounds(100, 100, 240, 300);
		
		
		JPanel panel = new JPanel();
		frmCccrps.getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("CCC Remote Profile Server");
		panel.add(lblNewLabel);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(SystemColor.menu);
		frmCccrps.getContentPane().add(desktopPane, BorderLayout.CENTER);
		
		JButton btnAbout = new JButton("About");
		btnAbout.setBounds(10, 203, 212, 23);
		desktopPane.add(btnAbout);
		
		final JCheckBox chckbxStartMinimized = new JCheckBox("Start Minimized");
		chckbxStartMinimized.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				prefs.put("checkbminimized", String.valueOf(chckbxStartMinimized.isSelected()));
			}
		});

		chckbxStartMinimized.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxStartMinimized.setBounds(10, 65, 212, 25);
		desktopPane.add(chckbxStartMinimized);
		
		    	
		boolean isminimized=Boolean.parseBoolean(prefs.get("checkbminimized", ""));
    	chckbxStartMinimized.setSelected(isminimized);
    	System.out.println(isminimized);
    	if(isminimized){
    		System.out.println("Minimizing");
    		frmCccrps.setExtendedState(Frame.ICONIFIED);
    	}
    	
    	JButton btnCloseServer = new JButton("Close Server");
    	btnCloseServer.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			System.exit(0);
    		}
    	});
    	btnCloseServer.setBounds(10, 177, 212, 23);
    	desktopPane.add(btnCloseServer);
    	
    	JButton btnStartOnWindows = new JButton("Launch on Windows Startup");
    	btnStartOnWindows.setForeground(new Color(255, 0, 0));
    	btnStartOnWindows.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			AutostartDialog.main();
    		}
    	});
    	btnStartOnWindows.setBounds(10, 13, 212, 43);
    	desktopPane.add(btnStartOnWindows);
    	
    	JLabel lblIp = new JLabel("IP:     "+InetAddress.getLocalHost().getHostAddress());
    	lblIp.setHorizontalAlignment(SwingConstants.CENTER);
    	lblIp.setBounds(12, 119, 210, 16);
    	desktopPane.add(lblIp);
		
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










