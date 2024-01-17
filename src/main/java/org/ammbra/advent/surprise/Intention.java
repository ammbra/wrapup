package org.ammbra.advent.surprise;

import io.github.ralfspoeth.json.JsonObject;

public sealed interface Intention
		permits Coupon, Experience, Present, Postcard
{
	JsonObject toJsonObject();
}
