package com.example.mysportfriends_school_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class MyBroadCastReceiver extends BroadcastReceiver {

    public interface NetworkStateListener {
        void onNetworkStateChange(boolean isWifiConnected, boolean isCellularConnected);
    }

    public interface WifiStateListener {
        void onWifiStateChange(boolean isWifiOn);
    }

    private NetworkStateListener networkListener;
    private WifiStateListener wifiListener;

    public void MyBroadcastReceiver(NetworkStateListener networkListener, WifiStateListener wifiListener) {
        this.networkListener = networkListener;
        this.wifiListener = wifiListener;
    }

    public void MyBroadcastReceiver(NetworkStateListener networkListener) {
        this.networkListener = networkListener;
    }

    public void MyBroadcastReceiver(WifiStateListener wifiListener) {
        this.wifiListener = wifiListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

        boolean isWifiConnected = false;
        boolean isCellularConnected = false;

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConnected = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                isCellularConnected = true;
            }
        }

        int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifiStateExtra) {
            case WifiManager.WIFI_STATE_ENABLED:
                if (wifiListener != null) {
                    wifiListener.onWifiStateChange(true);
                }
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                if (wifiListener != null) {
                    wifiListener.onWifiStateChange(false);
                }
                break;
        }

        if (networkListener != null) {
            networkListener.onNetworkStateChange(isWifiConnected, isCellularConnected);
        }
    }
}
