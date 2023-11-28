package org.ammbra.advent.surprise;

import org.json.JSONObject;

public sealed interface Intention
		permits Coupon, Experience, Present, Postcard {

	StringTemplate.Processor<JSONObject, RuntimeException> JSON = StringTemplate.Processor.of(
			(StringTemplate st) -> new JSONObject(st.interpolate())
	);

	JSONObject asJSON();
}
