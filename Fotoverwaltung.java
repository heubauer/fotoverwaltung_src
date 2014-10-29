package com.heubauer.fotoverwaltung;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
    
    /**
     * Wird aufgerufen, wenn die Activity das erste Mal erstellt wird.
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        parser = new XmlParser(this);

        //wenn wir es schon hier aufrufen weiß ich nicht ob das sinnvoll ist. Habe es deswegen
        // jetzt mal auskommentiert.
        //locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));
        
        listCtrl = new ListControl(this, (ListView)findViewById(R.id.headerList),
                (ListView)findViewById(R.id.pictureList));
        listCtrl.createList();
    }

    /**
     * Erstellt ein Optionsmenü mit dem Template menu/main.xml
     * @param menu
     * @return Boolean Gibt zurück, ob Menü erfolgreich erstellt wurde
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Führt die Aktion durch, wenn ein Menüeintrag ausgewählt wurde
     * @param item Ein Menüitem
     * @return Boolean Gibt das geklickte Item zurück
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Camera) {
            onShootAPictureClick();
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Handelt die Rückgabeparameter von fremden Activities (in diesem Falle nur von der Camera)
     * @param requestCode Code der Activity, die diese Funktion aufruft
     * @param resultCode Code, der einen Status enthält
     * @param data Daten die zurückgegeben werden von der Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                //for filename
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                //for date in Hashmap
                String date = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());

                String location = locationClass.getCurrentLocation().toString();

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    File image = new File(getFilesDir() + "/Fotos", timeStamp + ".jpeg");
                    image.getParentFile().mkdirs();
                    FileOutputStream out = new FileOutputStream(image);

                    //müssen wir compress aufrufen?
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
                    out.flush();
                    out.close();

                    parser.writeXml(new String[]{timeStamp + ".jpeg", "" + date,
                            location});
                    locationClass.stopOnLocationChanged();

                    HashMap<String, String> imageMap = new HashMap<String, String>();
                    imageMap.put("filename", timeStamp + ".jpeg");
                    imageMap.put("date", date);
                    imageMap.put("geoData", location);
                    listCtrl.updatePictureList(imageMap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Aktion, die ausgeführt wird, wenn "Take a picture" ausgewählt wird
     */
    public void onShootAPictureClick(){
        CamClass cam = new CamClass();

        if (locationClass == null) {
            locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));
        }
        else{
            locationClass.startOnLocationChanged();
        }

        startActivityForResult(cam.startCam(), 1);
    }
}