package cn.edu.dhu.login;

import android.app.Application;
import android.app.IntentService;

public class LoginServiceHome extends Application {
	private IntentService loginService;
	
	public void gohome(IntentService ls) {
		this.loginService = ls;
	}
	
	public IntentService comeout() {
		return this.loginService;
	}
}
