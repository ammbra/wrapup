package org.ammbra.advent.surprise;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public sealed interface Intention
		permits Coupon, Experience, Present, Postcard {

	StringTemplate.Processor<JSONObject, JSONException> JSON_VALIDATE =
			(StringTemplate st) -> {
				String quote = "\"";
				List<Object> filtered = validate(st, quote);
				String jsonSource = StringTemplate.interpolate(st.fragments(), filtered);
				return new JSONObject(jsonSource);
			};

	private static List<Object> validate(StringTemplate st, String quote) {
		List<Object> filtered = new ArrayList<>();
		for (Object value : st.values()) {
			if (value instanceof String str) {
				if (str.contains(quote)) {
					throw new JSONException("Injection vulnerability");
				}
				filtered.add(str);
			} else if (value instanceof Number || value instanceof Boolean ||
					value instanceof Currency || value instanceof LocalDate) {
				filtered.add(value);
			} else {
				throw new JSONException("Invalid value type");
			}
		}
		return filtered;
	}

	JSONObject asJSON();
}
