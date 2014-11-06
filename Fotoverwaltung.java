package com.heubauer.fotoverwaltung;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import java.io.File;
import java.io.IOException;

public class Fotoverwaltung extends Activity
{
    static final int REQUEST_TAKE_PHOTO = 1;

    private ListControl listCtrl;
    private LocationClass locationClass;
    private XmlParser parser;
    private Image curImage;
    
    /**
     * Wird aufgerufen, wenn die Activity das erste Mal erstellt wird.
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        parser = new XmlParser(this);

        //wenn wir es schon hier aufrufen weiß ich nicht ob das sinnvoll ist. Habe es deswegen
        // jetzt mal auskommentiert.
        //locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));
        
        listCtrl = new ListControl(this, (ListView)findViewById(R.id.headerList),
                (ListView)findViewById(R.id.pictureList), parser);
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
     * Speichert die Location in der XML und fügt das Bild der XML-Datei hinzu, um es dann anshließend
     * in der Übersicht anzuzeigen.
     * @param requestCode Code der Activity, die diese Funktion aufruft
     * @param resultCode Code, der einen Status enthält
     * @param data Daten die zurückgegeben werden von der Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                try {curImage.exportImage("private");} catch(IOException e){e.printStackTrace();}
                curImage.setGeoData(locationClass.getCurrentLocation().toString());
                parser.writeXml(curImage);
                listCtrl.updatePictureList(curImage);
        }
        locationClass.stopOnLocationChanged();
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
        
        Intent takePictureIntent = cam.startCam();
        curImage = new Image(this);
        
        File photoFile = null;
        try {
            photoFile = curImage.createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }
}