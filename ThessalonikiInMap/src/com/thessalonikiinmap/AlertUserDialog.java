package com.thessalonikiinmap;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;///////
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class AlertUserDialog extends DialogFragment implements DialogInterface.OnClickListener{
	
	private String displayMessage;
	private String settingsActivityAction;
	
	public AlertUserDialog (String displayMessage, String settingsActivityAction){
		this.displayMessage = displayMessage !=null ? displayMessage : "MESSAGE NOT SET";
		this.settingsActivityAction = settingsActivityAction;
	}
	
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setMessage(displayMessage);
		builder.setPositiveButton("OK", this);
		
		Dialog theDialog = builder.create();
		theDialog.setCanceledOnTouchOutside(false);
		return theDialog;
	}
	
	@Override
	public void onClick(DialogInterface dialogInterface, int i) {
		switch(i){
			case Dialog.BUTTON_POSITIVE:
				//perform desired action response to user clicking "ok"
				if(settingsActivityAction != null){
					startActivity(new Intent(settingsActivityAction));
				}
				break;
		}
		
	}
	
	
	
	
}
