package cccrpc.flo.com.cccremotepresetclient;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Florian on 11.07.2014.
 */



public class ListFragment extends Fragment {

    ArrayList<PresetData> Presets;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        setHasOptionsMenu(true);

        ListView PresetListView=(ListView) rootView.findViewById(R.id.list);

        Presets=((MainActivity)getActivity()).getPresets();
        if(Presets.isEmpty()){
            Presets.add(new PresetData("!No Presets Found!",null));
        }
        PresetListAdapter mAdapter = new PresetListAdapter(getActivity().getApplicationContext(),R.layout.list_item,Presets);
        PresetListView.setAdapter(mAdapter);

        // register onClickListener to handle click events on each item
        PresetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String SelectedPreset=Presets.get(position).getName();
                ((MainActivity)getActivity()).getClient().sendMessage(SelectedPreset);
                //Toast.makeText(getActivity().getApplicationContext(), "Preset Selected: " + SelectedPreset, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.connect_disconnect, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_Disconnect) {

            ((MainActivity)getActivity()).getClient().closeConnection();
            ConnectFragment newFragment = new ConnectFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, newFragment);
            transaction.commit();
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            prefs.edit().putBoolean("Autoconnect", false).commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

class PresetData{
    public String Name;
    public CharacterDrawable Char;

    PresetData(String N,CharacterDrawable C){
        Name=N;
        Char=C;
    }

    public String getName(){
        return Name;
    }
    public CharacterDrawable getChar(){
        return Char;
    }

    public void setName(String n){
        Name=n;
    }
    public void setChar(CharacterDrawable c){
        Char=c;
    }
}


class PresetListAdapter extends ArrayAdapter<PresetData> {

    // declaring our ArrayList of items
    private ArrayList<PresetData> objects;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public PresetListAdapter(Context context, int textViewResourceId, ArrayList<PresetData> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        PresetData i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView text = (TextView) v.findViewById(R.id.rowtext);
            ImageView img = (ImageView) v.findViewById(R.id.rowimg);

            if (text != null){
                text.setText(i.getName());
            }
            if (img != null){
                img.setImageDrawable(i.getChar());
            }

        }

        // the view must be returned to our activity
        return v;

    }

}


class CharacterDrawable extends ColorDrawable {

    private final char character;
    private final Paint textPaint;
    private final Paint borderPaint;
    private static final int STROKE_WIDTH = 10;
    private static final float SHADE_FACTOR = 0.9f;

    public CharacterDrawable(char character, int color) {
        super(color);
        this.character = character;
        this.textPaint = new Paint();
        this.borderPaint = new Paint();

        // text paint settings
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // border paint settings
        borderPaint.setColor(getDarkerShade(color));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(STROKE_WIDTH);
    }

    private int getDarkerShade(int color) {
        return Color.rgb((int) (SHADE_FACTOR * Color.red(color)),
                (int) (SHADE_FACTOR * Color.green(color)),
                (int) (SHADE_FACTOR * Color.blue(color)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // draw border
        canvas.drawRect(getBounds(), borderPaint);

        // draw text
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        textPaint.setTextSize(height / 2);
        canvas.drawText(String.valueOf(character), width/2, height/2 - ((textPaint.descent() + textPaint.ascent()) / 2) , textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}