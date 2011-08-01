package com.Rest2Go;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Utils {
	
	public static final String BASE_URL = "http://moveeat.net76.net/MoveEat/API/GetRestDetails.php";
	final static String TAG = "[MoveEat]";
	
	public static String preformRequest(String url){
		
		HttpGet httpget = new HttpGet(url);

		HttpClient httpclient = new DefaultHttpClient();
		log("executing request " + httpget.getURI());
		// Create a response handler
		HttpResponse responseBody;
		try {
			responseBody = httpclient.execute(httpget);

			HttpEntity entity = responseBody.getEntity();
			InputStream stream = entity.getContent();
			BufferedReader rd = new BufferedReader(new InputStreamReader(stream), 4096);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			log("response: " + sb.toString());
			return sb.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public static void log(String string){
		Log.i(TAG,string);
	}

}
