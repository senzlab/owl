package com.score.rahasak.remote;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.score.senz.ISenzService;
import com.score.senzc.enums.SenzTypeEnum;
import com.score.senzc.pojos.Senz;

import java.util.HashMap;

/**
 * Service to get current location
 * We are listening to location updates via LocationListener here
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
public class LocationService extends Service implements LocationListener {

    private static final String TAG = LocationService.class.getName();

    private LocationManager locationManager;

    private Senz thisSenz;

    // keep providers
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;

    // keeps weather service already bound or not
    private boolean isServiceBound = false;

    // service interface
    private ISenzService senzService = null;

    // service connection
    private ServiceConnection senzServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "Connected with senz service");
            senzService = ISenzService.Stub.asInterface(service);
            if (!isGPSEnabled && !isNetworkEnabled) {
                sendLocationProviderNotEnabled();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            senzService = null;
            Log.d("TAG", "Disconnected from senz service");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // start to listen location updates from here
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // getting GPS and Network status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d(TAG, "No location provider enable");
            if (senzService != null)
                sendLocationProviderNotEnabled();
        } else {
            if (isNetworkEnabled) {
                Log.d(TAG, "Getting location via Network");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            if (isGPSEnabled) {
                Log.d(TAG, "Getting location via GPS");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent.hasExtra("SENZ"))
            this.thisSenz = intent.getExtras().getParcelable("SENZ");

        // bind with senz service
        if (!isServiceBound) {
            Intent serviceIntent = new Intent("com.score.rahasak.remote.SenzService");
            bindService(serviceIntent, senzServiceConnection, Context.BIND_AUTO_CREATE);
            isServiceBound = true;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isGPSEnabled) locationManager.removeUpdates(LocationService.this);
        if (isNetworkEnabled) locationManager.removeUpdates(LocationService.this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, String.valueOf(location.getLatitude()));
        Log.d(TAG, String.valueOf(location.getLongitude()));

        if (isGPSEnabled) locationManager.removeUpdates(LocationService.this);
        if (isNetworkEnabled) locationManager.removeUpdates(LocationService.this);

        sendLocation(location);

        // unbind the service
        if (isServiceBound) {
            unbindService(senzServiceConnection);
            isServiceBound = false;
        }

        // stop the service when receive a location update
        stopSelf();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void sendLocation(Location location) {
        try {
            // create senz attributes
            HashMap<String, String> senzAttributes = new HashMap<>();
            senzAttributes.put("time", ((Long) (System.currentTimeMillis() / 1000)).toString());
            senzAttributes.put("lat", Double.toString(location.getLatitude()));
            senzAttributes.put("lon", Double.toString(location.getLongitude()));

            String id = "_ID";
            String signature = "_SIGNATURE";
            SenzTypeEnum senzType = SenzTypeEnum.DATA;
            Senz senz = new Senz(id, signature, senzType, null, thisSenz.getSender(), senzAttributes);

            senzService.send(senz);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendLocationProviderNotEnabled() {
        try {
            // create senz attributes
            HashMap<String, String> senzAttributes = new HashMap<>();
            senzAttributes.put("time", ((Long) (System.currentTimeMillis() / 1000)).toString());
            senzAttributes.put("status", "locDisabled");

            String id = "_ID";
            String signature = "_SIGNATURE";
            SenzTypeEnum senzType = SenzTypeEnum.DATA;
            Senz senz = new Senz(id, signature, senzType, null, thisSenz.getSender(), senzAttributes);

            senzService.send(senz);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
