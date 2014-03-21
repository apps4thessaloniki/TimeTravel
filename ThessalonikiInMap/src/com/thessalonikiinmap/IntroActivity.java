package com.thessalonikiinmap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class IntroActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		Thread timer = new Thread(){
			
			@Override
			public void run(){
				try{
					sleep(2000); 
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				finally{
					Intent openMainActivity = new Intent("com.thessalonikiinmap.MAINACTIVITY");
					startActivity(openMainActivity);
				}
			
				
			}
			
		};
		timer.start();
	}

	@Override
	public void onPause(){
		super.onPause();
		//mySong.release();
		finish();
	}

}
