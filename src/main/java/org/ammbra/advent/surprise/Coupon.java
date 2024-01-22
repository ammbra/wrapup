package org.ammbra.advent.surprise;

import io.github.ralfspoeth.json.JsonObject;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;

import static io.github.ralfspoeth.json.Aggregate.objectBuilder;

public record Coupon(double price, LocalDate expiringOn, Currency currency)
		implements Intention {

	public Coupon {
		Objects.requireNonNull(currency, "currency is required");
		if (price < 0) {
			throw new IllegalArgumentException("Price of an item cannot be negative");
		}
	}

	@Override
	public JsonObject toJsonObject() {
		return objectBuilder()
				.named("currency", currency)
				.named("expiresOn", expiringOn)
				.named("cost", price)
				.build();
	}
}
