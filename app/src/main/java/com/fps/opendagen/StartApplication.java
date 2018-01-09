package com.fps.opendagen;


import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lightcurb.sdk.LightcurbSDK;
import java.util.Map;

import nl.alledaags.device.Bluetooth;
import com.fps.opendagen.NotificationService;

public class StartApplication extends Application
{
    public void onCreate()
    {
        super.onCreate();

        if (!Bluetooth.hasFeature(getApplicationContext()))
        {
            Toast.makeText(this, "Deze app heeft Bluetooth nodig om goed te kunnen werken.", Toast.LENGTH_SHORT).show();
        }
        else if (!Bluetooth.isEnabled(getApplicationContext()))
        {
            // Bluetooth is disabled

            // Switch to settings to enable
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.bluetooth.BluetoothSettings");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity( intent);
        }

        final LightcurbSDK sdk = new LightcurbSDK(getApplicationContext());

        sdk.configure("d4ef06a4-9523-4de2-a3a5-6512169d20c2", new LightcurbSDK.OnConfigureListener()
        {
            @Override
            public void onConfigureFailed(Map<String, Object> data)
            {
                Log.d("failed", "onConfigureFailed: ");
            }

            @Override
            public void onConfigureCompleted(String data)
            {
                sdk.startBeaconMonitoring();
            }
        });


        // Start the service that receives strBeaconsSeen and broadcasts notifications
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }
}
