package org.ammbra.advent.surprise;

import org.json.JSONObject;
import java.util.Objects;

public record Postcard(String sender, String receiver, Celebration celebration) implements Intention {

	public Postcard {
		Objects.requireNonNull(sender, "sender is required");
		Objects.requireNonNull(receiver, "receiver is required");
		Objects.requireNonNull(celebration, "celebration is required");
	}

	public JSONObject asJSON() {
		return JSON_VALIDATE. """
				{
					"sender": "\{sender}",
				    "receiver": "\{receiver}",
				    "message": "\{celebration.getText()}"
				}
				""" ;
	}

}
