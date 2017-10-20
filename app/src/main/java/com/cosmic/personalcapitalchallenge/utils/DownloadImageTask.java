package com.cosmic.personalcapitalchallenge.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.os.AsyncTask;

import com.cosmic.personalcapitalchallenge.R;

import java.io.*;

/**
 * Created by anushree on 10/17/2017.
 * Util class to download the image for every Rss feed from the image link of the feed
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
    }

    @Override
    protected void onPreExecute() {
        bmImage.setImageResource(R.drawable.loading);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap myImage = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            myImage = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return myImage;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
