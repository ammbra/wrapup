package org.ammbra.advent.surprise;

import io.github.ralfspoeth.json.Aggregate;
import io.github.ralfspoeth.json.JsonObject;

public record Gift(Postcard postcard, Intention intention) {

	public JsonObject merge(String option) {
		var intentionJSON = intention.toJsonObject();
		var ob = Aggregate.objectBuilder();
		var pc = postcard.toJsonObject();
		pc.members().entrySet().forEach(e -> ob.named(e.getKey(), e.getValue()));
		ob.named(option, intentionJSON);
		return ob.build();
	}
}

