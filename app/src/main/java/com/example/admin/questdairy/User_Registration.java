package com.example.admin.questdairy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 7/20/2017.
 */

public class User_Registration extends Fragment {

    String a;
    View view;
    EditText user_etnm, user_etlnm, user_etcontact, user_etemail, user_etpass, user_etpass1;
    String nm, lnm, cnt, email, pass, pass1;
    Button user_btnreg, user_btnclr;
    ProgressDialog progressDialog;
    JSONObject jsonObject1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_reg_frame,null);
        init();

        user_btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values();
                if(nm.isEmpty() || lnm.isEmpty() || cnt.isEmpty() || email.isEmpty() || pass.isEmpty() || pass1.isEmpty()){
                    validation();
                }
                /*if(!(pass.equals(pass1))){
                    Snackbar snackbar =Snackbar.make(view,"Password does not match... ",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    user_etpass1.requestFocus();
                }*/
                else{
                    MyAsyncEmail myAsyncEmail = new MyAsyncEmail();
                    myAsyncEmail.execute();
                }
            }

            private void validation() {
                if(nm.isEmpty()){
                    user_etnm.setError("This field can not be empty");
                }
                if(lnm.isEmpty()){
                    user_etlnm.setError("This field can not be empty");
                }
                if(cnt.isEmpty()){
                    user_etcontact.setError("This field can not be empty");
                }
                if(email.isEmpty()){
                    user_etemail.setError("This field can not be empty");
                }
                if(pass.isEmpty()){
                    user_etpass.setError("This field can not be empty");
                }
                if(pass1.isEmpty()){
                    user_etpass1.setError("This field can not be empty");
                }
            }
        });

        user_btnclr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_etnm.setText("");          user_etlnm.setText("");
                user_etcontact.setText("");     user_etpass.setText("");
                user_etemail.setText("");       user_etpass1.setText("");
            }
        });

        return view;
    }

    private void init() {
        user_etnm = (EditText)view.findViewById(R.id.user_etnm);
        user_etlnm = (EditText)view.findViewById(R.id.user_etlnm);
        user_etcontact = (EditText)view.findViewById(R.id.user_etcontact);
        user_etemail = (EditText)view.findViewById(R.id.user_etemail);
        user_etpass = (EditText)view.findViewById(R.id.user_etpass);
        user_etpass1 = (EditText)view.findViewById(R.id.user_etpass1);
        user_btnreg = (Button)view.findViewById(R.id.user_btnreg);
        user_btnclr = (Button)view.findViewById(R.id.user_btnclr);
    }
    private void values(){
        nm = user_etnm.getText().toString().trim();
        lnm = user_etlnm.getText().toString().trim();
        cnt = user_etcontact.getText().toString().trim();
        email = user_etemail.getText().toString().trim();
        pass = user_etpass.getText().toString().trim();
        pass1 = user_etpass1.getText().toString().trim();
    }

    class MyAsync extends AsyncTask<Object, Object, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("waiting....");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            CommonURL commonURL = new CommonURL();
            URL url = commonURL.getUrl("UserRegWS.php");
            HttpURLConnection urlConnection = commonURL.getHttpURLConnection(url);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("first_name", nm);
                jsonObject.put("last_name", lnm);
                jsonObject.put("contact_number", cnt);
                jsonObject.put("email_id", email);

                jsonObject.put("username", email);
                jsonObject.put("password", pass);
                jsonObject.put("type","user");

                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(jsonObject.toString().getBytes());

                StringBuilder builder=new StringBuilder();
                String s1="";
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((s1=bufferedReader.readLine())!=null)
                {
                    builder.append(s1);

                }
                jsonObject1=new JSONObject(builder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonObject1;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            try {
                String res = s.getString("response");
                Snackbar snackbar = Snackbar.make(view,"Successfully registered...",Snackbar.LENGTH_LONG);
                snackbar.show();

                CommonSharedPreferenceClass.setValue(getContext(),"f_nm",nm);
                CommonSharedPreferenceClass.setValue(getContext(),"l_nm",lnm);
                CommonSharedPreferenceClass.setValue(getContext(),"email",email);

                Intent intent = new Intent(getActivity(),UserHomePage.class);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

// THIS WS WILL VALIDATE YOUR EMAIL ID
    class MyAsyncEmail extends AsyncTask<Object, Object, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Validating your email id..");
            progressDialog.show();
            a = user_etemail.getText().toString();
        }//onPreExecute

        @Override
        protected JSONObject doInBackground(Object... params) {

            CommonURL commonURL = new CommonURL();
            URL url = commonURL.getUrl("CheckEmail_idWS.php");
            HttpURLConnection urlConnection = commonURL.getHttpURLConnection(url);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", a);

                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(jsonObject.toString().getBytes());

                int code = urlConnection.getResponseCode();

                if (code == 200) {
                    StringBuilder builder = new StringBuilder();
                    String s1 = "";
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    while ((s1 = bufferedReader.readLine()) != null) {
                        builder.append(s1);

                    }
                    jsonObject1 = new JSONObject(builder.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonObject1;
        }   //DOINGBACKGROUND

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            progressDialog.dismiss();

            try {
                String msg = jsonObject.getString("msg");
                if (msg.equals("already exists")) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Invalid email id..");
                    alertBuilder.setMessage("This email id is already registered. Please use another email id..");
                    alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            user_etemail.setText("");
                            user_etemail.setFocusable(true);
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                    user_etemail.setFocusable(true);

                } else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Validation completed successfully..");
                    alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            validatePassword();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }       // else of msg
            } catch (JSONException e) {
                e.printStackTrace();
            }//catch
        }       //OnPostExecute
    }       //MyAsyncEmail

    private void validatePassword() {
        String aa = user_etpass.getText().toString().trim();
        String b = user_etpass1.getText().toString().trim();
        if (!(aa.equals(b))) {
            Snackbar snackbar = Snackbar.make(view, "Password does not match... ", Snackbar.LENGTH_LONG);
            snackbar.show();
            user_etpass1.requestFocus();
        }   //if
        else{
            MyAsync async = new MyAsync();
            async.execute();
        }
    }      //validatePassword()
}//User_regestration
