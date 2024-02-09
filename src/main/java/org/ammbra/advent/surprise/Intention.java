package org.ammbra.advent.surprise;

import org.ammbra.advent.surprise.decor.Font;
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
				JSONObject json = new JSONObject(jsonSource);
				return json;
			};

	private static List<Object> validate(StringTemplate st, String quote) {
		var mapping = st.values().stream()
				.map(value -> switch (value) {
					case Number n -> n;
					case Boolean b -> b;
					case Font f -> f;
					case Currency c -> c;
					case LocalDate date -> date;
					case String str when str.contains(quote) ->
							new JSONException("Injection vulnerability");
					case String str -> str;
					case Object _ -> new JSONException("Invalid value type");
				}).toList();
		var exceptions = mapping.stream()
				.filter(RuntimeException.class::isInstance)
				.map(JSONException.class::cast) // could be RuntimeException
				.toList();
		var exception = new JSONException("Contains all the exceptions");
		exceptions.forEach(exception::addSuppressed);
		return new ArrayList<>(mapping);
	}

	JSONObject asJSON();
}
