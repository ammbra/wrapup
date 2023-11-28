package org.ammbra.advent.request;

import org.ammbra.advent.surprise.Celebration;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class RequestConverter {

	private RequestConverter() {
	}

	public static RequestData fromJSON(JSONObject jsonObject) {
		RequestData.Builder builder = new RequestData.Builder();
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
		return builder.build();
	}

	public static JSONObject asJSONObject(InputStream reqBody) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (InputStreamReader sr = new InputStreamReader(reqBody)) {
			char[] buf = new char[1024];
			int len;
			while ((len = sr.read(buf)) > 0) {
				sb.append(buf, 0, len);
			}
		}
		return new JSONObject(sb.toString());
	}

}