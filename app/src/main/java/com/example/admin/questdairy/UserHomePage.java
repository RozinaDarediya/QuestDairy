package com.example.admin.questdairy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UserHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sp;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        sp = getSharedPreferences("QuestDiary",MODE_PRIVATE);
        e= sp.edit();
        String email = sp.getString("email",null);
        String fnm = sp.getString("f_nm",null);
        String lnm = sp.getString("l_nm",null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       // navHeaderView= navigationView.inflateHeaderView(R.layout.nav_header_user_home_page);
        View headerView = navigationView.getHeaderView(0);
        TextView textView = (TextView)headerView.findViewById(R.id.textView);
        textView.setText(email);
        TextView textView2 = (TextView)headerView.findViewById(R.id.textView2);
        textView2.setText(fnm +" "+lnm);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserSearchPage userSearchPage = new UserSearchPage();
        ft.replace(R.id.frame,userSearchPage);
        ft.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int pos = Integer.parseInt(sp.getString("farg",null));
            switch (pos){
                case 1:
                    //super.onBackPressed();
                    //this.finish();
                    //System.exit(0);
                   // android.os.Process.killProcess(android.os.Process.myPid());
                    finish();
                    moveTaskToBack(true);
                    break;
                case 2:
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    UserSearchPage userSearchPage = new UserSearchPage();
                    ft.replace(R.id.frame,userSearchPage);
                    ft.commit();
                    break;
                case 3:
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    User_Display_Selected_Expert user_display_selected_expert = new User_Display_Selected_Expert();
                    ft1.replace(R.id.frame,user_display_selected_expert);
                    ft1.commit();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            User_Profile user_profile = new User_Profile();
            ft.replace(R.id.frame,user_profile);
            ft.commit();

            return true;
        }
        if(id == R.id.logout){
            /*SharedPreferences sp = getSharedPreferences("QuestDiary",MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();*/


            e.putString("userid",null);
            e.putString("email",null);
            e.putString("f_nm",null);
            e.putString("l_nm",null);
            e.putBoolean("status",false);
            e.putString("expert_fnm",null);
            e.putString("expert_lnm",null);
            e.commit();

            //CommonSharedPreferenceClass.clear(UserHomePage.this);
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            return true;
        }

        //return super.onOptionsItemSelected(item);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search) {
            // display the search page
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            UserSearchPage userSearchPage = new UserSearchPage();
            ft.replace(R.id.frame,userSearchPage);
            ft.commit();
        } else if (id == R.id.profile) {
            // display the profile page
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            User_Profile user_profile = new User_Profile();
            ft.replace(R.id.frame,user_profile);
            ft.commit();
        } else if (id == R.id.wallet) {

        } else if (id == R.id.notification) {

        } else if (id == R.id.share) {

        } else if (id == R.id.contact_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
