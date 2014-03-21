package com.thessalonikiinmap;

import java.text.SimpleDateFormat;

import java.util.TimeZone;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

public class LogHelper {

	static final String timeStampFormat = "yyyy-MM-dd'T'HH:mm:ss";
	static final String timeStampTimeZoneId = "UTC";
	static final String  LOGTAG = "Monitor Location Provider";
	
	
	public static String FormatLocationInfo(String provider, double lat, double lng, float accuracy, long time){
		SimpleDateFormat timeStampFormatter = new SimpleDateFormat(timeStampFormat);
		timeStampFormatter.setTimeZone(TimeZone.getTimeZone(timeStampTimeZoneId));
		
		String timeStamp = timeStampFormatter.format(time); 
		String logMessage = String.format("%s | lat/lng = %f/%f | accuracy = %f | time = %s",
											provider, lat, lng, accuracy, timeStamp );
		
		return logMessage;
	}
	
	public static String FormatLocationInfo(Location location){
		String provider = location.getProvider();
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		float accuracy = location.getAccuracy();
		long time = location.getTime();
		
		return LogHelper.FormatLocationInfo(provider, lat, lng, accuracy, time);
	}
	
	
	public static String formatLocationProvider(Context context, LocationProvider provider){
		String name = provider.getName();
		int horizontalAccuracy = provider.getAccuracy();
		int powerRequirements = provider.getPowerRequirement();
		boolean hasMonetaryCost = provider.hasMonetaryCost();
		boolean requiresCell = provider.requiresCell();
		boolean requiresNetwork = provider.requiresNetwork();
		boolean requiresSatellite = provider.requiresSatellite();
		boolean supportsAltitude = provider.supportsAltitude();
		boolean supportsBearing= provider.supportsBearing();
		boolean supportsSpeed = provider.supportsSpeed();
		
		String enableMessage = "UNKNOWN";
		if (context != null){
			LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
			enableMessage = yOrN(lm.isProviderEnabled(name));
		}
		
		String horizontalAccuracyDisplay = translateAccuracyFineCource(horizontalAccuracy);
		String powerRequirementsDisplay = translatePower(powerRequirements);
		
		String logMessage = String.format("%s  | enabled:%s | horizontal accuracy:%s | power:%s |" +
				"cost:%s| uses cell:%s | uses network:%s | uses satellite:%s | has altitude:%s |" +
				"has bearing:%s | has speed: %s|", name, enableMessage, horizontalAccuracyDisplay,
				powerRequirementsDisplay, yOrN(hasMonetaryCost), yOrN(requiresCell), yOrN(requiresNetwork), 
				yOrN(requiresSatellite), yOrN(supportsAltitude), yOrN(supportsBearing), yOrN(supportsSpeed));
		
		return logMessage;
	}
	
	
	
	
	
	public static String yOrN(boolean value){
		return value ? "Y" : "N";
	}
	
	public static String yOrN(int value){
		return value != 0 ? "Y" : "N";
	}
	
	public static String translatePower(int value){
		String message = "UNDEFINED";
		
		switch(value){
			case LocationProvider.AVAILABLE:
				message = "AVAILABLE";
				break;
			case LocationProvider.OUT_OF_SERVICE:
				message = "OUT_OF_SERVICE";
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				message = "TEMPORARILY_UNAVAILABLE";
				break;
		}
		
		
		return message;
	}
	
	public static String translateAccuracyFineCource(int value){
		String message = "UNDEFINED";
		
		switch(value){
			case Criteria.ACCURACY_COARSE:
				message = "COARSE";
				break;
			case Criteria.ACCURACY_FINE:
				message = "FINE";
				break;
		}
		
		
		return message;
	}
	
	
	
	
	
}
