package org.ammbra.advent.surprise;

import org.json.JSONObject;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;

public record Coupon(double price, LocalDate expiringOn, Currency currency)
		implements Intention {

	public Coupon {
		Objects.requireNonNull(currency, "currency is required");
		if (price < 0) {
			throw new IllegalArgumentException("Price of an item cannot be negative");
		}
	}

	@Override
	public JSONObject asJSON() {
		return JSON_VALIDATE. """
				{
				    "currency": "\{currency}",
				    "expiresOn" : "\{ expiringOn}",
				    "cost": "\{price}"
				}
				""" ;
	}
}
