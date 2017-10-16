package com.example.admin.questdairy;

import android.app.Activity;
import android.content.Intent;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Admin on 7/23/2017.
 */

public class CommonURL{

    public URL getUrl(String str) {
        URL url = null;
        try {

            url = new URL("https://glycolytic-reconfig.000webhostapp.com/ProjectExpertAdvice/" + str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            /*Intent intent = new Intent(CommonURL.this,Exception_Display_act.class);
            intent.putExtra("msg","URL is not parsed correctly");
            startActivity(intent);*/
        }
        return url;
    }

    public HttpURLConnection getHttpURLConnection(URL url){

        HttpURLConnection urlConnection =null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type:","application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(30000);
            urlConnection.connect();


        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return urlConnection;
    }
}
