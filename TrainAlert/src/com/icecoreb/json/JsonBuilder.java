package com.icecoreb.json;

import org.json.JSONArray;

public class JsonBuilder {
	public JSONArray getJason(String jsonContent) throws Exception {
		JSONArray jsonArray = new JSONArray(jsonContent);
		return jsonArray;
	}
}
