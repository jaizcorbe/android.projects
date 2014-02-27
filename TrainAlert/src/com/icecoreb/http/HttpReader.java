package com.icecoreb.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpReader {
	public String readUrl(String url) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		InputStream inputStream = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} else {
				Log.d("HTTP read fail", "Failed to read url: [" + url
						+ "] http code: [" + statusCode + "]");
			}
		} catch (Exception e) {
			Log.d("HTTP read exception", e.getLocalizedMessage(), e);
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Log.d("HTTP read fail", "Failed to close input stream", e);
				}
			}
		}
		return stringBuilder.toString();
	}
}
