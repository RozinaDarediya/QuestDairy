package com.example.admin.questdairy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.questdairy.ConnectivityChecking.InternetConnectivityChecking;
import com.example.admin.questdairy.ConnectivityChecking.NoInternetScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity implements MyDelegate {

    SharedPreferences sp;
    SharedPreferences.Editor e;
    String nm, pass;
    EditText username, password;
    Button btnlogin;
    TextView tvreg,forgotPassword;
    JSONObject jsonObject1;
    ProgressDialog progressDialog;
    String role, email, f_nm, l_nm;
    String otpemail;
    int otp;
    int code;
    HttpURLConnection urlConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

       // InternetConnectivityChecking connection = new InternetConnectivityChecking();
        String response = isInetrnetOn();
        if(response.equals("not")){
            Intent intent = new Intent(Login.this, NoInternetScreen.class);
            startActivity(intent);
        }

        //creating object of SharedPreference
        sp = getSharedPreferences("QuestDiary",MODE_PRIVATE);
     //   e= sp.edit();
    }//onCreate

    private String isInetrnetOn() {
        String response = null;
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            response = "connected";

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            //eturn false;

            response = "not";
        }
        // return false;
        return response;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Boolean status = sp.getBoolean("status",false);
        String role = sp.getString("role","user");
        if(status==true){
            if(role.equals("user")){
                Intent intent = new Intent(this,UserHomePage.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(this,ExpertHomePage.class);
                startActivity(intent);
            }
        }
//************ TO GET OTP FOR FORGOT PASSWORD ***********
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//********** THIS FUNCTION WILL GET THE EMAIL ID FROM USER TO SENT OPT
                getMail();
            }
        }); //forgotPassword

// THIS FUNCTION WILL DO WORK OF LOGIN IN
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    validation();  //checking validation for login id and password
                }
                else {
                    MyAsync async = new MyAsync();
                    async.execute();
                }
            }
        });
// IF USER IS NOT A MEMBER THIS FUNCTION WILL OPEN THE REGESTRATION PAGE
        tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Login.this);

                alertBuilder.setTitle("Do you want to register as an Expert?");

                alertBuilder.setMessage( "Press 'YES'");
                alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),Register_screen.class);
                        intent.putExtra("key","expert");
                        startActivity(intent);
                    }
                });
                alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),Register_screen.class);
                        intent.putExtra("key","user");
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void validation() {
        if(username.getText().toString().trim().isEmpty()){
            username.setError("This field can not be empty");
        }
        if(password.getText().toString().trim().isEmpty()){
            password.setError("This field can not be empty");
        }
    }

    private void init() {
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        tvreg = (TextView)findViewById(R.id.tvreg);
        btnlogin = (Button)findViewById(R.id.btnlogin);
    }

// THIS FUNCTION WILL HANDLE THE RESPONSE FROM WEB SERVICE
    @Override
    public void response(JSONObject jsonObjectResponse, String file) {
        if(file.equals("Email/email.php")){
            try {
                otp = (int) jsonObjectResponse.get("otp");
                showOTPDialog();

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }//Email/email.php
        if(file.equals("Reset_Password.php")){
            try {
                String msg = String.valueOf(jsonObjectResponse.get("res"));
                if(msg.equals("success")){
                    Intent intent = new Intent(this,Login.class);
                    startActivity(intent);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }   //response

// THIS FUNCTION WILL OPEN THE DIALOG TO GET EMAIL TO SENT OPT
    void getMail(){
        LayoutInflater li = LayoutInflater.from(Login.this);
        View promptsView = li.inflate(R.layout.get_email, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
        alertDialogBuilder.setView(promptsView);
        final EditText emailId = (EditText) promptsView.findViewById(R.id.emailId);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("SUBMIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                if(emailId.getText().toString().isEmpty()){
                                    Toast.makeText(Login.this, "Email ID can not be empty...", Toast.LENGTH_SHORT).show();
                                    emailId.setFocusable(true);
                                }
                                otpemail = (emailId.getText().toString());
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("email",otpemail);
                                    WebServiceCallingClass webService = new WebServiceCallingClass(Login.this,Login.this,"Email/email.php",jsonObject,"Sending otp...");

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

// THIS FUNCTION WILL OPEN THE DIALOG TO WRITET OPT
    void showOTPDialog(){
        Toast.makeText(this, "hiiiiiiiii", Toast.LENGTH_SHORT).show();

        LayoutInflater li = LayoutInflater.from(Login.this);
        View promptsView = li.inflate(R.layout.get_otp, null);

          AlertDialog.Builder alertOtp = new AlertDialog.Builder(Login.this);
        alertOtp.setView(promptsView);

         final EditText otpET = (EditText) promptsView.findViewById(R.id.otpET);
        
        alertOtp
                .setCancelable(false)
                .setPositiveButton("SUBMIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String otpFromET = otpET.getText().toString();
                                if(String.valueOf(otp).equals(String.valueOf(otpFromET))){
                                    Toast.makeText(Login.this, "Now you can reset your password...", Toast.LENGTH_SHORT).show();
                                    resetPassword();
                                }
                                else {
                                    String message = "The OTP you have entered is incorrect." + " \n" + "Please try again";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            getMail();
                                        }
                                    });
                                    builder.create().show();
                                }
                                }
                            });
        //    builder.create().show();
        AlertDialog alertDialog = alertOtp.create();
        // show it
        alertDialog.show();
    } //showOTPDialog

// THIS FUNCTION WILL OPEN THE DIALOG TO RESET PASSWORD
    void resetPassword(){
        LayoutInflater li = LayoutInflater.from(Login.this);
        final View view = li.inflate(R.layout.reset_password_layout, null);

        AlertDialog.Builder alertResetPass = new AlertDialog.Builder(Login.this);
        alertResetPass.setView(view);

        final EditText pass1 = (EditText)view.findViewById(R.id.pass1);
        final EditText pass2 = (EditText)view.findViewById(R.id.pass2);


        alertResetPass
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()   {
                            public void onClick(DialogInterface dialog,int id) {
                                String a = pass1.getText().toString();
                                String b = pass2.getText().toString();
                                if(a.equals(b)){
                                    Snackbar snackbar = Snackbar.make(view,"Password changed successfully",Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                    try {
                                        JSONObject object = new JSONObject();
                                        object.put("username",otpemail);
                                        object.put("password",a);
                                        WebServiceCallingClass webService = new WebServiceCallingClass(Login.this,Login.this,"Reset_Password.php",object,"Updating password");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                }
                                else {

                                    String message = "The password you have entered is incorrect." + " \n" + "Please try again";

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                           resetPassword();
                                        }
                                    });
                                    builder.create().show();
                                }
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertResetPass.create();

        // show it
        alertDialog.show();

    }

    //********************  This WS calling will check the user is registered or not *********************************
    class MyAsync extends AsyncTask<Object, Object, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

             nm = username.getText().toString().trim();
             pass =password.getText().toString().trim();

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage("waiting....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }   //preExecute
        @Override
        protected JSONObject doInBackground(Object... params) {

            URL url = null;
            try {
                CommonURL commonURL = new CommonURL();
                 url = commonURL.getUrl("CheckUser.php");
                 urlConnection = commonURL.getHttpURLConnection(url);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username",nm);
                jsonObject.put("password",pass);

                /*DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(jsonObject.toString().getBytes());
*/
                DataOutputStream dataOutputStream = null;
                     try {
                                dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                                dataOutputStream.write(jsonObject.toString().getBytes());
                         } catch (IOException e1) {
                                e1.printStackTrace();
                                String c = "Error in sending required data...";
                                Intent intent = new Intent(getApplicationContext(),Exception_Display_act.class);
                                intent.putExtra("msg",c);
                                startActivity(intent);
                         }
                //int code = urlConnection.getResponseCode();
                try {
                    boolean res = urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
                } catch (IOException e) {
                    e.printStackTrace();
                    String a = "file not exits";
                    Intent intent = new Intent(getApplicationContext(),Exception_Display_act.class);
                    intent.putExtra("msg","Specified page not found");
                    startActivity(intent);
                }
                code = urlConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
                String c = "Connection time out";
                Intent intent = new Intent(getApplicationContext(),Exception_Display_act.class);
                intent.putExtra("msg","Connection time out.. You have poor internet connection..");
                startActivity(intent);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            StringBuilder builder = new StringBuilder();
                    String s1 = "";

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((s1 = bufferedReader.readLine()) != null) {
                    builder.append(s1);
                    //jsonObject = new JSONObject(builder.toString());
                }
                jsonObject1 = new JSONObject(builder.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
                /*else {
                    Toast.makeText(getApplicationContext(),"No record found...",Toast.LENGTH_LONG).show();
                }*/
            return jsonObject1;
        }       //doInBackground
        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            String res;

            try {
                res = s.getString("response");

            //if the response is successful then id will be stored in sharedpreference
            // and redirect to the next activity.
                if(res.equals("success")) {
                    String id = s.getString("id");
                    role = s.getString("type");
                    email = s.getString("username");
                    f_nm = s.getString("first_name");
                    l_nm = s.getString("last_name");
                        e=sp.edit();
                    e.putString("userid",id);
                    e.putString("email",email);
                    e.putString("f_nm",f_nm);
                    e.putString("l_nm",l_nm);
                    e.putBoolean("status",true);
                    e.commit();
            // if the existing user is User then this condition block will be executed
                    if(role.equals("user")) {
                        Intent intent = new Intent(getApplicationContext(), UserHomePage.class);
                        startActivity(intent);
                    }
             // if the existing user is Expert then this condition block will be executed
                    else{
                        Intent intent = new Intent(getApplicationContext(),ExpertHomePage.class);
                        startActivity(intent);
                    }
                }
            // if user id and password is not matched then following block will be execute
                else{
                    Toast.makeText(getApplicationContext(),"No matching record found",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Please enter correct email and password",Toast.LENGTH_LONG).show();
                    /*Snackbar snackbar = Snackbar.make(btnlogin,"No matching record found....",Snackbar.LENGTH_LONG);
                    snackbar.show();*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }   //onPostExecute()
    }   //MyAsync()

}   //mainActivity  Login
