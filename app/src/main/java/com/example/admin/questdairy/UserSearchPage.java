package com.example.admin.questdairy;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

/**
 * Created by Admin on 7/28/2017.
 */

public class UserSearchPage extends Fragment {

    ImageView searchbtn;
    String cat, f_nm, l_nm;
    Spinner search_expert;
    ProgressDialog progressDialog;
    RecyclerView category_listview;
    JSONArray jsonArray, jsonArray1;
    ArrayList<String> category ;
    ArrayList<String> firstname , lastname;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.user_search_page,null);
        SharedPreferences sp = this.getActivity().getSharedPreferences("QuestDiary",getContext().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("farg","1");
        e.commit();
        init();

        MyAsync myAsync = new MyAsync();
        myAsync.execute();

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = new ArrayList<>();
                lastname = new ArrayList<String>();
                MyAsyncSelectedCategory myAsyncSelectedCategory = new MyAsyncSelectedCategory();
                myAsyncSelectedCategory.execute();
            }
        });

        search_expert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//search_expert.setOnItemSelectedListener()


        return view;
    }

   private void init() {
        search_expert = (Spinner)view.findViewById(R.id.search_expert);
        searchbtn = (ImageView)view.findViewById(R.id.searchbtn);
        category = new ArrayList<>();
        category.add("Choose your category");
        category_listview = (RecyclerView) view.findViewById(R.id.category_listview);
    }



    //This WS will return the categories from the database server
    class MyAsync extends AsyncTask<Object, Object, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("waiting....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }   //onPreExecute
        @Override
        protected JSONArray doInBackground(Object... params) {
            CommonURL commonURL = new CommonURL();
            URL url = commonURL.getUrl("DistinctExpertCategory.php");
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
        }   //doInBackground
        @Override
        protected void onPostExecute(JSONArray s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            for(int i=0 ;i<s.length() ; i++){
                try {
                    JSONObject jsonObject1 = s.getJSONObject(i);
                    category.add(jsonObject1.getString("category_name"));

                    ArrayAdapter adp_spinner = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,category);
                    search_expert.setAdapter(adp_spinner);
                } catch (JSONException e) {
                    e.printStackTrace();
                }   //catch
            } // for loop
        }   //opPostExecute
    }   //Async Class


    // This WS will display the names of the selected categories
    class MyAsyncSelectedCategory extends AsyncTask<Object, Object, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }   //preExecute
        @Override
        protected JSONArray doInBackground(Object... params) {
            URL url = null;
            try {
                CommonURL commonURL = new CommonURL();
                url = commonURL.getUrl("Display_Selected_Category.php");
                HttpURLConnection urlConnection = commonURL.getHttpURLConnection(url);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("category_name",cat);

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
                    jsonArray1 = new JSONArray(builder.toString());
                }//if
                else {
                    Toast.makeText(getContext(),"No record found...",Toast.LENGTH_LONG).show();
                }//else

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonArray1;
        }   //doInBackground()
        @Override
        protected void onPostExecute(JSONArray s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
                try {
                    for(int i=0; i<s.length() ;i++) {
                        JSONObject jsonObject = s.getJSONObject(i);
                        String firstName = jsonObject.getString("first_name");
                        String lastName = jsonObject.getString("last_name");
                        firstname.add(firstName);
                        lastname.add(lastName);
                    }//for loop

                    Adapter_Display_selected_Cat disp_cat_adapter = new Adapter_Display_selected_Cat(firstname,lastname, getContext(), new OnItemClickListener() {
                        @Override
                        public void onClick(View v, int pos) {

                             f_nm  = firstname.get(pos);
                             l_nm = lastname.get(pos);

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            User_Display_Selected_Expert user_display_selected_expert = new User_Display_Selected_Expert();
                            ft.replace(R.id.frame,user_display_selected_expert);

                            CommonSharedPreferenceClass.setValue(getContext(),"expert_fnm",f_nm);
                            CommonSharedPreferenceClass.setValue(getContext(),"expert_lnm",l_nm);
                            ft.commit();
                        }
                    });

                   category_listview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                   category_listview.setAdapter(disp_cat_adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
            }
        }   //onPostExecute
    }       //MyAsyncSelectedCategory()
}   //UserSearchPage
