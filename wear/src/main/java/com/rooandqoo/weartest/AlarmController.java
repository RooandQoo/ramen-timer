package com.rooandqoo.weartest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

/**
 * usage:
 * AlarmController ac = new AlarmController(someService);
 * ac.turnOn();
 */
public class AlarmController implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private static final long CONNECTION_TIME_OUT_MS = 100;
    private static final String TAG = "AlarmController";
    private static final String PATH_SOUND_ALARM = "/sound_alarm";
    private static final String FIELD_ALARM_ON = "alarm_on";

    public AlarmController(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    public void turnOn() {
        turn(true);
    }

    public void turnOff() {
        turn(false);
    }

    private void turn(boolean onOrOff) {
        if (onOrOff) {
            Log.e(TAG, "turnOn");
        } else {
            Log.e(TAG, "turnOff");
        }
        //mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);

        if (mGoogleApiClient.isConnected()) {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(PATH_SOUND_ALARM);
            putDataMapRequest.getDataMap().putBoolean(FIELD_ALARM_ON, onOrOff);
            Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest())
                    .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(final DataApi.DataItemResult result) {
                            if(result.getStatus().isSuccess()) {
                                Log.d(TAG, "Data item set: " + result.getDataItem().getUri());
                            } else {
                                Log.d(TAG, "Failed!!!");
                            }
                        }
                    });
        } else {
            Log.e(TAG, "Failed to toggle alarm on phone - Client disconnected from Google Play "
                    + "Services");
        }
        //mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed");
    }

}
