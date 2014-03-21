package com.thessalonikiinmap;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class NetProviderStatusReceiver extends BroadcastReceiver {
	
	final String logTag = "Monitor Location";
	
	public NetProviderStatusReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Bundle extras = intent.getExtras();
		
		Log.d(logTag, "Monitor Location Broadcast Receiver Action: "+ action);
		
		if(action.equalsIgnoreCase(Intent.ACTION_AIRPLANE_MODE_CHANGED)){
			boolean state = extras.getBoolean("state");
			Log.d(logTag, String.format("Monitor Location Airplane Mode changed to "+ 
					(state ? "ON" : "OF")));
		}
		else if(action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)){
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			Log.d(logTag, String.format("Monitor Location Wi-Fi Radio Available: "+ 
					(wifiInfo.isAvailable() ? "YES" : "NO")));
		}
		
		throw new UnsupportedOperationException("Not yet implemented");
	}
	public void start(Context context){//let the system know what I want to receive
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(this, filter);
	}
	public void stop(Context context){
		context.unregisterReceiver(this);
	}
}
