package com.heubauer.fotoverwaltung;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ImageActivity extends Activity {
    private ImageView imageView;
    private String filename;
    private File imageFile;

    /**
     * Wird aufgerufen, wenn die Activity das erste Mal erstellt wird.
     * @param savedInstanceState 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        
        filename = getIntent().getStringExtra("filename");
        
        imageView = (ImageView)findViewById(R.id.imageView);
        imageFile = new File(getFilesDir() + "/Fotos", filename);
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
        return true;
    }
    
    /**
     * Führt die KAtion durch, wenn ein Menüeintrag ausgewählt wurde
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
                    exportImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Share:
            case R.id.Map:
            default:
                Toast toast = Toast.makeText(getApplicationContext(), "Function not implemented (yet)", Toast.LENGTH_SHORT);
                toast.show();
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Exportiert das Bild in der Activity in den öffentlichen Ordner DCIM
     * @throws IOException 
     */
    private void exportImage() throws IOException {
        FileChannel in = new FileInputStream(imageFile.getAbsolutePath()).getChannel();
        
        File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Fotoverwaltung");
        File newImage = new File(publicDir, filename);
        
        if (! publicDir.exists())
            publicDir.mkdirs();

        
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
