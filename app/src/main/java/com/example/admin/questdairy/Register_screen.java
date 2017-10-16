package com.example.admin.questdairy;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Register_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        if(key.equals("expert")){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Expert_Registration expert_registration = new Expert_Registration();
            ft.replace(R.id.frame_reg,expert_registration);
            ft.commit();
        }
        else{
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            User_Registration user_registration = new User_Registration();
            ft.replace(R.id.frame_reg,user_registration);
            ft.commit();
        }
    }
}
