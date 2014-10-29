package com.heubauer.fotoverwaltung;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
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
    
    public ListControl(Context context, ListView headerList, ListView pictureList) {
        //why two times? in createList
        headerListText = new ArrayList<HashMap<String, String>>();
        pictureListContent = new ArrayList<HashMap<String, String>>();

        imageView = new Intent(context, ImageActivity.class);
        this.headerList = headerList;
        this.pictureList = pictureList;
        this.context = context;
    }
    
    public void createList(){
        //why again?
        headerListText = new ArrayList<HashMap<String, String>>();
        pictureListContent = new ArrayList<HashMap<String, String>>();

        headerMap = new HashMap<String, String>();
        headerMap.put("filename", context.getString(R.string.filename));
        headerMap.put("date", context.getString(R.string.date));
        headerListText.add(headerMap);

        headerAdapter = new SimpleAdapter(context, headerListText, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
        headerList.setAdapter(headerAdapter);       

        createPictureList();
        
        pictureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> entry = (HashMap<String, String>)pictureList.getItemAtPosition(position);
                
                imageView.putExtra("filename", entry.get("filename"));
                imageView.putExtra("location", entry.get("geoData"));
                context.startActivity(imageView);
            }
        });
    }
    
    private void createPictureList() {
        XmlParser parser = new XmlParser(context);
        ArrayList<Image> images = parser.getImageData();
        
        if (images != null) {            
            for (Image image : images) {
                pictureMap = new HashMap<String, String>();
                pictureMap.put("filename", image.name);
                pictureMap.put("date", image.date);
                pictureMap.put("geoData", image.geoData);
                pictureListContent.add(pictureMap);                
                
                Log.i("Files", "Dateiname: " + image.name);
            }
            
            pictureAdapter = new SimpleAdapter(context, pictureListContent, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
            pictureList.setAdapter(pictureAdapter);
        }
    }
    
    public void updatePictureList(HashMap<String, String> image) {
        pictureListContent.add(image);
        pictureAdapter.notifyDataSetChanged();
    }
}