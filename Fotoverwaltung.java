package com.heubauer.fotoverwaltung;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Fotoverwaltung extends Activity
{
    ArrayAdapter<String> listAdapter;
    ListView pictureList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        pictureList = (ListView) findViewById(R.id.pictureList);
        
        listAdapter = new ArrayAdapter<String>(this, R.id.pictureList);
        pictureList.setAdapter(listAdapter);
        pictureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });
    
    }
    
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }
    
    
}
