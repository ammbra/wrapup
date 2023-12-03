package org.ammbra.advent.surprise;

import org.json.JSONObject;
import java.util.Currency;

public record Present(double itemPrice, double boxPrice,
					  Currency currency) implements Intention {

	@Override
	public JSONObject asJSON() {

		return JSON. """
				{
				    "currency": "\{currency}",
				    "boxPrice": "\{boxPrice}",
				    "packaged" : "\{ boxPrice > 0.0}",
				    "cost": "\{(boxPrice > 0.0) ? itemPrice + boxPrice : itemPrice}"
				}
				""" ;
	}
}
