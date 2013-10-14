package cn.edu.dhu.login;

public class LoginFailException extends Exception {
	private String msg;
	
	public LoginFailException(String msg) {
    	if(msg==null)
    		msg = "unknown reason.";
    	msg = "<font color=\"#C80000\">Failed, </font>" + msg;
    }
	
	@Override
	public String toString() {
		return msg;
	}
}

