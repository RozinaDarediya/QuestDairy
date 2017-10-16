package com.example.admin.questdairy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.meg7.widget.CustomShapeImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpertRegistrationRemaining extends AppCompatActivity implements MyDelegate {

    @BindView(R.id.imageView1)
    CustomShapeImageView imageView1;
    @BindView(R.id.current_pos)
    EditText currentPos;
    @BindView(R.id.experience)
    EditText experience;
    @BindView(R.id.btnreg)
    Button btnreg;
    @BindView(R.id.btnclr)
    Button btnclr;

    String fnm, lnm;
    int id ;
    Uri uri;
    String exp, current_pos;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK && data!=null) {
             uri=data.getData();
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView1.setImageBitmap(bm);
            ImageUploadClass uploadClass = new ImageUploadClass(this,"Saving your profile pic..",id+".png",id,uri);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_registration_remaining);
        ButterKnife.bind(this);

       id = CommonSharedPreferenceClass.getIntValue(getApplicationContext(),"userid");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // THIS FUNCTION WILL OPEN THE GALLARY TO SET PROFILE PIC
                setProfile();
            }
        });//onClick

    }//onCreate

    // THIS WILL GET FIRST NM AND LAST NM FROM SP AND CALL WS THAT WILL GIVE ID OF REGISTERED USER....


    private void setProfile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//**//*");
        startActivityForResult(intent,2);
    }

    @OnClick({R.id.btnreg, R.id.btnclr})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnreg:
                validation();
                getData();
                break;
            case R.id.btnclr:
                currentPos.setText("");
                experience.setText("");
                break;
        }//switch
    }//onClick

    private void getData() {
        current_pos = currentPos.getText().toString();
        exp = experience.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("",id);
            jsonObject.put("",current_pos);
            jsonObject.put("",exp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebServiceCallingClass WSCall = new WebServiceCallingClass(ExpertRegistrationRemaining.this,ExpertRegistrationRemaining.this,"Expert_Remaining_Detail.php",jsonObject,"Saving your details...");
    }

    private void validation() {
        if(currentPos.getText().toString().isEmpty()){
            currentPos.setError("This field can not be empty");
            currentPos.setFocusable(true);
        }
        if(experience.getText().toString().isEmpty()){
            experience.setError("This field can not be empty");
            experience.setFocusable(true);
        }
    }

    @Override
    public void response(JSONObject jsonObjectResponse, String file) {
        if(file.equals("Expert_Remaining_Detail.php")){
            try {
                String res = (String) jsonObjectResponse.get("response");
                Snackbar snackbar =  Snackbar.make(imageView1,"Your profile saved successfully...",Snackbar.LENGTH_LONG);
                snackbar.show();

                Intent intent = new Intent(this,ExpertHomePage.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
