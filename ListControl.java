package com.heubauer.fotoverwaltung;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
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
    
    public ListControl(Context context, ListView headerList, ListView pictureList) {
        headerListText = new ArrayList<HashMap<String, String>>();
        pictureListContent = new ArrayList<HashMap<String, String>>();
        
        this.headerList = headerList;
        this.pictureList = pictureList;
        this.context = context;
    }
    
    public void createPictureList(){
        headerListText = new ArrayList<HashMap<String, String>>();
        pictureListContent = new ArrayList<HashMap<String, String>>();

        headerMap = new HashMap<String, String>();
        headerMap.put("filename", context.getString(R.string.filename));
        headerMap.put("date", context.getString(R.string.date));
        headerListText.add(headerMap);

        headerAdapter = new SimpleAdapter(context, headerListText, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
        headerList.setAdapter(headerAdapter);       

        updatePictureList();
        
        pictureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> entry = (HashMap<String, String>)pictureList.getItemAtPosition(position);
                
                Toast toast = Toast.makeText(context, "Ausgewählt: " + entry.get("filename"), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
    
    public void updatePictureList() {
        XmlParser parser = new XmlParser(context);
        ArrayList<Image> images = parser.getImageData();
        
        if (images != null) {
            pictureMap = new HashMap<String, String>();
            
            for (Image image : images) {
                pictureMap.put("filename", image.name);
                pictureMap.put("date", image.date);
                pictureListContent.add(pictureMap);                
                
                Log.i("Files", "Dateiname: " + image.name);
            }
            
            pictureAdapter = new SimpleAdapter(context, pictureListContent, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
            pictureList.setAdapter(pictureAdapter);
        }
    }
}