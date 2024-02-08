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
				List<Object> filtered = new ArrayList<>();
				for (Object value : st.values()) {
					switch (value) {
						case String str when str.contains(quote) -> throw new JSONException("Injection vulnerability");
						case String str -> filtered.add(str);
						case Number n -> filtered.add(n);
						case Boolean b -> filtered.add(b);
						case Currency c -> filtered.add(c);
						case LocalDate date -> filtered.add(date);
						case Object _ -> throw new JSONException("Invalid value type");
					}
				}
				String jsonSource =
						StringTemplate.interpolate(st.fragments(), filtered);
				return new JSONObject(jsonSource);
			};

	JSONObject asJSON();
}
