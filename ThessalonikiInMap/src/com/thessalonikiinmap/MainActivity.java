
package com.thessalonikiinmap;

import java.io.IOException;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;////////////////////////


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;///////
import com.google.android.gms.maps.SupportMapFragment;//////
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LocationListener,SensorEventListener{
	
	private String logTag = "Monitor location";
	private GoogleMap mMap;
	private SensorManager sm;
	private LocationManager lm;
	
	private Sensor sensor;
    private float[] mRotationMatrix = new float[16];
	private float[] mValues= new float[3];
	private CustomDrawableView mCustomDrawableView;
	
	// Create a constant to convert nanoseconds to seconds.
		private static final float NS2S = 1.0f / 1000000000.0f;
		private final float[] deltaRotationVector = new float[4];
		private float timestamp;
		private float mAzimuth;
		private float mTilt;
		
		private GeomagneticField geoField;
	
	//private ArrayList<MyMarker> markers = new ArrayList<MyMarker>();
	
	MarkerOptions markerOptions;
	String provider;
	LatLng latLng;
    Location location;
    Location leukosPurgos = new Location("Leukos Purgos");
    Location apsidaGaleriou = new Location("Apsida Galeriou");
    Location limani = new Location("Limani");
    Location aristotelous = new Location("Aristotelous");
    Location agsofia = new Location("Agias Sofias");
    Location kastra = new Location("Apsida Galeriou");
    Location anwpoli = new Location("Anw Poli");
    float bear, bear2, bear3, bear4, bear5, bear6;
    boolean btnRotateClicked = false, clicked=false;
	//LocationListener networkLocationListener ;
//	LocationListener gpsLocationListener;
	//LocationListener passiveLocationListener;
//	NetProviderStatusReceiver statusReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try{
			
			setUpMapAndLocation();
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true; 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
	        case R.id.action_settings:
	            if(clicked == false){
	        		clicked = true;
	        		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	        	}
	        	else{
	        		clicked = false;
	        		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);}
	            return true;
	        case R.id.item1:
	        	if(btnRotateClicked == false){
	        		btnRotateClicked = true;
	        		item.setTitle("Stop Rotating");
	        	}
	        	else{
	        		btnRotateClicked = false;
	        		item.setTitle("Rotate");}
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }

	}

	private void setUpMapAndLocation() {
		 
		if (mMap == null) {
			
        	mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        	
            // check if map is created successfully or not
            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
            else{
            	mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);//set map type!!! NORMAL, TERRAIN, HYBRID, SATELLITE
        		mMap.getUiSettings().setMyLocationButtonEnabled(true);
            	            	
                Button btn_find = (Button) findViewById(R.id.btn_find);// Getting reference to btn_find of the layout activity_main
                btn_find.setOnClickListener(findClickListener); // Setting button click event listener for the find button
                          
            ////////location
               mMap.setMyLocationEnabled(true);//Enable my location layer
               
                lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
                           
                Criteria criteria = new Criteria(); //to retrieve provider
        		provider = lm.getBestProvider(criteria, true); //get the name of the best provider
        		location = lm.getLastKnownLocation(provider);//get current location
        		
        		latLng = new LatLng(location.getLatitude(), location.getLongitude());//create object for the current location  
        		
        		//mMap.addCircle(new CircleOptions().center(latLng).radius(20).fillColor(0x5500ff00).strokeWidth(0));
        		
        		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        		mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        		//setMarkers   
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.626258f,22.949257f))//.rotation(1)
      				  .title("Leukos Pirgos").snippet("������ ������").alpha(0.7f)//.draggable(true)
         				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.627719f,22.94668f))//.rotation(1)
        				  .title("Leoforos Nikis-Leukos").snippet("�������� �����").alpha(0.7f)//.draggable(true)
           				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();   
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.629324,22.94434))//.rotation(1)
      				  .title("Leoforos Nikis").snippet("�������� �����").alpha(0.7f)//.draggable(true)
      				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.633615,22.937474))//.rotation(1)
        				  .title("Teloneio").snippet("��������").alpha(0.7f)//.draggable(true)
        				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.631568f,22.941095f))//.rotation(1)
      				  .title("Port").snippet("������").alpha(0.7f)//.draggable(true)
      				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  	
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.632373f, 22.952178f))//.rotation(1)
    				  .title("Apsida Galeriou 1").snippet("����� ��� ��������").alpha(0.7f)//.draggable(true)
    				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.632076f,22.952058f))//.rotation(1)
      				  .title("Apsida Galeriou 2").snippet("����� ��� ��������").alpha(0.7f)//.draggable(true)
      				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();   
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.632289f,22.951669f))//.rotation(1)
      				  .title("Apsida Galeriou 3").snippet("����� ��� ��������").alpha(0.7f)//.draggable(true)
      				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.63175f,22.944404f))//.rotation(1)
      				  .title("Agia Sofia").snippet("����� ������").alpha(0.7f)//.draggable(true)
      				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow(); 
      			mMap.addMarker(new MarkerOptions().position(new LatLng(40.633375f,22.945794f))//.rotation(1)
      				  .title("Agias Sofias").snippet("����� ������").alpha(0.7f)//.draggable(true)
      				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.632373f, 22.952178f))//.rotation(1)
      				  .title("Egnatia").snippet("�������").alpha(0.7f)//.draggable(true)
      				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.634069f,22.938219f))//.rotation(1)
      				  .title("Plateia Eleutherias").snippet("������� ����������").alpha(0.7f)//.draggable(true)
      				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.631123f,22.945488f))//.rotation(1)
      				  .title("Diagwnios").snippet("���������").alpha(0.7f)//.draggable(true)
      				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();   
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.633802f,22.94463f))//.rotation(1)
        				  .title("Ermou").snippet("�����").alpha(0.7f)//.draggable(true)
        				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  
        		mMap.addMarker(new MarkerOptions().position(new LatLng(40.633957f,22.938772f))//.rotation(1)
      				  .title("Venizelou").snippet("���������").alpha(0.7f)//.draggable(true)
      				  .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow(); 
      			mMap.addMarker(new MarkerOptions().position(new LatLng(40.633131f,22.940644f))//.rotation(1)
      				  .title("Aristotelous 2").snippet("������������").alpha(0.7f)//.draggable(true)
      				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();   	
      			mMap.addMarker(new MarkerOptions().position(new LatLng(40.633798f,22.940515f))//.rotation(1)
      				  .title("Aristotelous 3").snippet("������������").alpha(0.7f)//.draggable(true)
      				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow(); 
      			mMap.addMarker(new MarkerOptions().position(new LatLng(40.645433f,22.951233f))//.rotation(1)
      				  .title("Kastra").snippet("������").alpha(0.7f)//.draggable(true)
      				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow(); 
      			mMap.addMarker(new MarkerOptions().position(new LatLng(40.630656f,22.953647f))//.rotation(1)
      				  .title("Aggelaki").snippet("��������").alpha(0.7f)//.draggable(true)
      				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))).showInfoWindow();  	
        
        	
        		mMap.setOnMapLongClickListener(mapLongClickSettings());
        		mMap.setOnInfoWindowClickListener(infoWindowClickListener());
        		mMap.setOnMarkerClickListener(markerClickSettings());
        		
        		if(location!=null){
                    onLocationChanged(location);
                }
                lm.requestLocationUpdates(provider, 20000, 0, this);

                sm= (SensorManager) getSystemService(SENSOR_SERVICE);
        		Sensor vector= sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        		
        		sm.registerListener((SensorEventListener) this, vector, 16000);  	
        		
            }
            
        }
    }
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		  SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
		  SensorManager.getOrientation(mRotationMatrix, mValues);

		  mAzimuth = (float)Math.toDegrees(mValues[0]);
		  mTilt = (float)Math.toDegrees(mValues[1]);
		  
		  if (btnRotateClicked == true){
			  
			  CameraPosition camPos = new CameraPosition.Builder().bearing(mAzimuth)
	  		  		  .target(new LatLng(location.getLatitude(), location.getLongitude()))
	  		  		  .zoom(17)
	  		  		  .tilt(67)
	  		  		  .build();
	  		  mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));
	  		//Toast.makeText(getBaseContext(),( "You see White Tower "), Toast.LENGTH_LONG ).show();
	  		
	  		bear = location.bearingTo(leukosPurgos);
	  		bear2 = location.bearingTo(apsidaGaleriou);
	  		bear3 = location.bearingTo(limani);
	  		bear4 = location.bearingTo(aristotelous);
	  		bear5 = location.bearingTo(agsofia);
  			bear6 = location.bearingTo(kastra);
	  		
	  		DecimalFormat df = new DecimalFormat("#.#");
	  	    double b = Double.parseDouble(df.format(bear));
	  	    double b2 = Double.parseDouble(df.format(bear2));
	  	    double b3 = Double.parseDouble(df.format(bear3));
	  	    double b4 = Double.parseDouble(df.format(bear4));
	  	    double b5 = Double.parseDouble(df.format(bear5));
	  	    double b6 = Double.parseDouble(df.format(bear6));
	  	    double a = Double.parseDouble(df.format(mAzimuth)); 
	  	    
		  	    if (b>(a-0.02)&& b<(a+0.02))
			  		 Toast.makeText(getBaseContext(),( "You see Leukos Pirgos" ), Toast.LENGTH_LONG ).show();
		  	    else if(b2>(a-0.02)&& b2<(a+0.02))
		  	    	Toast.makeText(getBaseContext(),( "You see Apsida Galeriou"), Toast.LENGTH_LONG ).show();
		  	    else if(b3>(a-0.02)&& b2<(a+0.02))
		  	    	Toast.makeText(getBaseContext(),( "You see the Port"), Toast.LENGTH_LONG ).show();
		  	    else if(b4>(a-0.02)&& b2<(a+0.02))
		  	    	Toast.makeText(getBaseContext(),( "You see Plateia Aristotelous"), Toast.LENGTH_LONG ).show();
		  	    else if(b5>(a-0.02)&& b2<(a+0.02))
		  	    	Toast.makeText(getBaseContext(),( "You see Plateia Agias Sofias"), Toast.LENGTH_LONG ).show();
		  	    else if(b6>(a-0.02)&& b2<(a+0.02))
		  	    	Toast.makeText(getBaseContext(),( "You see Kastra"), Toast.LENGTH_LONG ).show();
		 }
		 //onLocationChanged(location);		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		geoField = new GeomagneticField(
		         Double.valueOf(location.getLatitude()).floatValue(),
		         Double.valueOf(location.getLongitude()).floatValue(),
		         Double.valueOf(location.getAltitude()).floatValue(),
		         System.currentTimeMillis()
		      );
		
		double lat = location.getLatitude();
		double lng = location.getLongitude();

		LatLng LAT = new LatLng(lat, lng);
		
		leukosPurgos.setLatitude(40.625811f);
		leukosPurgos.setLongitude(22.948047f);
		apsidaGaleriou.setLatitude(40.632373f);
		apsidaGaleriou.setLongitude(22.952178f);
		limani.setLatitude(40.631568f);
		limani.setLongitude(22.941095f);
  		aristotelous.setLatitude(40.633131f);
  		aristotelous.setLongitude(22.940644f);
    	agsofia.setLatitude(40.633375f);
    	agsofia.setLongitude(22.945794f);
    	kastra.setLatitude(40.645433f);
    	kastra.setLongitude(22.951233f);
   		//anwpoli = new Location("Anw Poli");
		
		double distanceLeukos = location.distanceTo(leukosPurgos)*0.001;
		double distanceApsida = location.distanceTo(apsidaGaleriou)*0.001;
		double distanceLimani = location.distanceTo(limani)*0.001;
		double distanceAristot = location.distanceTo(aristotelous)*0.001;
		double distanceAgsofia = location.distanceTo(agsofia)*0.001;
		double distanceKastra = location.distanceTo(kastra)*0.001;
	
		ArrayList<Double> distances = new ArrayList<Double>();
		distances.add(distanceLeukos);
		distances.add(distanceApsida);
		distances.add(distanceLimani);
		distances.add(distanceAristot);
		distances.add(distanceAgsofia);
		distances.add(distanceKastra);
		
		double minDistance = Collections.min(distances);
		String shown=" .";
		if (minDistance <=1 ){
			if (minDistance == distanceLeukos)
				shown="Leukos Purgos";
			else if(minDistance == distanceApsida)
				shown="Apsida Galeriou";
			else if(minDistance == distanceLimani)
				shown="Limani";
			else if(minDistance == distanceAristot)
				shown="Limani";
			else if(minDistance == distanceAgsofia)
				shown="Limani";
			else if(minDistance == distanceKastra)
				shown="Limani";
			
			Toast.makeText(getBaseContext(),( "You are near " + shown), Toast.LENGTH_LONG ).show();
		}
		
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	private OnMapLongClickListener mapLongClickSettings(){
		
		return new OnMapLongClickListener(){
			
			public void onMapLongClick(LatLng latLng){
				
               //mMap.clear();
               mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)); // Animating to the touched position
                
               // Creating a marker
               markerOptions = new MarkerOptions();
               markerOptions.position(latLng).alpha(0.7f).draggable(true);
               mMap.addMarker(markerOptions);    
      						
               new ReverseGeocodingTask(getBaseContext()).execute(latLng);
			}
		};
	}
	private void showpopup(Drawable myIcon)//ImageView imageView)//, int width, int height)
    {		 // Create dialog
			 Dialog dialog = new Dialog(this);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			 dialog.setContentView(R.layout.activity_display);
			// dialog.setTitle("DIALOG");
			 
			ImageView image = (ImageView) dialog.findViewById(R.id.imageview);
			dialog.getWindow().setBackgroundDrawable(null);
			
			image.setBackground(myIcon);
			dialog.show();
     }
	
	private OnInfoWindowClickListener infoWindowClickListener(){
		
		return new OnInfoWindowClickListener(){
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				/*Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);//this, Another.class);
 	            startActivity(i);  */
				String url = "";
				
				 if (marker.getTitle().equals("Leukos Pirgos")){
					showpopup(getResources().getDrawable( R.drawable.leukos1));
				}else if (marker.getTitle().equals("Leoforos Nikis-Leukos")){
					showpopup(getResources().getDrawable( R.drawable.leukos4));
				}else if (marker.getTitle().equals("Leoforos Nikis")){
					showpopup(getResources().getDrawable( R.drawable.limani5));
				}else if (marker.getTitle().equals("Teloneio")){
					showpopup(getResources().getDrawable( R.drawable.telonio));
				}else if (marker.getTitle().equals("Limani")){
					showpopup(getResources().getDrawable( R.drawable.limani7));
				}else if (marker.getTitle().equals("Apsida Galeriou 1")){
					showpopup(getResources().getDrawable( R.drawable.kamara5));
				}else if (marker.getTitle().equals("Apsida Galeriou 2")){
					showpopup(getResources().getDrawable( R.drawable.kamara8));
				}else if (marker.getTitle().equals("Apsida Galeriou 3")){
					showpopup(getResources().getDrawable( R.drawable.kamara1));
				}else if (marker.getTitle().equals("Agia Sofia")){
					showpopup(getResources().getDrawable( R.drawable.sofias));
				}else if (marker.getTitle().equals("Agias Sofias")){
					showpopup(getResources().getDrawable( R.drawable.agsofias));
				}else if (marker.getTitle().equals("Egnatia")){
					showpopup( getResources().getDrawable( R.drawable.kamara5));
				}else if (marker.getTitle().equals("Plateia Eleutherias")){
					showpopup(getResources().getDrawable( R.drawable.eleutherias));
				}else if (marker.getTitle().equals("Diagwnios")){
					showpopup(getResources().getDrawable( R.drawable.diagwnios));
				}else if (marker.getTitle().equals("Ermou")){
					showpopup(getResources().getDrawable( R.drawable.ermou));
				}else if (marker.getTitle().equals("Venizelou")){
					showpopup(getResources().getDrawable( R.drawable.venizelou));
				}else if (marker.getTitle().equals("Aristotelous 3")){
					showpopup(getResources().getDrawable( R.drawable.arist3));
				}else if (marker.getTitle().equals("Aristotelous 2")){
					showpopup(getResources().getDrawable( R.drawable.arist2));
				}else if (marker.getTitle().equals("Kastra")){
					showpopup(getResources().getDrawable( R.drawable.kastra));
				}else if (marker.getTitle().equals("Aggelaki")){
					showpopup(getResources().getDrawable( R.drawable.aggelaki));
				}
						
			}
		};
	}
	
	public void openWebURL( String inURL ) {
	    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );

	    startActivity( browse );
	}
	
	private OnMarkerClickListener markerClickSettings(){
		
		return new OnMarkerClickListener(){
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				//marker.hideInfoWindow();
				marker.showInfoWindow();
				return false;
			}
		};
	}
	
	// Defining button click event listener for the find button
	OnClickListener findClickListener = new OnClickListener() {
         @Override
         public void onClick(View v) {
             // Getting reference to EditText to get the user input location
             EditText etLocation = (EditText) findViewById(R.id.et_location);

             // Getting user input location
             String location = etLocation.getText().toString();

             if(location!=null && !location.equals("")){
                 new GeocoderTask().execute(location);
             }
         }
    };
	 // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            	//mMap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
 
                mMap.addMarker(new MarkerOptions().position(latLng)//.rotation(1)
                	.title(addressText).alpha(0.7f)//.draggable(true)
          			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).showInfoWindow();      	
 
                // Locate the first location
                if(i==0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	public class CustomDrawableView extends View {
	    Paint paint = new Paint();
	    public CustomDrawableView(Context context) {
	      super(context);
	      paint.setColor(0xff00ff00);
	      paint.setStyle(Style.STROKE);
	      paint.setStrokeWidth(2);
	      paint.setAntiAlias(true);
	    };
	}

	private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String>{
        Context mContext;
 
        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
        }
 
        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = params[0].latitude;
            double longitude = params[0].longitude;
 
            List<Address> addresses = null;
            String addressText="";
 
            try {
                addresses = geocoder.getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
 
            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);
 
                addressText = String.format("%s, %s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getLocality(),
                address.getCountryName());
            }
 
            return addressText;
        }
	
        @Override
        protected void onPostExecute(String addressText) {
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(addressText);
 
            // Placing a marker on the touched position
            mMap.addMarker(markerOptions).showInfoWindow();
             
        }
    }



}



	   
