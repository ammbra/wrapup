package org.ammbra.advent.surprise;

import org.json.JSONObject;


public record Gift(Postcard postcard, Intention intention) {

	public JSONObject composeToJSON(String option) {
		JSONObject intentionJSON = intention.mapToJSON();
		return postcard.greet().put(option, intentionJSON);
	}
}

