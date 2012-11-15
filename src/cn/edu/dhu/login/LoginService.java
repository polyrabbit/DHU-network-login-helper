package cn.edu.dhu.login;

import java.util.Random;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LoginService extends IntentService {
	private NotificationManager notiManager;
	private final int noti_id = new Random().nextInt();
	
	public LoginService() {
		super("LoginService");
	}
	
	
	@Override 
	protected void onHandleIntent(Intent intent) {
		((LoginServiceHome)getApplicationContext()).gohome(this);
		Intent it = new Intent(this, NotifyActivity.class);
		
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, it, 0);
		Bundle b = intent.getExtras();
		String username = b.getString("username");
		String passwd = b.getString("passwd");
		
		Notification noti = new Notification(R.drawable.ic_launcher_info, "正在努力的登录中...", 0);
		noti.flags = Notification.FLAG_ONGOING_EVENT;
		noti.setLatestEventInfo(this, "正在登录 "+username, "点此取消登录", pIntent);
		notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notiManager.notify(noti_id, noti);
		
		String quote =  Rabbit.login(username, passwd);
		if(!quote.startsWith("Failed!!")) {
			SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_name), Activity.MODE_PRIVATE);
	    	SharedPreferences.Editor editor = preferences.edit();
	    	editor.putString("username", username)
	    		.putString("passwd", passwd)
	    		.commit();
		}		
		
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		it.putExtra("quote", quote);
		startActivity(it);
    }
	
	@Override 
    public void onDestroy() {
		notiManager.cancel(noti_id);
		super.onDestroy();
	}
}
