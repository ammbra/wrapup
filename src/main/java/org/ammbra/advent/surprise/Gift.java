package org.ammbra.advent.surprise;

import io.github.ralfspoeth.json.JsonObject;

import java.util.HashMap;

public record Gift(Postcard postcard, Intention intention) {

	public JsonObject merge(String option) {
		var intntnJson = intention.toJsonObject();
		var pcJson = postcard.toJsonObject();
		var m = new HashMap<>(pcJson.members());
		m.put(option, intntnJson);
		return new JsonObject(m); // makes an immutable copy internally
	}
}

