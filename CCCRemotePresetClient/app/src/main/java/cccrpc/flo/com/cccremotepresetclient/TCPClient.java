package cccrpc.flo.com.cccremotepresetclient;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Prashant Adesara
 * Handle the TCPClient with Socket Server.
 * */

public class TCPClient {

    private String serverMessage;
    /**
     * Specify the Server Ip Address here. Whereas our Socket Server is started.
     * */
    public String SERVERIP; // your computer IP address
    public int SERVERPORT ;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    public Activity MainActivity;
    private PrintWriter out = null;
    private BufferedReader in = null;
    Socket socket;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(final String IP,final String Port,final OnMessageReceived listener)
    {
        mMessageListener = listener;
        SERVERIP=IP;
        SERVERPORT=Integer.getInteger(Port,34762);

    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            System.out.println("message: "+ message);
            out.println(message);
            out.flush();
        }
    }



    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Log.d("TCP Client", "Conecting to: " + SERVERIP + ":" + SERVERPORT);

            //create a socket to make the connection with the server
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddr, SERVERPORT),3000);
            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server

                mMessageListener.messageReceived("ServerSucces");

                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                        Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                    }
                    serverMessage = null;
                }
            }
            catch (Exception e)
            {
                Log.e("TCP SI Error", "SI: Error", e);
                e.printStackTrace();
                mMessageListener.messageReceived("ServerError");
            }
            finally
            {
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP SI Error", "SI: Error", e);
            mMessageListener.messageReceived("ServerError");
        }

    }


    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    public void closeConnection(){
        mRun=false;
        try{
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}