package org.ammbra.advent.surprise;

import org.json.JSONObject;
import java.util.Currency;

public record Present(double itemPrice, double boxPrice, Currency currency) implements Intention {

	@Override
	public JSONObject mapToJSON() {
		var JSON = StringTemplate.Processor.of(
				(StringTemplate st) -> new JSONObject(st.interpolate())
		);

		return JSON. """
				{
				    "currency": "\{currency.toString()}",
				    "boxPrice": "\{boxPrice}",
				    "packaged" : "\{ boxPrice > 0.0}",
				    "cost": "\{(boxPrice > 0.0) ? itemPrice + boxPrice : itemPrice}"
				};
				""" ;
	}
}
