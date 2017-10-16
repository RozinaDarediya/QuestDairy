package com.example.admin.questdairy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Exception_Display_act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception__display_act);

        TextView text = (TextView)findViewById(R.id.text);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        text.setText(msg);
    }
}
