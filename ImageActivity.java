package com.heubauer.fotoverwaltung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class ImageActivity extends Activity {
    private ImageView imageView;
    private Image curImage;
    private XmlParser parser;
    private File imageFile;
    private String latLng;
    private ShareActionProvider shareProvider;

    /**
     * Wird aufgerufen, wenn die Activity das erste Mal erstellt wird.
     * @param savedInstanceState 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        latLng = getIntent().getStringExtra("geoData");
        parser = new XmlParser(this);
        try{parser.parseXML();}catch(Exception e){e.printStackTrace();}
        for(Image img : parser.getImageData()) {
            if(img.name.equals(getIntent().getStringExtra("filename")))
                curImage = img;                
        }
        imageView = (ImageView)findViewById(R.id.imageView);
        imageFile = new File(getFilesDir() + "/Fotos", curImage.name);
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageView.setImageBitmap(bitmap);
    }
    
    /**
     * Erstellt ein Optionsmenü mit dem Template menu/main.xml
     * @param menu
     * @return Boolean Gibt zurück, ob Menü erfolgreich erstellt wurde
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.image, menu);
        
        MenuItem item = menu.findItem(R.id.Share);

        shareProvider = (ShareActionProvider) item.getActionProvider();

        return true;
    }
    
    /**
     * Führt die Aktion durch, wenn ein Menüeintrag ausgewählt wurde
     * @param item Ein Menüitem
     * @return Boolean Gibt das geklickte Item zurück
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        switch(id) {
            case R.id.Export:
                try {
                    exportImage(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Share:
                doShare();
                break;
            case 4: //Alle Teilen abfangen.
                break;
            case R.id.Map:
                Intent maps = new Intent(this, MapsActivity.class);
                maps.putExtra("geoData", latLng);
                startActivity(maps);
                break;
            default:
                Toast toast = Toast.makeText(getApplicationContext(), "Function not implemented (yet)", Toast.LENGTH_SHORT);
                toast.show();
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Teilt das gerade angezeigte Bild
     */
    private void doShare() {
        if (shareProvider != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            
            //Holt das Bild aus der ImageView, konvertiert es und packt es in den Mediastore, damit andere Apps es dort rausholen können
            Drawable drawable = imageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
            
            shareIntent.setType("image/*");
            Uri uri = Uri.parse(path);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareProvider.setShareIntent(shareIntent);
        }
    }
    
    /**
     * Exportiert das Bild in der Activity in den öffentlichen Ordner DCIM
     * @throws IOException 
     */
    private void exportImage(boolean toExternal) throws IOException {
        FileChannel in = new FileInputStream(imageFile.getAbsolutePath()).getChannel();
        File newImage = null;
        if (toExternal) {
            File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Fotoverwaltung");
            if (! publicDir.exists())
                publicDir.mkdirs();
            newImage = new File(publicDir, curImage.name);
        } else {
             newImage = File.createTempFile("fotoverwaltung", curImage.name, getExternalCacheDir());
        }
        
        FileChannel out = new FileOutputStream(newImage.getAbsolutePath()).getChannel();
        try
        {
            in.transferTo(0, in.size(), out);
        }
        finally
        {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }
}
