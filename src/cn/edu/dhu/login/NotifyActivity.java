package cn.edu.dhu.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

public class NotifyActivity extends Activity implements OnClickListener, OnCancelListener {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        
        new AlertDialog.Builder(this)
	    	.setMessage(it.getExtras().getString("quote"))
	    	.setPositiveButton("OK", this)	    	
			.setOnCancelListener(this)
	    	.create()
	    	.show();
    }
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		NotifyActivity.this.finish();
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		NotifyActivity.this.finish();
	}
}
