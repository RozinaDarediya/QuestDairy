package com.example.admin.questdairy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class Expert_Registration extends Fragment {

    JSONObject jsonObject1;
    ProgressDialog progressDialog;
    EditText expert_etnm, expert_etlnm, expert_etcontact, expert_degree, expert_etemail, expert_etpass, expert_etpass1;
    Button expert_btnuploaddegree, user_btnreg, user_btnclr;
    ImageView degree;
    View view;
    String img, a;
    Uri uri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == getActivity().RESULT_OK && data != null) {
            uri = data.getData();
            img = String.valueOf(data.getData());
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            degree.setImageBitmap(bm);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.expert_reg_frame, null);
        init();


        expert_btnuploaddegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        user_btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expert_etnm.getText().toString().trim().isEmpty() || expert_etlnm.getText().toString().trim().isEmpty() ||
                        expert_etcontact.getText().toString().trim().isEmpty() || expert_degree.getText().toString().trim().isEmpty() ||
                        expert_etemail.getText().toString().trim().isEmpty() || expert_etpass.getText().toString().trim().isEmpty() ||
                        expert_etpass1.getText().toString().trim().isEmpty()) {
                    validation();
                }

                if(!(expert_etemail.getText().toString().trim().isEmpty())) {
                    MyAsyncEmail myAsyncEmail = new MyAsyncEmail();
                    myAsyncEmail.execute();
                }
            }
//      THIS IS A VALIDATION FUNCTION
            private void validation() {
                if (expert_etnm.getText().toString().trim().isEmpty()) {
                    expert_etnm.setError("This field can not be empty");
                }
                if (expert_etlnm.getText().toString().trim().isEmpty()) {
                    expert_etlnm.setError("This field can not be empty");
                }
                if (expert_etcontact.getText().toString().trim().isEmpty()) {
                    expert_etcontact.setError("This field can not be empty");
                }
                if (expert_degree.getText().toString().trim().isEmpty()) {
                    expert_degree.setError("This field can not be empty");
                }
                if (expert_etemail.getText().toString().trim().isEmpty()) {
                    expert_etemail.setError("This field can not be empty");
                }
                if (expert_etpass.getText().toString().trim().isEmpty()) {
                    expert_etpass.setError("This field can not be empty");
                }
                if (expert_etpass1.getText().toString().trim().isEmpty()) {
                    expert_etpass1.setError("This field can not be empty");
                }
            }
        });

        user_btnclr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expert_etnm.setText("");
                expert_etlnm.setText("");
                expert_etcontact.setText("");
                expert_degree.setText("");
                expert_etemail.setText("");
                expert_etpass.setText("");
                expert_etpass1.setText("");
                degree.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    private void init() {
        expert_etnm = (EditText) view.findViewById(R.id.expert_etnm);
        expert_etlnm = (EditText) view.findViewById(R.id.expert_etlnm);
        expert_etcontact = (EditText) view.findViewById(R.id.expert_etcontact);
        expert_degree = (EditText) view.findViewById(R.id.expert_degree);
        expert_etemail = (EditText) view.findViewById(R.id.expert_etemail);
        expert_etpass = (EditText) view.findViewById(R.id.expert_etpass);
        expert_etpass1 = (EditText) view.findViewById(R.id.expert_etpass1);
        expert_btnuploaddegree = (Button) view.findViewById(R.id.expert_btnuploaddegree);
        user_btnreg = (Button) view.findViewById(R.id.user_btnreg);
        user_btnclr = (Button) view.findViewById(R.id.user_btnclr);
        degree = (ImageView) view.findViewById(R.id.degree);
    }
// THIS WS IS VALIDATING EMAIL ID....
    class MyAsyncEmail extends AsyncTask<Object, Object, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Validating your email id..");
            progressDialog.show();
            a = expert_etemail.getText().toString();
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
        }//DOINBACKGROUND

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
                            expert_etemail.setText("");
                            expert_etemail.setFocusable(true);
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                    expert_etemail.setFocusable(true);

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

// THIS FUNCTION VALIDATE YOUR PASSWORD AND REDIRECT TO NEXT PAGE.....
    private void validatePassword() {
        String aa = expert_etpass.getText().toString().trim();
        String b = expert_etpass1.getText().toString().trim();
        if(!(aa.equals(b))) {
            Snackbar snackbar =Snackbar.make(view,"Password does not match... ",Snackbar.LENGTH_LONG);
            snackbar.show();
            expert_etpass1.requestFocus();
        }   //if
        else{
            Intent intent = new Intent(getActivity(), Expert_Registration_Part2.class);
            intent.putExtra("first_name", expert_etnm.getText().toString());
            intent.putExtra("last_name", expert_etlnm.getText().toString());
            intent.putExtra("contact", expert_etcontact.getText().toString());
            intent.putExtra("degree", expert_degree.getText().toString());
            intent.putExtra("email", expert_etemail.getText().toString());
            intent.putExtra("password", expert_etpass.getText().toString());
            intent.putExtra("certificate", String.valueOf(uri));
            startActivity(intent);
        }   //else
    }       //validatePassword
}
