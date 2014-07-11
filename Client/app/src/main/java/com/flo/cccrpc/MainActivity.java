package com.flo.cccrpc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import org.json.JSONArray;

public class MainActivity extends Activity
{
	EditText editText;
	Button button;
	LinearLayout alertDialog;


	public void widget_init()
	{
		editText = (EditText) findViewById(R.id.input_IP);
		button = (Button) findViewById(R.id.shutBt);
	}

	public void saveIP(String IP)
	{
		SharedPreferences sp = getSharedPreferences("RememberIP",Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("IP", IP);
		editor.commit();
	}

	public String getIP()
	{
		SharedPreferences sp = getSharedPreferences("RememberIP",Activity.MODE_PRIVATE);
		return sp.getString("IP", null);
	}

    public String getAutoConnnect()
    {
        SharedPreferences sp = getSharedPreferences("AutoConnnect",Activity.MODE_PRIVATE);
        return sp.getString("AutoConnnect", null);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        widget_init();
        editText.setText(getIP());
        if (getIP() != null && getAutoConnnect() == "true") {


        }

        SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), "http://10.110.109.41:6666", new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception e, SocketIOClient socketIOClient) {

                if (e != null) {
                    e.printStackTrace();
                    return;
                }


                socketIOClient.emitEvent("message");


                socketIOClient.on("recievelistitem", new EventCallback() {
                    @Override
                    public void onEvent(JSONArray jsonArray, Acknowledge acknowledge){


                            Log.d("Socket.IO","Event: recievelstitem");


                    }
                });

            }
        });


    }



	public void onClick_connect(View view)
	{
        System.out.println("ConnectClicked");
        String Ip = editText.getText().toString();
        saveIP(Ip);

    }



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == 1)
		{
			new AlertDialog.Builder(this).setTitle("Instruction")
					.setMessage("BLABLA").setNegativeButton("OK", null)
					.show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 1, 1, "Instructions");
        menu.add(0, 1, 1, "About");
		return super.onCreateOptionsMenu(menu);
	}


    @Override
    protected void onDestroy()
    {
        try
        {
            System.out.println("onDestroy.");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }





}
