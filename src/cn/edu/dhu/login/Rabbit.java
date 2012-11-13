package cn.edu.dhu.login;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rabbit {

	private static String UA = "Mozilla/5.0 (compatible; DHU-network-login-helper/1.1)";
	private static String URL_POST = "https://securelogin.arubanetworks.com/auth/index.html/u";
	private static String URL_VERIFY = "http://localhost:8080/howareyou";
	private static String LOGIN_PAGE_ENC = "gbk";
	private static int TIME_OUT = 10000;
	static {
//		not working in android
//		System.setProperty("sun.net.client.defaultConnectTimeout", TIME_OUT);
//    	System.setProperty("sun.net.client.defaultReadTimeout", TIME_OUT);
	}
	
    public static String login(String username, String password) {
    	try {
    		int timeOut = 1000;
    		HttpURLConnection conn = urlopen(URL_VERIFY, timeOut);
			if("fine".equalsIgnoreCase(conn.getHeaderField("X-How-Are-You"))) {
				return urlreader(conn);
			}
    	} catch(UnknownHostException e) {
    		return urlerror("Ç×£¬Ã²ËÆÄã»¹Ã»ÓÐÁ¬ÍøÅ¶^_^");
		} catch (Exception e) {
		}
    	
    	try {
			String postData = urlencode(new String[] {"user", username, "password", password});
			urlopen(URL_POST, postData);
    	} catch (SocketTimeoutException e) {
    		return urlerror("Ç×£¬Ã²ËÆÄãµÄÍøÂç²»Ì«ÎÈ¶¨Ñ½^_^");
		} catch (Exception e) {
		}
		
        try {
        	HttpURLConnection conn = urlopen(URL_VERIFY);
//			if (!url.getHost().equals(urlConnection.getURL().getHost())) {
//			        we were redirected!
			if("fine".equalsIgnoreCase(conn.getHeaderField("X-How-Are-You"))) {
				return urlreader(conn);
			}
			throw new Exception("maybe try again later or maybe wrong username and password.");
        } catch (SocketTimeoutException e) {
    		return urlerror("Ç×£¬Ã²ËÆÄãµÄÍøÂç²»Ì«ÎÈ¶¨Ñ½^_^");
		} catch (Exception e) {
			return urlerror(e.getMessage());
		}
    }
    
    public static String urlencode(String[] queryParams) {
    	if(queryParams.length==0) return "";
    	if((queryParams.length&0x1)==1) throw new IllegalArgumentException("para length should be even.");
    	
    	StringBuilder sb = new StringBuilder();
    	try {
	    	for(int i=0; i<queryParams.length; i+=2) {    		
				sb.append(URLEncoder.encode(queryParams[i], LOGIN_PAGE_ENC)+"="+URLEncoder.encode(queryParams[i+1], LOGIN_PAGE_ENC));			
	    	    sb.append("&");
	    	}
    	} catch (UnsupportedEncodingException e) {
    		for(int i=0; i<queryParams.length; i+=2) {    		
				sb.append(URLEncoder.encode(queryParams[i])+"="+URLEncoder.encode(queryParams[i+1]));			
	    	    sb.append("&");
	    	}
    	}
    	return sb.deleteCharAt(sb.length()-1).toString();
    }
    
    public static HttpURLConnection urlopen(String url) throws IOException {
    	return urlopen(url, TIME_OUT);
    }
    
    public static HttpURLConnection urlopen(String url, int timeOut) throws IOException {
    	URL req = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)req.openConnection();
		conn.setRequestProperty("User-Agent", UA);
//		conn.setInstanceFollowRedirects(true); //the default one
		conn.setConnectTimeout(timeOut);
		conn.setReadTimeout(timeOut);		
//		conn.connect();
		conn.getInputStream(); //fk_java
//		conn.disconnect();
		return conn;
    }
    
    public static HttpURLConnection urlopen(String url, String data) throws IOException {
    	return urlopen(url, data, TIME_OUT);
    }
    
    public static HttpURLConnection urlopen(String url, String data, int timeOut) throws IOException {
    	URL req = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)req.openConnection();
		conn.setRequestProperty("User-Agent", UA);
		conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		conn.setFixedLengthStreamingMode(data.length());
		conn.setInstanceFollowRedirects(false);
		conn.setConnectTimeout(timeOut);
		conn.setReadTimeout(timeOut);
		conn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(data);
	    writer.flush();
	    writer.close();
//		conn.connect();
		conn.getInputStream(); //fk_java
//		conn.disconnect();
		return conn;
    }
    
    public static String urlreader(HttpURLConnection conn) {
    	String charset = "utf-8";
    	Pattern patt_charset = Pattern.compile("charset=(.+)$", Pattern.CASE_INSENSITIVE);
    	Matcher mat_charset = patt_charset.matcher(conn.getContentType());
    	if(mat_charset.find()){
    		charset = mat_charset.group(1);
    	}
    	try {
	        InputStreamReader ireader = new InputStreamReader(conn.getInputStream(), charset);
	        int content_length = conn.getContentLength()==-1 ? 8192:conn.getContentLength();
	        char[] quote = new char[content_length];
	        int len = ireader.read(quote);
	        return String.valueOf(quote, 0, len);
    	} catch (UnsupportedEncodingException e) {
    		return urlerror("¶î..·þÎñÆ÷Ã²ËÆ³ö¹ÊÕÏÁË, ²»ÈÏÊ¶µÄ±àÂë"+e.getMessage()+"¡£");
        } catch (IOException e) {
        	return urlerror(e.toString());
        }
    }
    
    public static String urlerror(String msg) {
    	if(msg==null) msg = "unknown reason.";
    	return "Failed!! " + msg + " -- rabbit";
    }
}