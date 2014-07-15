package cccrpc.flo.com.cccremotepresetclient;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.conn.util.InetAddressUtils;

/**
 * Created by Florian on 14.07.2014.
 */
public class ConnectDialogFragment extends DialogFragment {

    TextView lbl;
    ProgressBar prb;
    View view;
    String IP;
    String Port;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_connectdialog, null);
        setCancelable(false);
        getDialog().setTitle("Connecting");

        lbl = (TextView)view.findViewById(R.id.dialogtext);
        prb = (ProgressBar)view.findViewById(R.id.dialogprogress);

        Bundle mybundle = getArguments();
        IP = mybundle.getString("IP");
        Port = mybundle.getString("Port");

        lbl.setVisibility(View.INVISIBLE);

        if(InetAddressUtils.isIPv4Address(IP)||InetAddressUtils.isIPv6Address(IP)){
            ((MainActivity)getActivity()).connectToServer(IP,Port);



        }else{
            setError("Invalid IP");
            // Toast.makeText(getActivity().getApplicationContext(),"Invalid IP",Toast.LENGTH_LONG);
        }


        return view;
    }


    public void setError(){

        getDialog().setTitle("Error");
        lbl.setText("Server not reachable");
        lbl.setVisibility(View.VISIBLE);
        prb.setVisibility(View.GONE);
        setCancelable(true);

    }

    public void setError(String s){

        getDialog().setTitle("Error");
        lbl.setText(s);
        lbl.setVisibility(View.VISIBLE);
        prb.setVisibility(View.GONE);
        setCancelable(true);

    }

    public void setSuccess(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        prefs.edit().putString("IP", IP).commit();
        prefs.edit().putString("Port",Port).commit();

        ((MainActivity)getActivity()).requestlist(this);

    }

    public void setUpdateDialog(){

        getDialog().setTitle("Error");
        setCancelable(true);
        lbl.setText("Server Version to old please Update");
        lbl.setVisibility(View.VISIBLE);
        prb.setVisibility(View.GONE);

        ((MainActivity)getActivity()).getClient().closeConnection();


    }



}
