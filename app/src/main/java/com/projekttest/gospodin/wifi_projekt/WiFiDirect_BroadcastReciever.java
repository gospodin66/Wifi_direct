package com.projekttest.gospodin.wifi_projekt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

public class WiFiDirect_BroadcastReciever extends BroadcastReceiver {
    private WifiP2pManager manager;
    private WifiDirect_MainActivity main_activity;
    private WifiP2pManager.Channel channel;

    public WiFiDirect_BroadcastReciever(WifiP2pManager wifiManager, WifiP2pManager.Channel _channel, WifiDirect_MainActivity wifiMainActivity){
        super();
        this.manager = wifiManager;
        this.main_activity = wifiMainActivity;
        this.channel = _channel;
    }


    private void wifiCheck(Intent intent){
        int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

        if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
            Toast.makeText(main_activity, "Wi-fi enabled", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(main_activity, "Wi-fi disabled", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(intentAction)){
            // Check to see if Wi-Fi is enabled and notify appropriate activity

            wifiCheck(intent);
        }
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(intentAction)){
                if(manager != null){
                    // Call WifiP2pManager.requestPeers() to get a list of current peers
                    Log.d("Requesting peers", "Processing..");
                    manager.requestPeers(channel, (WifiP2pManager.PeerListListener) main_activity.getSupportFragmentManager().findFragmentById(R.id.fragmentContainer));
                }
        }
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(intentAction)){
            // Respond to new connection or disconnections

        }
        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(intentAction)){
            // Respond to this device's wifi state changing



        }
        else if(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(intentAction)){

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, 10000);
            if (state == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED) {
                Log.d("Discovery", "Started");
            }
            else {
                Log.d("Discovery", "Stopped");
            }

        }
    }
}
