package cn.edu.dhu.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotifyActivity extends Activity implements OnClickListener, OnCancelListener {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        if(it.getExtras()==null) {
			((LoginServiceHome)getApplicationContext()).comeout().stopSelf();
			NotifyActivity.this.finish();
        	return;
        }
        
        LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.quote_dialog,
				(ViewGroup) findViewById(R.id.quote_dialog));
        new AlertDialog.Builder(this)
        	.setView(layout)
//	    	.setMessage(s)
	    	.setPositiveButton("OK", this)	    	
			.setOnCancelListener(this)
	    	.create()
	    	.show();
        
        TextView msgBody = (TextView)layout.findViewById(R.id.message);
        msgBody.setText(Html.fromHtml(it.getExtras().getString("quote")));
        msgBody.setMovementMethod(LinkMovementMethod.getInstance());
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
