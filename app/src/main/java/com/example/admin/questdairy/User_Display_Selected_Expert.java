package com.example.admin.questdairy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Admin on 8/8/2017.
 */

public class User_Display_Selected_Expert extends Fragment {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.degree1)
    TextView degree1;
    @BindView(R.id.exp)
    TextView exp;
    @BindView(R.id.pos)
    TextView pos;
    @BindView(R.id.areCity)
    TextView areCity;
    @BindView(R.id.rate)
    RatingBar rate_bar;
   /* @BindView(R.id.spinner_action)
    Spinner spinnerAction;*/
    @BindView(R.id.blog)
    RecyclerView blog;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    Unbinder unbinder;

    ProgressDialog progressDialog;
    String fnm, lnm;
    JSONArray jsonArray1;
    //ArrayAdapter<String> adapter;
    ArrayList<String> blog_title;
    ArrayList<String> blog_content;
    ArrayList<String> blog_date;
    ArrayList<String> blog_time;
    //ArrayList<String> action;
    String first, last,contact_number, degree, area,city,rate,current_possition,experience;
    String lid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_selected_expert, null);
        unbinder = ButterKnife.bind(this, view);

        SharedPreferences sp = this.getActivity().getSharedPreferences("QuestDiary",getContext().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("farg","2");
        e.commit();

       FloatingActionMenu fab_menu = (FloatingActionMenu)view.findViewById(R.id.fab_menu);
        FloatingActionButton fab1 = (FloatingActionButton)view.findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton)view.findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton)view.findViewById(R.id.fab3);
        FloatingActionButton fab4 = (FloatingActionButton)view.findViewById(R.id.fab4);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = "tel:"+contact_number;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(a));
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address",contact_number);
                startActivity(smsIntent);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("fab 3");
            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("fab 4");
            }
        });
        fnm = CommonSharedPreferenceClass.getValue(getContext(),"expert_fnm");
        lnm = CommonSharedPreferenceClass.getValue(getContext(),"expert_lnm");

        mainLayout.setVisibility(View.INVISIBLE);
        MyAsyncDisplayExpertPro myAsync = new MyAsyncDisplayExpertPro();
        myAsync.execute();

        return view;
    }

    private void showToast(String s) {
        Toast.makeText(getActivity(),s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyAsyncDisplayExpertPro extends AsyncTask<Object, Object, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getContext());
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
                url = commonURL.getUrl("DisplaySelectedExpertProfile.php");
                HttpURLConnection urlConnection = commonURL.getHttpURLConnection(url);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("first_name", fnm);
                jsonObject.put("last_name", lnm);

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
                    Toast.makeText(getContext(), "No record found...", Toast.LENGTH_LONG).show();
                }//else

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonArray1;
        }//doInBackground

        @Override
        protected void onPostExecute(JSONArray s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            mainLayout.setVisibility(View.VISIBLE);
            blog_title = new ArrayList<>();
            blog_content = new ArrayList<>();
            blog_time = new ArrayList<>();
            blog_date = new ArrayList<>();

            try {
                for (int i=0; i<s.length();i++){
                    JSONObject jsonObject = s.getJSONObject(i);
                     lid = jsonObject.getString("lid");
                     first = jsonObject.getString("first_name");
                     last = jsonObject.getString("last_name");
                     contact_number = jsonObject.getString("contact_number");
                     degree = jsonObject.getString("degree");
                     area = jsonObject.getString("area");
                     city = jsonObject.getString("city");
                     rate = jsonObject.getString("rate");
                     current_possition = jsonObject.getString("current_possition");
                     experience = jsonObject.getString("experience");

                    JSONArray object = jsonObject.getJSONArray("arr_blog");
                    for(int j = 0; j<object.length(); j++){
                        JSONObject jsonObjectBlog = object.getJSONObject(j);
                        blog_title.add(jsonObjectBlog.getString("tital"));
                        blog_content.add(jsonObjectBlog.getString("blog"));
                        blog_date.add(jsonObjectBlog.getString("b_dt"));
                        blog_time.add(jsonObjectBlog.getString("b_tm"));
                    }
                }

                name.setText(first + " " + last);
                num.setText(contact_number);
                degree1.setText("Degree : "+degree);
                exp.setText("Experience : "+experience);
                pos.setText("Current Position : "+current_possition);
                areCity.setText(area + " , " + city);
                rate_bar.setRating(Float.parseFloat(rate));

                Glide.with(getActivity())
                        .load("https://glycolytic-reconfig.000webhostapp.com/ProjectExpertAdvice/UploadImage/"+lid+".png")
                        .centerCrop()
                        .placeholder(R.drawable.ic_profile)
                        .into(img);
               /* action = new ArrayList<>();
                action.add("Message");
                action.add("Chat");
                action.add("Call");
                action.add("Video call");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, action);
                spinnerAction.setAdapter(adapter);*/

                CommonClassDisplayBlog classDisplayBlog = new CommonClassDisplayBlog(blog_title, blog_content, getContext(), new NewInterface() {
                    @Override
                    public void onClick(View v, int pos) {
                        String title  = blog_title.get(pos);
                        String detail = blog_content.get(pos);
                        String date = blog_date.get(pos);
                        String time = blog_time.get(pos);

                        Bundle bundle = new Bundle();
                        bundle.putString("title",title);
                        bundle.putString("detail",detail);
                        bundle.putString("date",date);
                        bundle.putString("time",time);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Display_detailed_blog detailedBlog = new Display_detailed_blog();
                        ft.replace(R.id.frame,detailedBlog);
                        detailedBlog.setArguments(bundle);
                        ft.commit();
                      }
                });

                blog.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                blog.setAdapter(classDisplayBlog);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//post Execute

    }//async class
}
