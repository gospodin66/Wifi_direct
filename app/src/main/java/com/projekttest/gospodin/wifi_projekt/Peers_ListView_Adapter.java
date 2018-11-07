package com.projekttest.gospodin.wifi_projekt;


import android.app.Activity;
import android.net.wifi.p2p.WifiP2pDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class Peers_ListView_Adapter extends ArrayAdapter<WifiP2pDevice> {

    private Context context;
    private int layoutResId;
    private ArrayList<WifiP2pDevice> device;


    public Peers_ListView_Adapter(Context _context, int _layoutResId, ArrayList <WifiP2pDevice> _device) {
        super(_context, _layoutResId, _device);
        this.layoutResId = _layoutResId;
        this.context = _context;
        this.device = _device;
    }


    @NonNull
    public View getView(int position, View v, @NonNull ViewGroup parent){

        ViewHolder holder = new ViewHolder();
        WifiP2pDevice __device =  device.get(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (mInflater != null) {
            v = mInflater.inflate(R.layout.listview_peer_adapter, parent, false);
        }

        holder.txtDeviceName = v.findViewById(R.id.textDeviceName);
        holder.txtDeviceDetails = v.findViewById(R.id.textDeviceDetails);


        if (__device != null) {
            holder.txtDeviceName.setText(__device.deviceName);
            holder.txtDeviceDetails.setText(__device.deviceAddress);
        }

        return v;
    }


    public class ViewHolder{
        TextView txtDeviceName;
        TextView txtDeviceDetails;
    }

}
