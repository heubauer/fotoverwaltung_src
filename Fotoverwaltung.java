package com.heubauer.fotoverwaltung;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class Fotoverwaltung extends Activity
{
    SimpleAdapter headerAdapter, pictureAdapter;
    ArrayList<HashMap<String, String>> headerListText, pictureListContent;
    HashMap<String, String> headerMap, pictureMap;
    ListView headerList, pictureList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        String[] countries = { "India", "Pakistan", "China", "Bangladesh","Afghanistan",
                            "India", "Pakistan", "China", "Bangladesh","Afghanistan",
                            "India", "Pakistan", "China", "Bangladesh","Afghanistan",
                            "India", "Pakistan", "China", "Bangladesh","Afghanistan"};
        String[] capitals = { "New Delhi", "Islamabad", "Beijing", "Dhaka","Kabul",
                            "New Delhi", "Islamabad", "Beijing", "Dhaka","Kabul",
                            "New Delhi", "Islamabad", "Beijing", "Dhaka","Kabul",
                            "New Delhi", "Islamabad", "Beijing", "Dhaka","Kabul"};

        fillPictureList(countries, capitals);
    }

    private void fillPictureList(String[] filename, String[] date){
        headerListText = new ArrayList<HashMap<String, String>>();
        pictureListContent = new ArrayList<HashMap<String, String>>();
        
        headerList = (ListView) findViewById(R.id.headerList);
        pictureList = (ListView) findViewById(R.id.pictureList);

        headerMap = new HashMap<String, String>();
        headerMap.put("filename", getString(R.string.filename));
        headerMap.put("date", getString(R.string.date));
        headerListText.add(headerMap);

        headerAdapter = new SimpleAdapter(this, headerListText, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
        headerList.setAdapter(headerAdapter);
        
        for (int i = 0; i < filename.length; i++) {
            pictureMap = new HashMap<String, String>();

            pictureMap.put("filename", filename[i]);
            pictureMap.put("date", date[i]);
            pictureListContent.add(pictureMap);
        }        
        
        pictureAdapter = new SimpleAdapter(this, pictureListContent, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
        pictureList.setAdapter(pictureAdapter);
        pictureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //To see reaction.
                TextView filename = (TextView) findViewById(R.id.filename);
                Toast toast = Toast.makeText(getApplicationContext(), "Ausgewählt: ", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.Picy) {
            onShootAPictureClick();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onShootAPictureClick(){
        CamClass cam = new CamClass();
        startActivityForResult(cam.startCam(), 1);

        LocationClass locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));

        Toast toast = Toast.makeText(getApplicationContext(), "" +locationClass.getCurrentLocacion(), Toast.LENGTH_LONG);
        toast.show();
    }
}