package cccrpc.flo.com.cccremotepresetclient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.apache.http.conn.util.InetAddressUtils;

/**
 * Created by Florian on 11.07.2014.
 */
public class ConnectFragment extends Fragment implements View.OnClickListener{

    public ConnectFragment() {
    }

    Button mButton;
    EditText mTextView;
    CheckBox mCheckBox;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);
        setHasOptionsMenu(true);

        mButton = (Button) rootView.findViewById(R.id.connectButton);
        mButton.setOnClickListener(this);

        mTextView = (EditText) rootView.findViewById(R.id.IPeditText);
        mCheckBox = (CheckBox) rootView.findViewById(R.id.AutoConnectcheckBox);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mTextView.setText(prefs.getString("IP",""));
        mCheckBox.setChecked(prefs.getBoolean("Autoconnect",false));

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    prefs.edit().putBoolean("Autoconnect", isChecked).commit();

            }
        });

        if(prefs.getBoolean("Autoconnect",false)&&(InetAddressUtils.isIPv4Address(mTextView.getText().toString())||InetAddressUtils.isIPv6Address(mTextView.getText().toString()))){
            FragmentManager manager=getFragmentManager();
            ConnectDialogFragment Dialog = new ConnectDialogFragment();

            Bundle data = new Bundle();
            data.putString("IP",mTextView.getText().toString());
            data.putString("Port","34762");
            Dialog.setArguments(data);

            Dialog.show(manager, "ConnectDialogFrag");


            prefs.edit().putBoolean("Autoconnect",mCheckBox.isChecked()).commit();
        }


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.connect, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_Disconnect) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == mButton) {

            FragmentManager manager=getFragmentManager();
            ConnectDialogFragment Dialog = new ConnectDialogFragment();

            Bundle data = new Bundle();
            data.putString("IP",mTextView.getText().toString());
            data.putString("Port","34762");
            Dialog.setArguments(data);

            Dialog.show(manager, "ConnectDialogFrag");


            SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
            prefs.edit().putBoolean("Autoconnect", mCheckBox.isChecked()).commit();


        }
    }


}
