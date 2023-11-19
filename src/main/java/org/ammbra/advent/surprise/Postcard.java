package org.ammbra.advent.surprise;

import org.json.JSONObject;

public record Postcard(String sender, String receiver, Celebration celebration) {

	public JSONObject greet() {
		var JSON = StringTemplate.Processor.of(
				(StringTemplate st) -> new JSONObject(st.interpolate())
		);

		return JSON. """
				{
					"sender": "\{sender}",
				    "receiver": "\{receiver}",
				    "message": "\{celebration.getText()}"
				};
				""" ;
	}

}
