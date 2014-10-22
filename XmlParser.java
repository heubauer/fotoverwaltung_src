package com.heubauer.fotoverwaltung;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

/**
 *
 * @author Marco
 */
class Image {
    public String name;
    public String date;
    public String geoData;
}

public class XmlParser {
    private ArrayList<Image> images;
    private final Context context;
    
    public XmlParser(Context context) {
        images = null;
        this.context = context;        
        
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getInputStream();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

        } catch (XmlPullParserException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    private InputStream getInputStream() throws IOException{
        try {
            InputStream in_s = context.getAssets().open("imageData.xml");
            return in_s;
        } catch (FileNotFoundException e) {
            Log.i("", "komme hier rein");
            File xmlFile = new File(context.getFilesDir(), "imageData.xml");
            xmlFile.createNewFile();
                
            FileWriter fstream = new FileWriter(xmlFile, true);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.newLine();

            out.close();
            
            InputStream in_s = context.getAssets().open("imageData.xml");
            return in_s;
        }
    }
    
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException {
        int eventType = parser.getEventType();
        Image currentImg = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                        images = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name == "image"){
                        currentImg = new Image();
                    } else if (currentImg != null){
                        if (name == "filename"){
                            currentImg.name = parser.nextText();
                        } else if (name == "date"){
                            currentImg.date = parser.nextText();
                        } else if (name == "geoData"){
                            currentImg.geoData= parser.nextText();
                        }  
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("product") && currentImg != null){
                        images.add(currentImg);
                    } 
            }
            eventType = parser.next();
        }
    }
    
    /**
     * returns images in XML-file
     * @return ArrayList<Image>
     */
    public ArrayList<Image> getImageData() {
        return images;
    }
    
    public String writeXml(String[] imgData){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "image");
            
            serializer.startTag("", "filename");
            serializer.text(imgData[0]);
            serializer.endTag("", "filename");
            serializer.startTag("", "date");
            serializer.text(imgData[1]);
            serializer.endTag("", "date");
            serializer.startTag("", "geoData");
            serializer.text(imgData[2]);
            serializer.endTag("", "geoData");
            
            serializer.endTag("", "image");
            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }
}
