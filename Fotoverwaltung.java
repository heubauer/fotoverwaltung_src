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
                HashMap<String, String> entry = (HashMap<String, String>)pictureList.getItemAtPosition(position);
                TextView filename = (TextView) findViewById(R.id.filename);
                Toast toast = Toast.makeText(getApplicationContext(), "Ausgewählt: " +  
                        entry.get("filename"), Toast.LENGTH_SHORT);
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
        if (id == R.id.Camera) {
            onShootAPictureClick();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onShootAPictureClick(){

        //hiermit wird das Event onLocationChanged gestartet, sollte sich jemand nach dem Öffnen
        //der Kamera noch weit bewegen wird dadurch sicher gestellt, dass wir die aktuellste
        //Location beziehen.
        LocationClass locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));

        CamClass cam = new CamClass();
        startActivityForResult(cam.startCam(), 1);

        //Testweise:
        Toast toasty = Toast.makeText(getApplicationContext(), ""+ locationClass.getCurrentLocacion(), Toast.LENGTH_LONG);
        toasty.show();
        locationClass.stopOnLocationChanged();
        //Nach dem das Bild gespeichert wurde muss es an die BildClass weiter gegeben werden und dort muss
        //auch das onLactionChanged Event entfernt/gestoppt werden.
    }
}