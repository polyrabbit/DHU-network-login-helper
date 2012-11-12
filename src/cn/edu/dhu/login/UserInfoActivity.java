package cn.edu.dhu.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class UserInfoActivity extends Activity implements OnClickListener, OnFocusChangeListener, TextWatcher, OnCancelListener, OnKeyListener {
	
	private View layout = null;
	private EditText txt_username = null;
	private EditText txt_passwd = null;
	private Button btn_OK = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_name), Activity.MODE_PRIVATE);
        
        LayoutInflater inflater = getLayoutInflater();
		layout = inflater.inflate(R.layout.login_dialog,
				(ViewGroup) findViewById(R.id.login_dialog));
		btn_OK = new AlertDialog.Builder(this)
			.setTitle(R.string.login_dialog_title)
			.setIcon(R.drawable.ic_launcher_info)
			.setView(layout)
			.setPositiveButton("好的", this)
			.setNegativeButton("再等会", this)
			.setOnCancelListener(this)
			.show()
			.getButton(AlertDialog.BUTTON_POSITIVE);
		
//		btn_OK.setEnabled(false);
		
		txt_username = (EditText)layout.findViewById(R.id.username);
		txt_username.setOnFocusChangeListener(this);
		txt_username.addTextChangedListener(this);
		txt_username.setText(preferences.getString("username", null));
		
		txt_passwd = (EditText)layout.findViewById(R.id.password);
		txt_passwd.setOnFocusChangeListener(this);
		txt_passwd.setOnKeyListener(this);
		txt_passwd.setText(preferences.getString("passwd", null));
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	//TouchPal won't response correctly
    	hideInputMethod();
    }
    
    @Override
	public void onClick(DialogInterface dialog, int which) {
    	if(which==AlertDialog.BUTTON_POSITIVE) {
    		login();
    	}
    	cleanup();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		((EditText)v).setTextColor(hasFocus?Color.BLACK : Color.GRAY);
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(s.toString().trim().length()==0) {
			btn_OK.setEnabled(false);
		} else {
			btn_OK.setEnabled(true);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		cleanup();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_ENTER) {
			if(txt_username.getText().toString().trim().length()!=0) {
				login();
				cleanup();
			}
			return true;
		}
		return false;
	}
	
	public void login() {    	
		Intent it = new Intent(this, LoginService.class);
		it.putExtra("username", txt_username.getText().toString().trim());
		it.putExtra("passwd", txt_passwd.getText().toString());
		this.startService(it);
	}
	
	public void cleanup() {
		UserInfoActivity.this.finish();
		hideInputMethod();
	}
	
	public void hideInputMethod() {
		InputMethodManager imm = (InputMethodManager)layout.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(layout.getApplicationWindowToken(), 0);
	}
}