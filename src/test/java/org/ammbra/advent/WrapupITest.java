package org.ammbra.advent;

import org.ammbra.advent.request.Choice;
import org.ammbra.advent.surprise.Celebration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WrapupITest {

	@Test
	void testMainInvalidPostcard() {
		List<HttpRequest> requests = new ArrayList<>();
		for (Celebration celebration : Celebration.values()) {
			String data = STR. """
								{
									"sender": [{
									   "device_name": "Phone",
									   "key_category": 1
									}, {
									   "power_state": 3,
									   "reason": 1
									}],
									"receiver": "Duke",
									"celebration": "\{ celebration}",
									"option" : "Invalid"
								}
							""" ;
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:8081"))
					.POST(HttpRequest.BodyPublishers.ofString(data))
					.build();
			requests.add(request);
		}

		try (HttpClient client = HttpClient.newBuilder().build()) {
			List<HttpResponse<String>> responses = chain(requests, client);
			for (HttpResponse<String> response : responses) {
				assertEquals(400, response.statusCode());
				assertNotNull(response.body());
			}
		}
	}

	@Test
	void testMainOnlyPostcard() {
		List<HttpRequest> requests = new ArrayList<>();
		for (Celebration celebration : Celebration.values()) {
			String data = STR. """
								{
									"sender": "Ana",
									"receiver": "Duke",
									"celebration": "\{ celebration}",
									"option" : "\{ Choice.NONE }"
								}
							""" ;
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:8081"))
					.POST(HttpRequest.BodyPublishers.ofString(data))
					.build();
			requests.add(request);
		}

		try (HttpClient client = HttpClient.newBuilder().build()) {
			List<HttpResponse<String>> responses = chain(requests, client);
			for (HttpResponse<String> response : responses) {
				assertEquals(200, response.statusCode());
				assertNotNull(response.body());
			}
		}
	}

	@Test
	void testMainOnlyCoupon() {
		List<HttpRequest> requests = new ArrayList<>();
		Random random =  new Random();
		for (Celebration celebration : Celebration.values()) {
			String data = STR. """
								{
									"sender": "Ana",
									"receiver": "Duke",
									"celebration": "\{ celebration }",
									"option" : "\{ Choice.COUPON }",
									"itemPrice": "\{ random.nextDouble(2, 7) }"
								}
							""" ;
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:8081"))
					.POST(HttpRequest.BodyPublishers.ofString(data))
					.build();
			requests.add(request);
		}

		try (HttpClient client = HttpClient.newBuilder().build()) {
			List<HttpResponse<String>> responses = chain(requests, client);
			for (HttpResponse<String> response : responses) {
				assertEquals(200, response.statusCode());
				assertNotNull(response.body());
			}
		}
	}

	@Test
	void testMainOnlyExperience() {
		List<HttpRequest> requests = new ArrayList<>();
		Random random =  new Random();
		for (Celebration celebration : Celebration.values()) {
			String data = STR. """
								{
									"sender": "Ana",
									"receiver": "Duke",
									"celebration": "\{ celebration }",
									"option" : "\{ Choice.EXPERIENCE }",
									"itemPrice": "\{ random.nextDouble(2, 10) }"
								}
							""" ;
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:8081"))
					.POST(HttpRequest.BodyPublishers.ofString(data))
					.build();
			requests.add(request);
		}

		try (HttpClient client = HttpClient.newBuilder().build()) {
			List<HttpResponse<String>> responses = chain(requests, client);
			for (HttpResponse<String> response : responses) {
				assertEquals(200, response.statusCode());
				assertNotNull(response.body());
			}
		}
	}

	@Test
	void testMainOnlyPresent() {
		List<HttpRequest> requests = new ArrayList<>();
		Random random =  new Random();
		for (Celebration celebration : Celebration.values()) {
			String data = STR. """
								{
									"sender": "Ana",
									"receiver": "Duke",
									"celebration": "\{ celebration }",
									"option" : "\{ Choice.PRESENT }",
									"itemPrice": "\{ random.nextDouble(5, 10) }",
									"boxPrice": "\{ random.nextDouble(0.2, 0.7) }",
								}
							""" ;
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:8081"))
					.POST(HttpRequest.BodyPublishers.ofString(data))
					.build();
			requests.add(request);
		}

		try (HttpClient client = HttpClient.newBuilder().build()) {
			List<HttpResponse<String>> responses = chain(requests, client);
			for (HttpResponse<String> response : responses) {
				assertEquals(200, response.statusCode());
				assertNotNull(response.body());
			}
		}
	}

	private static List<HttpResponse<String>> chain(List<HttpRequest> requests, HttpClient client) {

		return requests.stream()
				.map(request -> {
					try {
						return client.send(request, HttpResponse.BodyHandlers.ofString());
					} catch (IOException | InterruptedException e) {
						throw new RuntimeException("Cannot send request " , e);
					}
				}).toList();
	}


}

