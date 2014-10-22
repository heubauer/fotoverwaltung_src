package com.heubauer.fotoverwaltung;

import android.app.Activity;
<<<<<<< HEAD
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
=======
import android.location.LocationManager;
import android.os.Bundle;
>>>>>>> 0aae6f5b7a4aed997df89879f86e7cd08a5fa8de
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
<<<<<<< HEAD
import java.util.Date;
=======
>>>>>>> 0aae6f5b7a4aed997df89879f86e7cd08a5fa8de

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
        if (id == R.id.Picy) {
            onShootAPictureClick();
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
                if (bitmap1 != null) {
                    File image = new File(getDir("Fotos", Context.MODE_PRIVATE), timeStamp + ".jpeg");
                    FileOutputStream fOut = new FileOutputStream(image);
                    
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                    Log.i("", ""+image);
                }
            } catch (Exception e) {
                
            }
        }
    }

<<<<<<< HEAD
    private void onClickyClick(){
=======
    public void onShootAPictureClick(){
>>>>>>> 0aae6f5b7a4aed997df89879f86e7cd08a5fa8de
        CamClass cam = new CamClass();
        startActivityForResult(cam.startCam(), 1);

        LocationClass locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));

        Toast toast = Toast.makeText(getApplicationContext(), "" +locationClass.getCurrentLocacion(), Toast.LENGTH_LONG);
        toast.show();
    }
}