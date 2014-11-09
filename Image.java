package com.heubauer.fotoverwaltung;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Image {
    private String name;
    private String date;
    private String geoData;
    private String curPath;
    private final Context context;

    public Image(Context context) {
        this.context = context;
    }
    
    /**
     * Erstellt eine temporäre Datei, in der das Bild der Kamera gespeichert wird.
     * Auch werden Uhrzeit und Name schon eingetragen
     * @return File Bilddatei mit temporären Pfad
     * @throws IOException 
     */
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String formatedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(timeStamp, ".jpg", storageDir);
        name = timeStamp + ".jpg";
        date = formatedDate;
        geoData = "0,0";
        curPath = image.getAbsolutePath();
        
        return image;
    }
    
    /**
     * Kopiert ein Bild vom aktuellen Ort zu einem neuen.
     * Im Falle von private wird das alte auch gelöscht.
     * @param to String, der den Exportzweck enthält (gallerie, public und private)
     * @throws IOException 
     */
    public void exportImage(String to) throws IOException {
        curPath = (curPath != null)? curPath : context.getFilesDir() + "/Fotos/" + name;
        FileChannel in = new FileInputStream(curPath).getChannel();
        File oldImage = new File(curPath);
        File newImage = null;
        if ("galerie".equalsIgnoreCase(to)) {
            File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Fotoverwaltung");
            if (! publicDir.exists())
                publicDir.mkdirs();
            newImage = new File(publicDir, name);
        } else if("private".equalsIgnoreCase(to)){
            File privateDir = new File(context.getFilesDir() + "/Fotos");
            if (! privateDir.exists())
                privateDir.mkdirs();
            newImage = new File(privateDir, name);
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
            
            if("private".equalsIgnoreCase(to))
                if(oldImage.delete())
                    Log.i("Delete", "Datei erfolgreich aus Cache gelöscht.");
            curPath = newImage.getAbsolutePath();
            
            if("galerie".equalsIgnoreCase(to)) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newImage)));
                Toast galeryToast = Toast.makeText(context, "Export sucessful", Toast.LENGTH_SHORT);
                galeryToast.show();
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
    
    public String getGeoData() {
        return geoData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGeoData(String geoData) {
        this.geoData = geoData;
    }

    
}
