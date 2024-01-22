package org.ammbra.advent.surprise;

import io.github.ralfspoeth.json.JsonObject;

import java.util.Currency;
import java.util.Objects;

import static io.github.ralfspoeth.json.Aggregate.objectBuilder;

public record Experience(double price, Currency currency) implements Intention {

    public Experience {
        Objects.requireNonNull(currency, "currency is required");
        if (price < 0) {
            throw new IllegalArgumentException("Price of an item cannot be negative");
        }
    }

    @Override
    public JsonObject toJsonObject() {
        return objectBuilder()
                .named("currency", currency)
                .named("cost", price)
                .build();
    }

}
