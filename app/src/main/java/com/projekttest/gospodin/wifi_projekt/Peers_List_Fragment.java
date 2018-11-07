package com.projekttest.gospodin.wifi_projekt;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.WpsInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.Toast;


public class Peers_List_Fragment extends Fragment implements WifiP2pManager.PeerListListener {

    private Params params;  // interface
    private ArrayList<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private ListView peersListView;

    public Peers_List_Fragment() {
        // Required empty public constructor
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_peers, container, false);

        peersListView = (ListView) v.findViewById(R.id.peersListView);
        Peers_ListView_Adapter adapter = new Peers_ListView_Adapter(getActivity(), R.layout.listview_peer_adapter, peers);
        peersListView.setAdapter(adapter);
        peersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connect(position);
            }
        });


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        params = (WifiDirect_MainActivity) context;       // parameters for connect(manager, channel)
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {

        if(!peerList.getDeviceList().equals(peers)) {
            peers.clear();
        }
            peers.addAll(peerList.getDeviceList());

            Log.e("Pees", String.valueOf(peers.size())+peerList.toString());

            Peers_ListView_Adapter adapter = new Peers_ListView_Adapter(getActivity(), R.layout.listview_peer_adapter, peers);
            peersListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            Log.d("Adapter", "Created");

        if (peers.size() == 0) {
            Log.d("Main activity", "No devices found");
        }

    }


    public void connect(int position) {

        Log.d("Connect", "Connecting...");
        WifiP2pDevice device = peers.get(position);
        WifiP2pConfig config = new WifiP2pConfig();

        WifiP2pManager manager = params.getManager();
        WifiP2pManager.Channel channel = params.getChannel();

        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;     //representing Wi-Fi Protected Setup


        manager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver notifies
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(getActivity().getApplicationContext(), "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}
