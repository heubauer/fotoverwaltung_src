package com.heubauer.fotoverwaltung;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Marco
 */
public class ListControl {

    private SimpleAdapter headerAdapter, pictureAdapter;
    private ArrayList<HashMap<String, String>> headerListText, pictureListContent;
    private HashMap<String, String> headerMap, pictureMap;
    private final ListView headerList, pictureList;
    private final Context context;
    private final Intent imageView;
    private final XmlParser parser;
    
    /**
     * Der Listkontroller wird verwendet um eine Liste zu erstellen
     * @param context Der Context in dem die Liste aufgerufen wird
     * @param headerList Die ListView in der der Header erstellt wird
     * @param pictureList Die ListView in der die Bilderliste erstelt wird
     * @param parser Der Parser, der für die Bild-XML genutzt wird
     */
    public ListControl(Context context, ListView headerList, ListView pictureList, XmlParser parser) {
        //why two times? in createList
        headerListText = new ArrayList<HashMap<String, String>>();
        pictureListContent = new ArrayList<HashMap<String, String>>();

        this.parser = parser;

        imageView = new Intent(context, ImageActivity.class);
        this.headerList = headerList;
        this.pictureList = pictureList;
        this.context = context;
    }
    
    /**
     * Erstellt die Header- und Imageliste in der Hauptactivity
     */
    public void createList() {
        headerListText = new ArrayList<HashMap<String, String>>();
        pictureListContent = new ArrayList<HashMap<String, String>>();

        headerMap = new HashMap<String, String>();
        headerMap.put("filename", context.getString(R.string.filename));
        headerMap.put("date", context.getString(R.string.date));
        headerListText.add(headerMap);

        headerAdapter = new SimpleAdapter(context, headerListText, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
        headerList.setAdapter(headerAdapter);       

        try {createPictureList();}catch(Exception e){e.printStackTrace();}

        
        pictureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> entry = (HashMap<String, String>)pictureList.getItemAtPosition(position);
                
                imageView.putExtra("filename", entry.get("filename"));
                context.startActivity(imageView);
            }
        });
    }
    
    /**
     * Bestückt die Bilderliste mit Inhalt
     */
    private void createPictureList(){
        //XmlParser parser = new XmlParser(context);
        try {parser.parseXML();}
        catch(Exception e){e.printStackTrace();}
        
        ArrayList<Image> images = parser.getImageData();

        if (images != null) {
            for (Image image : images) {
                pictureMap = new HashMap<String, String>();
                pictureMap.put("filename", image.getName());
                pictureMap.put("date", image.getDate());
                pictureMap.put("geoData", image.getGeoData());
                pictureListContent.add(pictureMap);

                Log.i("Files", "Dateiname: " + image.getName());
            }

            pictureAdapter = new SimpleAdapter(context, pictureListContent, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
            pictureList.setAdapter(pictureAdapter);
        }
    }
    
    /**
     * Fügt ein neu geschossenes Bild der Bilderliste hinzu
     * @param image Das Bild, dass eingetragen werden soll
     */
    public void updatePictureList(Image image) {
        HashMap<String, String> imageMap = new HashMap<String, String>();
        imageMap.put("filename", image.getName());
        imageMap.put("date", image.getDate());
        imageMap.put("geoData", image.getGeoData());
        
        pictureListContent.add(imageMap);
        pictureAdapter.notifyDataSetChanged();
    }
}