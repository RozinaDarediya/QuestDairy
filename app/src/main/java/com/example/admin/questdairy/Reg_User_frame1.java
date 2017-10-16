package com.example.admin.questdairy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Admin on 7/27/2017.
 */

public class Reg_User_frame1 extends Fragment {

    View view;
    EditText user_etnm, user_etlnm, user_etcontact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reg_user_frame1,null);
        return view;
    }
}
