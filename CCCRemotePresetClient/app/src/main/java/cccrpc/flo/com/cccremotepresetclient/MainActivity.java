package cccrpc.flo.com.cccremotepresetclient;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;


public class MainActivity extends Activity {


    private TCPClient mTcpClient = null;
    private connectTask conctTask = null;
    boolean ServerStatus = false;
    boolean recordInput=false;
    ArrayList<PresetData> Presets;
    boolean DataRequestInProgress=false;
    DialogFragment mDialogFragment;
    boolean ServerOK=true;
    boolean ServerSucess=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ConnectFragment())
                    .commit();
        }


    }


    public void connectToServer(String ip, String port) {
        mTcpClient = null;
        ServerStatus = true;

        conctTask = new connectTask();
        conctTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ip, port);
    }

    public TCPClient getClient() {
        return mTcpClient;
    }

    public ArrayList<PresetData> getPresets(){
        return Presets;
    }

    public void requestlist(DialogFragment d){

        mDialogFragment=d;

        AsyncTask mRequestPreset = new ReqestPresets();
        mRequestPreset.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);


    }




    private class ReqestPresets extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... d) {

            Log.d("LoadList","Run Asyncthread");
            DataRequestInProgress=true;
            mTcpClient.sendMessage("RequestList");

            while(DataRequestInProgress)
            {
                SystemClock.sleep(200);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void d) {

            if(ServerOK){
                ListFragment newFragment = new ListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.commit();

                mDialogFragment.dismiss();
            }else{
                ConnectDialogFragment myFragment = (ConnectDialogFragment) getFragmentManager().findFragmentByTag("ConnectDialogFrag");
                if (myFragment.getDialog() != null) {
                    myFragment.setUpdateDialog();
                    mTcpClient.stopClient();
                }

            }


        }

    }

    public class connectTask extends AsyncTask<String, String, TCPClient> {
        @Override
        protected TCPClient doInBackground(String... message) {

            mTcpClient = new TCPClient(message[0], message[1], new TCPClient.OnMessageReceived() {

                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    try {
                        //this method calls the onProgressUpdate
                        if (message != null) {
                        publishProgress(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mTcpClient.run();
            Log.d("Connection", "Started");

            return null;
        }

        public void newpreset(){
            Presets=new ArrayList<PresetData>();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            if(recordInput)
            {
                Presets.add(new PresetData(values[0],new CharacterDrawable(values[0].substring(0,1).charAt(0), 0xFF805781)));
            }


            if(UpdateConnectionStatus(values[0])==false){
                if(values[0].matches("StartRecords")){recordInput=true; newpreset();}
                if(values[0].matches("StopRecords")){recordInput=false; Presets.remove(Presets.size()-1); DataRequestInProgress=false;}
                Log.d("Return Message from Socket::::: >>>>> ", values[0]);
            }



        }

        @Override
        protected void onPostExecute(TCPClient client) {
            ServerStatus = false;
        }


    }

    public void stopClient(){
        SystemClock.sleep(100);
        mTcpClient.stopClient();
        mTcpClient = null;
        conctTask.cancel(true);
        conctTask = null;
    }

    @Override
    protected void onDestroy() {
        try {
            mTcpClient.closeConnection();
            mTcpClient = null;
            conctTask.cancel(true);
            conctTask = null;
        } catch (Exception e) {
            Log.e("OnDestroy Error:", e.toString());
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public boolean UpdateConnectionStatus(String status) {
        ConnectDialogFragment myFragment = (ConnectDialogFragment) getFragmentManager().findFragmentByTag("ConnectDialogFrag");
        boolean returnvalue=false;
        if(status==null){
            status="ServerError";
        }
        Log.d("ServerCommand", status);


        if(status.substring(0, Math.min(status.length(), 6)).matches("CCCRPS"))
        {
            if(status.split("S")[1].matches("0001")){

                ServerOK=true;
                returnvalue = true;
            }else{
                ServerOK=false;
            }
        }

        if (status.matches("ServerError")) {
            if (myFragment != null) {
                myFragment.setError();

                try {
                    mTcpClient.closeConnection();
                    mTcpClient = null;
                    conctTask.cancel(true);
                    conctTask = null;
                } catch (Exception e) {
                    Log.e("UpDateStatus to Error Error:", e.toString());
                    e.printStackTrace();
                }

            } else {

                ConnectFragment newFragment = new ConnectFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.commit();


                try {
                    mTcpClient.closeConnection();
                    mTcpClient = null;
                    conctTask.cancel(true);
                    conctTask = null;
                } catch (Exception e) {
                    Log.e("UpDateStatus to Error Error:", e.toString());
                    e.printStackTrace();
                }

            }
        }

        if (status.matches("ServerSucces")) {
            if (myFragment.getDialog() != null) {
                myFragment.setSuccess();
            }
            returnvalue = true;
        }
        return returnvalue;
    }
}