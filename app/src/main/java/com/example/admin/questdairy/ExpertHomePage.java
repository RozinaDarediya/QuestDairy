package com.example.admin.questdairy;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ExpertHomePage extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.expert_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
                    case R.id.profile:

                        Intent intent_ex = new Intent(this,Expert_profile.class);
                        startActivity(intent_ex);
                        return true;
                    case R.id.logout:

                        CommonSharedPreferenceClass.setBooleanValue(this,"status",false);
                        CommonSharedPreferenceClass.setValue(this,"userid",null);
                        CommonSharedPreferenceClass.setValue(this,"email",null);
                        CommonSharedPreferenceClass.setValue(this,"f_nm",null);
                        CommonSharedPreferenceClass.setValue(this,"l_nm",null);

                        Intent intent = new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                        return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_home_page);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }//onCreate

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void setUpViewPager(ViewPager viewPager) {
            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

            pagerAdapter.addFragment(new Expert_Write_Blog_Tab(),"Blog Writing");
            pagerAdapter.addFragment(new Expert_Blog_History_Tab(),"Blog History");
            pagerAdapter.addFragment(new Expert_Chat_Tab(),"Chat");
            pagerAdapter.addFragment(new Expert_Video_Tab(),"Video");
            viewPager.setAdapter(pagerAdapter);
    }//setUpViewPager


    class ViewPagerAdapter extends FragmentPagerAdapter{

        List<Fragment> FragmentList =new ArrayList<>();
        List<String> FragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);

        }//ViewPagerAdapter

        @Override
        public Fragment getItem(int position) {
            return FragmentList.get(position);
        }//getItem

        @Override
        public int getCount() {
            return FragmentList.size();
        }//getCount

        public void addFragment(Fragment fragment,String title){
            FragmentList.add(fragment);
            FragmentTitle.add(title);
        }//addFragment

        @Override
        public CharSequence getPageTitle(int position) {
            return FragmentTitle.get(position);
        }//getPageTitle
    }//ViewPagerAdapter


}//ExpertHomePage
