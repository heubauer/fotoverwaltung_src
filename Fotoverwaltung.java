package com.heubauer.fotoverwaltung;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Fotoverwaltung extends Activity
{
    private ListControl listCtrl;
    private LocationClass locationClass;
    private XmlParser parser;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        parser = new XmlParser(this);
        locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));
        
        listCtrl = new ListControl(this, (ListView)findViewById(R.id.headerList), (ListView)findViewById(R.id.pictureList));
        listCtrl.createList();
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String date = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
                        
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    File image = new File(getFilesDir() + "/Fotos", timeStamp + ".jpeg");
                    image.getParentFile().mkdirs();
                    FileOutputStream out = new FileOutputStream(image);
                    
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
                    out.flush();
                    out.close();
                    
                    parser.writeXml(new String[]{timeStamp + ".jpeg", "" + date, "" + locationClass.getCurrentLocation()});
                    locationClass.stopOnLocationChanged();
                    
                    HashMap<String, String> imageMap = new HashMap<String, String>();
                    imageMap.put("filename", timeStamp + ".jpeg");
                    imageMap.put("date", date);
                    listCtrl.updatePictureList(imageMap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void onShootAPictureClick(){
        CamClass cam = new CamClass();
        startActivityForResult(cam.startCam(), 1);

        locationClass.getCurrentLocation();
    }
    
    public String getTranslation(int id) {
        return getString(id);
    }
}