package com.example.admin.questdairy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meg7.widget.CustomShapeImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.admin.questdairy.R.id.imageView;

/**
 * Created by Admin on 8/14/2017.
 */

public class User_Profile extends Fragment {

    @BindView(R.id.profile_image)
    CustomShapeImageView profileImage;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.genderbtn)
    ImageView genderbtn;
    @BindView(R.id.dob)
    TextView dob;
    @BindView(R.id.datebtn)
    ImageView datebtn;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.password)
    TextView password;
    @BindView(R.id.passwordbtn)
    ImageView passwordbtn;
    @BindView(R.id.btnsave)
    Button btnsave;
    Unbinder unbinder;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.cntbtn)
    ImageView cntbtn;

    JSONArray jsonArray,jsonArrayRes;
    JSONObject jsonObject1;
    SharedPreferences sp;
    SharedPreferences.Editor e;
    ProgressDialog progressDialog;
    String firstname, lastname;
    String fnm ,lnm, cnt, gen, b_date,unm, pass;
    int id;
    String stringID;
    View view;
    Uri img_uri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.user_profile, null);
         unbinder = ButterKnife.bind(this,view);

       //Fatching userid from sharedPreference file.........................
        String key ="userid";
        id = Integer.parseInt(CommonSharedPreferenceClass.getValue(getContext(),key));
        stringID= String.valueOf(id);

        //**************THIS WS WILL DISPLAY USER PROFILE IN FRAGMENT************
        AsyncProfile async = new AsyncProfile();
        async.execute();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.profile_image, R.id.cntbtn, R.id.genderbtn, R.id.datebtn, R.id.passwordbtn, R.id.btnsave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_image:
                getImage();
                break;
            case R.id.cntbtn:
                //***************THIS FUNCTION WILL GENERATE ALERT DIALOG TO EDIT CONTACT NUMBER************
                onbtncnt();
                break;
            case R.id.genderbtn:
                //************THIS METHOD WILL SHOW POP UP MENU TO SET THE GENDER***************
                gender();
                break;
            case R.id.datebtn:
                break;
            case R.id.passwordbtn:
                //**************** THIS FUNCTION WILL GENERATE ALERT DIALOG TO CHANGE THE PASSWORD *************
                changePassword();
                break;
            case R.id.btnsave:
                AsyncSaveProfile saveProfile = new AsyncSaveProfile();
                saveProfile.execute();
                break;
        }       //swich()

    }// ********************* onViewClicked()

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==getActivity().RESULT_OK && data!=null) {
            Uri uri=data.getData();
            img_uri = uri;
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            profileImage.setImageBitmap(bm);
            AsyncImgUpload imgUpload = new AsyncImgUpload();
            imgUpload.execute();
        }
    }
//************ THIS FUNCTION WILL OPEN GALLARY TO PICK IMAGE
    void getImage(){
        firstname = CommonSharedPreferenceClass.getValue(getContext(),"f_nm");
        lastname = CommonSharedPreferenceClass.getValue(getContext(),"l_nm");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }
    //************THIS METHOD WILL SHOW POP UP MENU TO SET THE GENDER***************
    void gender(){
        PopupMenu popupMenu = new PopupMenu(getContext(),genderbtn);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String a = (String) item.getTitle();
               gender.setText(a);
                Toast.makeText(getContext(), "You set gender...", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popupMenu.show();//showing popup menu
    }
    //**************** THIS FUNCTION WILL GENERATE ALERT DIALOG TO CHANGE THE PASSWORD *************
    void changePassword(){
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.change_password_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(view);
        final EditText currentPassword = (EditText) view.findViewById(R.id.currentPassword);
        final EditText newPassword = (EditText) view.findViewById(R.id.newPAssword);
        final EditText rePassword = (EditText) view.findViewById(R.id.rePAssword);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()   {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                // result.setText(currentPassword.getText());

                                String a = newPassword.getText().toString();
                                String b = rePassword.getText().toString();
                                if(currentPassword.getText().toString().isEmpty() || newPassword.getText().toString().isEmpty() || rePassword.getText().toString().isEmpty()){
                                    Toast.makeText(getContext(), "None of the above field can be empty", Toast.LENGTH_SHORT).show();
                                }

                                if(a.equals(b)){
                                    Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                                    password.setText(a);
                                }
                                else {
                                    String message = "The password you have entered is incorrect." + " \n" + "Please try again";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            changePassword();
                                        }
                                    });
                                    builder.create().show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    //***************THIS FUNCTION WILL GENERATE ALERT DIALOG TO EDIT CONTACT NUMBER************
    void onbtncnt(){
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                contact.setText(userInput.getText());
                                Toast.makeText(getContext(), "You have changed your contact number...", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    } //***************FUNCTION OVER*************/

    // *********** this WS will display the user profile
        class AsyncProfile extends AsyncTask<Object, Object, JSONArray> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }   //preExecute

            @Override
            protected JSONArray doInBackground(Object... params) {
                URL url = null;
                try {
                    CommonURL commonURL = new CommonURL();
                    url = commonURL.getUrl("User_Profile.php");
                    HttpURLConnection urlConnection = commonURL.getHttpURLConnection(url);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("lid", id);

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
                        jsonArray = new JSONArray(builder.toString());
                    } else {
                        Toast.makeText(getContext(), "No record found...", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return jsonArray;
            }   //doInBackground

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                super.onPostExecute(jsonArray);
                progressDialog.dismiss();
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                         fnm = jsonObject.getString("first_name");
                         lnm = jsonObject.getString("last_name");
                         cnt = jsonObject.getString("contact_number");
                         gen = jsonObject.getString("gender");
                         b_date = jsonObject.getString("date_of_birth");
                         unm = jsonObject.getString("username");
                         pass = jsonObject.getString("password");


                        name.setText(fnm + " " + lnm);
                        gender.setText(gen);
                        contact.setText(cnt);
                        dob.setText(b_date);
                        email.setText(unm);
                        password.setText(pass);
                    }

                    String imguri ="https://glycolytic-reconfig.000webhostapp.com/ProjectExpertAdvice/UploadImage/"+id+".png" ;
                    Log.e("msg",imguri);
//                    Glide.with(getActivity())
//                            .load(imguri)
//                            .centerCrop()
//                            .placeholder(R.drawable.ic_profile)
//                            .into(profileImage);

                    Glide.with(getActivity()).load(imguri)
                            .error(R.drawable.ic_profile)
                            .into(profileImage);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }//onPostExxecute()
        }//AsyncPrifie class

    //********************************************************************this WS will save the profile changes in db
    class AsyncSaveProfile extends AsyncTask<Object, Object, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cnt = contact.getText().toString();
            gen = gender.getText().toString();
            b_date = dob.getText().toString();
            pass = password.getText().toString();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Saving Data....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }   //preExecute

        @Override
        protected JSONArray doInBackground(Object... params) {
            URL url = null;
            try {
                CommonURL commonURL = new CommonURL();
                url = commonURL.getUrl("Update_User_Profile.php");
                HttpURLConnection urlConnection = commonURL.getHttpURLConnection(url);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lid", id);
                jsonObject.put("contact_number", cnt);
                jsonObject.put("gender", gen);
                jsonObject.put("date_of_birth", b_date);
                jsonObject.put("password", pass);

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
                    jsonArrayRes = new JSONArray(builder.toString());
                } else {
                    Toast.makeText(getContext(), "No record found...", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return jsonArrayRes;
        }      //doInBackground

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            String res = null;
            progressDialog.dismiss();
            try {
                for(int i=0 ; i<jsonArray.length(); i++){
                    JSONObject objectRes = new JSONObject();
                    objectRes = jsonArray.getJSONObject(i);
                    res = objectRes.getString("res1");
                }
                 
                if (res.equals("success")){
                    Snackbar snackbar = Snackbar.make(getView(),"Your profile information is saved...",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }//     AsyncSaveProfile

    //*********************************** THIS WS WILL SAVE IMG IN DB
    class AsyncImgUpload extends AsyncTask<Object, Object, JSONObject> {
        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Saving profile picture....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }//onPostExecute

        @Override
        protected JSONObject doInBackground(Object... params) {

            String name= stringID+".png";//image name
            String boundary="*****";
            String lineEnd="\r\n";
            String twoHyphens="--";
            int bytesAvailable,bytesRead,bufferSize;
            byte buffer[];
            int maxBufferSize=1*1024*1024;

            try {
                URL uri1 = new URL("https://glycolytic-reconfig.000webhostapp.com/ProjectExpertAdvice/image_upload.php?id="+id);
// a= inser id ;
                InputStream stream=getActivity().getContentResolver().openInputStream(img_uri);
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
        }//doInBackground

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            progressDialog.dismiss();
            String msg = null;
            try {
                msg = jsonObject1.getString("msg");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }//onProgressUpdate
    }//AsyncImgUpload
}//*****************user Profile class