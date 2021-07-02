package com.example.get_current_map_here;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

public class MyLocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.get_current_map_here.UPDATE_LOCATION";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent !=null)
        {
            final String action = intent.getAction()    ;

            if (ACTION_PROCESS_UPDATE.equals(action))
            {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null)
            {
                Location location = result.getLastLocation();
                String location_string = new StringBuilder(""+ location.getLatitude())
                        .append("/")
                        .append(location.getLongitude())
                        .toString();
                Log.e("Log", location_string);
                try{
                    MainActivity.getInstance().updateTextView(location_string);

                }catch (Exception e)
                {
                    Toast.makeText(context , location_string, Toast.LENGTH_LONG).show();
                }

            }

            }
        }

    }
}
