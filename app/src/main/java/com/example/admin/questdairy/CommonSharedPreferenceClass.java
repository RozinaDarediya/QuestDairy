package com.example.admin.questdairy;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.admin.questdairy.R.id.content;
import static com.example.admin.questdairy.R.id.context;

/**
 * Created by Admin on 8/21/2017.
 */

/*  key     =>   value    =>    type
    userid  =>   id       =>    String
    email   =>  email     =>    String
    f_nm    =>  f_nm      =>    String
    l_nm    =>  l_nm      =>    String
    status  =>  true      =>    boolean
    expert_fnm => expert_fnm    =>  String
    expert_lnm => expert_lnm    =>  String
*/


public class CommonSharedPreferenceClass {
    static SharedPreferences sp ;
    static SharedPreferences.Editor editor;

    public static void setValue(Context context, String key, String value){
        sp = context.getSharedPreferences("QuestDiary",Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getValue(Context context,String key){
        sp = context.getSharedPreferences("QuestDiary",Context.MODE_PRIVATE);
        editor = sp.edit();
        String value = sp.getString(key,null);
        return value;
    }

    public static int getIntValue(Context context, String key){
        sp = context.getSharedPreferences("QuestDiary",Context.MODE_PRIVATE);
        editor = sp.edit();
        int value = sp.getInt(key,0);
        return value;
    }

    public static void setIntValue(Context context, String key, int value){
        sp = context.getSharedPreferences("QuestDiary",Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public static void setBooleanValue(Context context, String key, boolean value){
        sp = context.getSharedPreferences("QuestDiary",Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static boolean getBooleanValue(Context context,String key){
        sp = context.getSharedPreferences("QuestDiary",Context.MODE_PRIVATE);
        editor = sp.edit();
        boolean value = sp.getBoolean(key,false);
        return value;
    }

    public static void clear(Context context){
        sp = context.getSharedPreferences("QuestDiary",Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.clear();
    }
}
