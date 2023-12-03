package org.ammbra.advent.surprise;

import org.json.JSONObject;
import java.time.LocalDate;
import java.util.Currency;

public record Coupon(double price, LocalDate expiringOn, Currency currency)
		implements Intention {

	@Override
	public JSONObject asJSON() {
		return JSON. """
				{
				    "currency": "\{currency}",
				    "expiresOn" : "\{ expiringOn}",
				    "cost": "\{price}"
				}
				""" ;
	}
}
