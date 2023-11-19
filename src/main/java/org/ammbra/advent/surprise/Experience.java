package org.ammbra.advent.surprise;

import org.json.JSONObject;

import java.util.Currency;

public record Experience(double price, Currency currency) implements Intention {

	@Override
	public JSONObject mapToJSON() {
		var JSON = StringTemplate.Processor.of(
				(StringTemplate st) -> new JSONObject(st.interpolate())
		);

		JSONObject json = JSON. """
				{
				    "currency": "\{currency.toString()}",
				    "cost": "\{price}"
				};
				""";
		return json;
	}

}
