package org.ammbra.advent.surprise;

import org.json.JSONObject;
import java.time.LocalDate;
import java.util.Currency;

public record Coupon(double price, LocalDate expiringOn, Currency currency)
		implements Intention {

	@Override
	public JSONObject mapToJSON() {
		var JSON = StringTemplate.Processor.of(
				(StringTemplate st) ->
						new JSONObject(st.interpolate())
		);

		return JSON. """
				{
				    "currency": "\{currency.toString()}",
				    "expiresOn" : "\{ expiringOn}",
				    "cost": "\{price}"
				};
				""" ;
	}
}
