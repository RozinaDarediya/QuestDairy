package com.example.admin.questdairy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Admin on 8/21/2017.
 */

public class WebServiceCallingClass extends Activity {
    Context context;
    MyDelegate myDelegate;
    JSONObject jsonObject;
    String urlData;
    String msgOfProgressDialog;

    URL url = null;
    HttpURLConnection urlConnection;
    int code;

    JSONObject jsonObjectResponse;
    ProgressDialog progressDialog;

    public WebServiceCallingClass(Context context, MyDelegate myDeliget, String urlData, JSONObject jsonObject, String msgOfProgressDialog) {
        this.context = context;
        this.myDelegate = myDeliget;
        this.urlData = urlData;
        this.jsonObject = jsonObject;
        this.msgOfProgressDialog = msgOfProgressDialog;

        AsyncClass asyncClass = new AsyncClass();
        asyncClass.execute();
    }

    class AsyncClass extends AsyncTask<Object, Object, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(msgOfProgressDialog);
            progressDialog.show();
        }   //preExecute


        @Override
        protected JSONObject doInBackground(Object... params) {


            try {
                url = new URL("https://glycolytic-reconfig.000webhostapp.com/ProjectExpertAdvice/" + urlData);
            }catch (MalformedURLException e) {
                e.printStackTrace();
                Intent intent = new Intent(context,Exception_Display_act.class);
                intent.putExtra("msg","URL is not parsed correctly");
                startActivity(intent);
            }//catch of url


            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type:", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(30000);
                urlConnection.connect();

            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(jsonObject.toString().getBytes());
            } catch (IOException e1) {
                e1.printStackTrace();
                String c = "Error in sending required data...";
                Intent intent = new Intent(context,Exception_Display_act.class);
                intent.putExtra("msg",c);
                startActivity(intent);
            }
                try {
                    boolean res = urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
                } catch (IOException e) {
                    e.printStackTrace();
                    String a = "file not exits";
                    Intent intent = new Intent(context,Exception_Display_act.class);
                    intent.putExtra("msg","Specified page not found");
                    startActivity(intent);
                }
                code = urlConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                String c = "Connection time out";
                Intent intent = new Intent(context,Exception_Display_act.class);
                intent.putExtra("msg","Connection time out.. You have poor internet connection..");
                startActivity(intent);
            }
            StringBuilder builder = new StringBuilder();
            String s1 = "";
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((s1 = bufferedReader.readLine()) != null) {
                    builder.append(s1);
                    jsonObjectResponse = new JSONObject(builder.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObjectResponse;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            myDelegate.response(s,urlData);
        }
    }//myAsync class
}
