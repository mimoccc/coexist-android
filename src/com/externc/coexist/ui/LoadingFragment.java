package com.externc.coexist.ui;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.externc.coexist.R;

public class LoadingFragment extends DialogFragment{

	public LoadingFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final ProgressDialog dialog = new ProgressDialog(getActivity());
	    //
	    dialog.setTitle("Loading");
	    dialog.setMessage(getString(R.string.loading_string));
	    dialog.setIndeterminate(true);
	    dialog.setCancelable(false);

	    return dialog; 
	}

}
