package org.ammbra.advent.surprise;

import org.json.JSONObject;

import java.util.Currency;

public record Experience(double price, Currency currency) implements Intention {

	@Override
	public JSONObject asJSON() {

		return JSON. """
				{
				    "currency": "\{currency}",
				    "cost": "\{price}"
				};
				""" ;
	}

}
