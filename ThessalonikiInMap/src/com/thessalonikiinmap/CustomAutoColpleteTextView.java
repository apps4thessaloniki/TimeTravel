package com.thessalonikiinmap;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CustomAutoColpleteTextView extends AutoCompleteTextView {
 
    public CustomAutoColpleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
    }
 
    /** Returns the place description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }

}
