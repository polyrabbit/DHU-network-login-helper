package cn.edu.dhu.login;

public class LoginFailException extends Exception {
	private String msg;
	
	public LoginFailException(String msg) {
    	if(msg==null) msg = "unknown reason.";
    	msg = "Failed!! " + msg + " -- rabbit";
    }
	
	@Override
	public String toString() {
		return msg;
	}
}

