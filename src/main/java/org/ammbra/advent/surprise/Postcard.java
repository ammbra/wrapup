package org.ammbra.advent.surprise;

import io.github.ralfspoeth.json.JsonObject;

import java.util.Objects;

import static io.github.ralfspoeth.json.Aggregate.objectBuilder;

public record Postcard(String sender, String receiver, Celebration celebration) implements Intention {

	public Postcard {
		Objects.requireNonNull(sender, "sender is required");
		Objects.requireNonNull(receiver, "receiver is required");
		Objects.requireNonNull(celebration, "celebration is required");
	}

	public JsonObject toJsonObject() {
		return objectBuilder()
				.named("sender", sender)
				.named("receiver", receiver)
				.named("celebration", celebration)
				.build();
	}

}
