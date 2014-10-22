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
        
//        for (int i = 0; i < filename.length; i++) {
//            pictureMap = new HashMap<String, String>();
//
//            pictureMap.put("filename", filename[i]);
//            pictureMap.put("date", date[i]);
//            pictureListContent.add(pictureMap);
//        }
        
        updatePictureList();
        
        pictureAdapter = new SimpleAdapter(context, pictureListContent, R.layout.row, new String[]{"filename", "date"}, new int[]{R.id.filename, R.id.date});
        pictureList.setAdapter(pictureAdapter);
        pictureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //To see reaction.
                HashMap<String, String> entry = (HashMap<String, String>)pictureList.getItemAtPosition(position);
                
                Toast toast = Toast.makeText(context, "Ausgewählt: " + entry.get("filename"), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
    
    public void updatePictureList() {
        File dir = new File(context.getFilesDir() + "/Fotos");
        String[] files = dir.list();
            
        Log.i("status", "still working: " + files);
        
        if (files != null)
            for (String name : files) {
                Log.i("Files", "Dateiname: " + name);
            }
    }
}