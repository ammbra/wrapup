package org.ammbra.advent.surprise;

import io.github.ralfspoeth.json.JsonObject;

import java.util.Currency;
import java.util.Objects;

import static io.github.ralfspoeth.json.Aggregate.objectBuilder;

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
    public JsonObject toJsonObject() {
        return objectBuilder()
                .named("currency", currency)
                .named("boxPrice", boxPrice)
                .named("packaged", boxPrice > 0)
                .named("cost", (boxPrice > 0) ? itemPrice + boxPrice : itemPrice)
                .build();
    }
}
