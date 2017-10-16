package com.example.admin.questdairy;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 8/25/2017.
 */

public class ImageUploadClass {

    ProgressDialog progressDialog;
    Context context;
    String msg;
    String img_nm;
    int id;
    JSONObject jsonObject1;
    Uri img_uri;

    public ImageUploadClass(Context context,String msg,String img_nm,int id,Uri img_uri) {
        this.context = context;
        this.msg = msg;
        this.img_nm = img_nm;
        this.id = id;
        this.img_uri = img_uri;

        Async async = new Async();
        async.execute();
    }

    class Async extends AsyncTask<Object, Object, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            String name= img_nm+".png";//image name
            String boundary="*****";
            String lineEnd="\r\n";
            String twoHyphens="--";
            int bytesAvailable,bytesRead,bufferSize;
            byte buffer[];
            int maxBufferSize=1*1024*1024;

            try {
                URL uri1 = new URL("https://glycolytic-reconfig.000webhostapp.com/ProjectExpertAdvice/image_upload.php?id="+id);
// a= inser id ;
                InputStream stream=context.getContentResolver().openInputStream(img_uri);
//uri is image
                HttpURLConnection urlConnection = (HttpURLConnection) uri1.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
                urlConnection.setRequestProperty("ENCTYPE","multipart/form-data");
                urlConnection.setRequestProperty("Connection","Keep-Alive");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(30000);
                urlConnection.connect();



                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.writeBytes(twoHyphens+boundary+lineEnd);
                dataOutputStream.writeBytes("Content-Disposition:form-data; name=\"fileUpload\";filename=\"" + name +"\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);


                bytesAvailable =stream.available();
                bufferSize= Math.min(bytesAvailable,maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = stream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = stream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = stream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.close();

                int c = urlConnection.getResponseCode();
                dataOutputStream.flush();
                dataOutputStream.close();
                if (c == 200) {
                    StringBuilder builder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String s1 = "";
                    while ((s1 = bufferedReader.readLine()) != null) {
                        builder.append(s1);
                    }

                    jsonObject1 = new JSONObject(builder.toString());
                    /*String msg = jsonObject1.getString("msg");

                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();*/
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return jsonObject1;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            String msg = null;
            try {
                msg = jsonObject1.getString("msg");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
