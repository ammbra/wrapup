package org.ammbra.advent.surprise;

import org.json.JSONObject;

public sealed interface Intention permits Coupon, Experience, Present {

	JSONObject mapToJSON();
}
