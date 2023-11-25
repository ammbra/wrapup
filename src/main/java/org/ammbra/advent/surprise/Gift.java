package org.ammbra.advent.surprise;

import org.json.JSONObject;


public record Gift(Postcard postcard, Intention intention) {

	public JSONObject merge(String option) {
		JSONObject intentionJSON = intention.asJSON();
		return postcard.asJSON().put(option, intentionJSON);
	}
}

