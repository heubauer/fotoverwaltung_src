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
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fotoverwaltung extends Activity
{
    ListControl listCtrl;
    LocationClass locationClass;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listCtrl = new ListControl(this, (ListView)findViewById(R.id.headerList), (ListView)findViewById(R.id.pictureList));
        listCtrl.createPictureList();
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
        if (requestCode == 1) {
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String date = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
                XmlParser parser = new XmlParser(this);
                locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));
                        
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    File image = new File(getFilesDir() + "/Fotos", timeStamp + ".jpeg");
                    image.getParentFile().mkdirs();
                    FileOutputStream out = new FileOutputStream(image);
                    
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                    out.flush();
                    out.close();
                    
                    parser.writeXml(new String[]{timeStamp + ".jpeg", "" + date, "" + locationClass.getCurrentLocation()});
                                        
                    listCtrl.updatePictureList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void onShootAPictureClick(){

        //hiermit wird das Event onLocationChanged gestartet, sollte sich jemand nach dem Öffnen
        //der Kamera noch weit bewegen wird dadurch sicher gestellt, dass wir die aktuellste
        //Location beziehen.
        locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));

        CamClass cam = new CamClass();
        startActivityForResult(cam.startCam(), 1);

        //Testweise:
        Toast toasty = Toast.makeText(getApplicationContext(), ""+ locationClass.getCurrentLocation(), Toast.LENGTH_LONG);
        toasty.show();
        locationClass.stopOnLocationChanged();
        //Nach dem das Bild gespeichert wurde muss es an die BildClass weiter gegeben werden und dort muss
        //auch das onLactionChanged Event entfernt/gestoppt werden.
    }
    
    public String getTranslation(int id) {
        return getString(id);
    }
}