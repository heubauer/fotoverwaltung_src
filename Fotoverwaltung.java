package com.heubauer.fotoverwaltung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fotoverwaltung extends Activity
{
    ListControl listCtrl;
    
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
                    File image = new File(getFilesDir() + "/Fotos", timeStamp + ".jpeg");
                    FileOutputStream fOut = new FileOutputStream(image);
                    
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                    
                    Log.i("newFile", "" + image);
                    
                    listCtrl.updatePictureList();
                }
            } catch (Exception e) {
                
            }
        }
    }
    
    public void onShootAPictureClick(){
        CamClass cam = new CamClass();
        startActivityForResult(cam.startCam(), 1);

        LocationClass locationClass = new LocationClass((LocationManager)getSystemService(LOCATION_SERVICE));

        Toast toast = Toast.makeText(getApplicationContext(), "" +locationClass.getCurrentLocacion(), Toast.LENGTH_LONG);
        toast.show();
    }
    
    public String getTranslation(int id) {
        return getString(id);
    }
}