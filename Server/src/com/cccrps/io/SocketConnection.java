package com.cccrps.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class SocketConnection extends Thread{

	private Socket client = null;
	private boolean running = false;
	private PrintWriter mOut;
	private OnMessageReceived messageListener;
	

	public SocketConnection(Socket cl,OnMessageReceived messageListener)
	{
		this.messageListener = messageListener;
		this.client=cl;
	}




	public void sendMessage(String message)
	{
		try
		{
			if (mOut != null && !mOut.checkError())
			{
				System.out.println("Send: "+message);
				mOut.println(message);
				mOut.flush();
			}
		}
		catch (Exception e)
		{
		}
	}


	@Override
	public void run()
	{
		//super.run();
		running = true;
		try
		{
			System.out.println("PA: ThreadConnecting...");

			try
			{
				
				
				
				// sends the message to the client
				mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

				// read the message received from client
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				System.out.println("PA: Connecting Done.");
				
				sendMessage("CCCRPS0001");
				
				while (running)
				{
					String message = in.readLine();
					if (message != null && messageListener != null)
					{
						if(message.matches("exit")){
							running=false;
						}else{
							messageListener.messageReceived(this,message);
						}
					}else{
						running=false;
						System.out.println("PA: Lost heartbeat");
					}
				}
			}
			catch (Exception e)
			{
				System.out.println("PA: Error: "+e.getMessage());
				e.printStackTrace();
				
			}
			finally
			{
				this.client.close();
				System.out.println("PA: Connection Closed");
			}
		}
		catch (Exception e)
		{
			System.out.println("PA: Error");
			e.printStackTrace();
		
		}

	}


	public interface OnMessageReceived
	{
		public void messageReceived(SocketConnection client,String message);
	}
	
}
