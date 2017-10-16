package com.example.admin.questdairy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Expert_Registration_Part2 extends AppCompatActivity {

    JSONArray jsonArray;
    ArrayList<String> category_sapinner ;
    LinearLayout layout;
    Spinner spinner;
    EditText expert_add1, expert_add2, expert_area, expert_city, expert_pincode;
    Button btnreg, btnclr;
    ProgressDialog progressDialog;
    TextInputLayout text;
    JSONObject jsonObject1;
    String nm, lnm, cnt, deree, email, pass,img;
    String add1, add2, area, city, pincode,category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert__registration__part2);

        Intent intent = getIntent();
        nm = intent.getStringExtra("first_name");
        lnm = intent.getStringExtra("last_name");
        cnt = intent.getStringExtra("contact");
        deree = intent.getStringExtra("degree");
        email = intent.getStringExtra("email");
        pass = intent.getStringExtra("password");
        img = intent.getStringExtra("certificate");

        init();

        MyAsyncCategory async = new MyAsyncCategory();
        async.execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//spinner item selected

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values();
                if(add1.isEmpty() || add2.isEmpty() || area.isEmpty() || city.isEmpty() || pincode.isEmpty()){
                    validation();
                }
                else{
                    MyAsync async = new MyAsync();
                    async.execute();
                }
            }
        });//btnreg

        btnclr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expert_add1.setText("");
                expert_add2.setText("");
                expert_area.setText("");
                expert_city.setText("");
                expert_pincode.setText("");
            }
        });//btnclr

    }//oncreate
//VALIDATION FUNCTION
    private void validation() {
        if(add1.isEmpty()){
            expert_add1.setError("This field can not be empty");
        }
        if(add2.isEmpty()){
            expert_add2.setError("This field can not be empty");
        }
        if(area.isEmpty()){
            expert_area.setError("This field can not be empty");
        }
        if(city.isEmpty()){
            expert_city.setError("This field can not be empty");
        }
        if(pincode.isEmpty()){
            expert_pincode.setError("This field can not be empty");
        }
        if(category.equals("Choose your category")){
            text.setErrorEnabled(true);
            text.setError("You must choose one of the category");
        }
    }//validation

    private void values() {
        add1 = expert_add1.getText().toString().trim();
        add2 = expert_add2.getText().toString().trim();
        area = expert_area.getText().toString().trim();
        city = expert_city.getText().toString().trim();
        pincode = expert_pincode.getText().toString().trim();
        //category = spinner.getText().toString().trim()
    }//values

    private void init() {
        category_sapinner = new ArrayList<>();
        category_sapinner.add("Choose your category");

        expert_add1 = (EditText)findViewById(R.id.expert_add1);
        expert_add2 = (EditText)findViewById(R.id.expert_add2);
        expert_area = (EditText)findViewById(R.id.expert_area);
        expert_city = (EditText)findViewById(R.id.expert_city);
        expert_pincode = (EditText)findViewById(R.id.expert_pincode);
        spinner = (Spinner)findViewById(R.id.spinner);
        btnreg = (Button)findViewById(R.id.btnreg);
        btnclr = (Button)findViewById(R.id.btnclr);
         text = (TextInputLayout)findViewById(R.id.text);
        layout = (LinearLayout)findViewById(R.id.layout);

    }//init

// THIS WS WILL SAVE DATA IN DB**************
    class MyAsync extends AsyncTask<Object, Object, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Expert_Registration_Part2.this);
            progressDialog.setMessage("waiting....");
            progressDialog.show();
        }//onPreExecute

        @Override
        protected JSONObject doInBackground(Object... params) {

            CommonURL commonURL = new CommonURL();
            URL url = commonURL.getUrl("ExpertRegWS.php");
            HttpURLConnection urlConnection = commonURL.getHttpURLConnection(url);

            /*String nm, lnm, cnt, deree, email, pass,img;
            String add1, add2, area, city, pincode,category;*/
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("first_name", nm);
                jsonObject.put("last_name", lnm);
                jsonObject.put("contact_number", cnt);
                jsonObject.put("degree", deree);
                jsonObject.put("address_line1", add1);
                jsonObject.put("address_line2", add2);
                jsonObject.put("area", area);
                jsonObject.put("city", city);
                jsonObject.put("pincode", pincode);
                jsonObject.put("category_name",category);
                jsonObject.put("status","null");


                jsonObject.put("username", email);
                jsonObject.put("password", pass);
                jsonObject.put("type","expert");

                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(jsonObject.toString().getBytes());

                int code = urlConnection.getResponseCode();
                if(code==200){
                    StringBuilder builder=new StringBuilder();
                    String s1="";
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    while ((s1=bufferedReader.readLine())!=null)
                    {
                        builder.append(s1);

                    }
                    jsonObject1=new JSONObject(builder.toString());

                }

                }
                 catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonObject1;
        }//doInBackground

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            progressDialog.dismiss();
            try {
                String res = jsonObject.getString("response");
               // String res1 = jsonObject.getString("response1");

                if(res.equals("successful")){
                    int id= jsonObject.getInt("id");
                    Snackbar snackbar = Snackbar.make(layout,"Successfully registered...",Snackbar.LENGTH_LONG);
                    snackbar.show();

                    CommonSharedPreferenceClass.setIntValue(getApplicationContext(),"userid",id);
                    CommonSharedPreferenceClass.setValue(getApplicationContext(),"f_nm",nm);
                    CommonSharedPreferenceClass.setValue(getApplicationContext(),"l_nm",lnm);
                    CommonSharedPreferenceClass.setValue(getApplicationContext(),"email",email);

                    /*Intent intent = new Intent(getApplication(),ExpertHomePage.class);
                    startActivity(intent);*/

                    Intent intent = new Intent(Expert_Registration_Part2.this,ExpertRegistrationRemaining.class);
                    startActivity(intent);

                }
                else {
                    Snackbar snackbar = Snackbar.make(layout,"Something went wrong. Registration failed...",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//onPostExecute
    }//myasync
// THIS WS WILL DISPLAY CATEGORY FROM DB ***************************
    private class MyAsyncCategory extends AsyncTask<Object, Object, JSONArray>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Expert_Registration_Part2.this);
            progressDialog.setMessage("fatching data....");
            progressDialog.show();
        }// onPreExecute

        @Override
        protected JSONArray doInBackground(Object... params) {
            CommonURL commonURL = new CommonURL();
            URL url = commonURL.getUrl("DisplayCategory.php");
            HttpURLConnection urlConnection = commonURL.getHttpURLConnection(url);


            try {

                StringBuilder builder=new StringBuilder();
                String s1="";
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((s1=bufferedReader.readLine())!=null)
                {
                    builder.append(s1);

                }
                jsonArray = new JSONArray(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonArray;
        }// doInBackground...

        @Override
        protected void onPostExecute(JSONArray s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
                try {
                    for(int i=0 ;i<s.length() ; i++) {
                        JSONObject jsonObject1 = s.getJSONObject(i);
                        category_sapinner.add(jsonObject1.getString("cat_name"));
                    }//for
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,category_sapinner);
                spinner.setAdapter(adapter);
            }//postExecute
        }//myasynccategory
} //main

