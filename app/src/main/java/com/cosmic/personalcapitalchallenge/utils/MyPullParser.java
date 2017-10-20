package com.cosmic.personalcapitalchallenge.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cosmic.personalcapitalchallenge.models.RssPCBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by anushree on 10/17/2017.
 * A class to parse the RSS feed
 */

public class MyPullParser {
    private String title = "title";
    private String description = "description";
    private String date = "pubDate";
    private String link = "link";
    private String image = "image";
    public volatile boolean parsingComplete = true;



    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return date;
    }

    public String getImage() {
        return image;
    }




    public ArrayList<RssPCBean> parseXml(XmlPullParser myparse) {

        int event;
        String text = null;
        RssPCBean bean = null;
        ArrayList<RssPCBean> mylist = new ArrayList<>();

        try {
            event = myparse.getEventType();
            int i=0;
            boolean insideItem = false; // This flag indicates inside the Item tag , so we can check other tags inside Item tag
            boolean afterEnclosure = false;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myparse.getName();
                switch (event) {

                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        if (name.equals("item")) {
                            insideItem = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = myparse.getText();
                        text = text.trim();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("item")) {
                            insideItem = false;
                            bean = new RssPCBean(getTitle(),getLink(),getDescription(),getImage(),getPubDate());
                            mylist.add(bean);

                        } else if (name.equals("title")) {
                            if (insideItem) {
                                title = text;
                            }
                        } else if (name.equals("link")) {
                            if (insideItem) {
                                link = text;
                            }
                        } else if (name.equals("description")) {
                            if (insideItem) {
                                description = text;
                            }
                        }
                        else if (name.equals("pubDate")) {
                            if (insideItem) {
                                date = text;

                            }
                        }
                        else if (name.equals("enclosure")) {
                            if (insideItem) {
                                // This flag indicates Content tag after enclosure tag since there are 2 content tags inside
                                // every RSS feed item
                                afterEnclosure = true;
                            }
                        }

                       else if (name.equals("content")) {
                            if (insideItem && afterEnclosure) {
                                image = myparse.getAttributeValue(0);
                                afterEnclosure = false;
                            }
                        }
                        break;

                    default: Log.i("Parser", "default");
                }

                event = myparse.next();
            }
            parsingComplete = false;

        }catch(XmlPullParserException e){
            Log.i("Parser", "XmlPullParserException");
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return mylist;

    }

}