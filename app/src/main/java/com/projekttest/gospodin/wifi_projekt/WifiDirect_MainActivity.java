package com.projekttest.gospodin.wifi_projekt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;


interface Params{
    WifiP2pManager getManager();
    WifiP2pManager.Channel getChannel();
}


public class WifiDirect_MainActivity extends AppCompatActivity implements Params {
    private WifiP2pManager _manager;
    private WifiP2pManager.Channel _channel;
    private BroadcastReceiver _bReciever;
    private IntentFilter intentFilter;

    public WifiP2pManager getManager() {
        return _manager;
    }
    public WifiP2pManager.Channel getChannel() {
        return _channel;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wifi_direct);

        final Switch wifiSwitch = (Switch) findViewById(R.id.switchWifi);
        final ToggleButton btnPeers = (ToggleButton) findViewById(R.id.btnDiscoverPeers);

        _manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        _channel = _manager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener(){
            public void onChannelDisconnected(){
                Log.d("Channel", "Disconnected");
            }
        });
        _bReciever = new WiFiDirect_BroadcastReciever(_manager, _channel, this);

        intentFilter = new IntentFilter();

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);

        final WifiP2pManager.ActionListener _listener = new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("Discover peers","Success");
            }
            @Override
            public void onFailure(int reasonCode) {
                Log.e("Discover peers", "Failed - "+reasonCode); }
        };



        if(wifiOn()) wifiSwitch.setChecked(true);
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) switchWifi(true);

                else {
                    switchWifi(false);
                    btnPeers.setChecked(false);
                }
            }
        });


        btnPeers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Log.d("Main", "Scanning start");
                    _manager.discoverPeers(_channel, _listener);
                }
                else{
                    Log.d("Main", "Scanning stop");
                }
            }
        });



        FragmentManager fManager = getSupportFragmentManager();

        Peers_List_Fragment peerListFrag = (Peers_List_Fragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        fManager.beginTransaction().replace(R.id.fragmentContainer, peerListFrag).commit();

    }


    private void switchWifi (boolean enable){

        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifi != null) {
            if (!wifi.isWifiEnabled() && enable) wifi.setWifiEnabled(true);

            else if (wifi.isWifiEnabled() && !enable) wifi.setWifiEnabled(false);
        }
    }

    private boolean wifiOn(){
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifi != null) {
            return wifi.isWifiEnabled();
        }
        return false;
    }




    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(_bReciever);
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(_bReciever, intentFilter);
    }

}