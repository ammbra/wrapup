package org.ammbra.advent.surprise;

import org.json.JSONObject;
import java.util.Currency;
import java.util.Objects;

public record Present(double itemPrice, double boxPrice,
					  Currency currency) implements Intention {

	public Present {
		Objects.requireNonNull(currency, "currency is required");
		if (itemPrice < 0) {
			throw new IllegalArgumentException("Price of an item cannot be negative");
		} else if (boxPrice < 0) {
			throw new IllegalArgumentException("Price of the box cannot be negative");
		}
	}

	@Override
	public JSONObject asJSON() {

		return JSON_VALIDATE. """
				{
				    "currency": "\{currency}",
				    "boxPrice": "\{boxPrice}",
				    "packaged" : "\{ boxPrice > 0.0}",
				    "cost": "\{(boxPrice > 0.0) ? itemPrice + boxPrice : itemPrice}"
				}
				""" ;
	}
}
