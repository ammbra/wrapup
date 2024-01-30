package org.ammbra.advent.surprise;

import org.json.JSONObject;

import java.util.Currency;
import java.util.Objects;

public record Experience(double price, Currency currency) implements Intention {

	public Experience {
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
				    "cost": "\{price}"
				}
				""" ;
	}

}
