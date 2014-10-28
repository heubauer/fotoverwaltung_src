package com.heubauer.fotoverwaltung;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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

class Image {
    public String name;
    public String date;
    public String geoData;
}

public class XmlParser {
    private ArrayList<Image> images;
    private final Context context;
    private final File xmlFile;
    
    /**
     * Erstellt einen XML-Parser um mit der imageData.xml zu interagieren
     * @param context Context in dem der Parser aufgerufen wird
     */
    public XmlParser(Context context) {
        images = null;
        xmlFile = new File(context.getFilesDir(), "imageData.xml");
        this.context = context;
        
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            
            if(!xmlFile.exists()) {                
                xmlFile.createNewFile();
                
                FileWriter fstream = new FileWriter(xmlFile.getAbsolutePath(), true);
                BufferedWriter out = new BufferedWriter(fstream);

                out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                out.newLine();

                out.close();
            }
            
            InputStream in_s = new FileInputStream(xmlFile);

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Parst die XML-Datei in ein Array mit Objekten des Typs Image
     * @param parser XMLPullParser, der verwendet werden soll 
     * @throws XmlPullParserException
     * @throws IOException 
     */
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException {
        int eventType = parser.getEventType();
        Image currentImg = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                        images = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if ("image".equals(name)){
                        currentImg = new Image();
                    } else if (currentImg != null){
                        if ("filename".equals(name)){
                            currentImg.name = parser.nextText();
                        } else if ("date".equals(name)){
                            currentImg.date = parser.nextText();
                        } else if ("geoData".equals(name)){
                            currentImg.geoData= parser.nextText();
                        }  
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("image") && currentImg != null){
                        images.add(currentImg);
                    } 
            }
            eventType = parser.next();
        }
    }
    
    /**
     * Gibt die in der XML-Datei gespeicherten Bilder zurück
     * @return ArrayList<Image>
     */
    public ArrayList<Image> getImageData() {
        return images;
    }
    
    /**
     * Parst die Bilddaten in ein XML. Benötigt werden Dateiname, Datum und Geodaten
     * @param imgData Ein String-Array mit den Bilddaten
     * @throws RuntimeException
     */
    public void writeXml(String[] imgData){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
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
            writeXmlToFile(writer.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }
    
    /**
     * Schreibt einen XML-String in die XML-Datei
     * @param xmlString String, der eingetragen werden soll in die Datei
     * @throws IOException 
     */
    private void writeXmlToFile (String xmlString) throws IOException{     
        FileWriter fstream = new FileWriter(xmlFile.getAbsolutePath(), true);
        BufferedWriter out = new BufferedWriter(fstream);

        out.write(xmlString);
        out.newLine();

        out.close();
    }
}
