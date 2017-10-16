package com.example.admin.questdairy.ConnectivityChecking;

import android.app.Activity;
import android.net.ConnectivityManager;

/**
 * Created by Admin on 8/23/2017.
 */

public class InternetConnectivityChecking extends Activity{

    String response;
    public String isInetrnetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            response = "connected";

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            //eturn false;

            response = "not";
        }
        // return false;
        return response;
    }
}
