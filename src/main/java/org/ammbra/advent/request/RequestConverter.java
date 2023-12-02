package org.ammbra.advent.request;

import org.ammbra.advent.surprise.Celebration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RequestConverter {
	static Logger logger = Logger.getLogger(RequestConverter.class.getName());

	private RequestConverter() {
	}

	public static RequestData fromJSON(JSONObject jsonObject) {
		RequestData.Builder builder = new RequestData.Builder();

		if (jsonObject.keySet().containsAll(Arrays.asList("sender", "receiver", "celebration", "option"))) {
			for (String key : jsonObject.keySet()) {
				switch (key) {
					case "sender" -> builder.sender(jsonObject.optString(key));
					case "receiver" -> builder.receiver(jsonObject.optString(key));
					case "celebration" -> builder.celebration(jsonObject.optEnum(Celebration.class, key));
					case "option" -> builder.choice(jsonObject.optEnum(Choice.class, key));
					case "itemPrice" -> builder.itemPrice(jsonObject.optDouble(key));
					case "boxPrice" -> builder.boxPrice(jsonObject.optDouble(key));
				}

			}
		}

		return builder.build();
	}

	public static JSONObject asJSONObject(InputStream reqBody) throws IOException {
		JSONObject json = null;
		StringBuilder sb = new StringBuilder();
		try (InputStreamReader sr = new InputStreamReader(reqBody)) {
			char[] buf = new char[1024];
			int len;
			while ((len = sr.read(buf)) > 0) {
				sb.append(buf, 0, len);
			}
		}

		try {
			json = new JSONObject(sb.toString());
		} catch (JSONException jsonException) {
			logger.log(Level.SEVERE, jsonException.toString());
		}

		return json;
	}

}