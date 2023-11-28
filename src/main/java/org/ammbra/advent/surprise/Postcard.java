package org.ammbra.advent.surprise;

import org.json.JSONObject;

public record Postcard(String sender, String receiver, Celebration celebration) implements Intention {

	public JSONObject asJSON() {
		return JSON. """
				{
					"sender": "\{sender}",
				    "receiver": "\{receiver}",
				    "message": "\{celebration.getText()}"
				};
				""" ;
	}

}
