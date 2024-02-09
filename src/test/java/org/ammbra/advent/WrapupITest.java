package org.ammbra.advent;

import org.ammbra.advent.request.Choice;
import org.ammbra.advent.surprise.decor.Celebration;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WrapupITest {

	@Test
	void testMainInvalidPostcard() throws ExecutionException, InterruptedException {
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
			CompletableFuture<List<HttpResponse<String>>> combinedFutures = toCombinedFuture(requests, client);
			combinedFutures.get().forEach((response) -> {
				assertEquals(400, response.statusCode());
				assertNotNull(response.body());
			});
		}
	}


	@Test
	void testMainOnlyPostcard() throws ExecutionException, InterruptedException {
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
			CompletableFuture<List<HttpResponse<String>>> combinedFutures = toCombinedFuture(requests, client);
			combinedFutures.get().forEach((response) -> {
				assertEquals(200, response.statusCode());
				assertNotNull(response.body());
			});
		}
	}

	@Test
	void testMainOnlyCoupon() throws ExecutionException, InterruptedException {
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
			CompletableFuture<List<HttpResponse<String>>> combinedFutures = toCombinedFuture(requests, client);
			combinedFutures.get().forEach((response) -> {
				assertEquals(200, response.statusCode());
				assertNotNull(response.body());
			});
		}
	}

	@Test
	void testMainOnlyExperience() throws ExecutionException, InterruptedException {
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
			CompletableFuture<List<HttpResponse<String>>> combinedFutures = toCombinedFuture(requests, client);
			combinedFutures.get().forEach((response) -> {
				assertEquals(200, response.statusCode());
				assertNotNull(response.body());
			});
		}
	}

	@Test
	void testMainOnlyPresent() throws ExecutionException, InterruptedException {
		List<HttpRequest> requests = new ArrayList<>();
		Random random =  new Random();
		for (Celebration celebration : Celebration.values()) {
			String data = STR. """
								{
									"sender": "Ana",
									"receiver": "Duke",
									"celebration": "\{ celebration }",
									"option" : "\{ Choice.PRESENT }",
									"itemPrice": "\{ random.nextDouble(5, 10) }"
								}
							""" ;
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://127.0.0.1:8081"))
					.POST(HttpRequest.BodyPublishers.ofString(data))
					.build();
			requests.add(request);
		}

		try (HttpClient client = HttpClient.newBuilder().build()) {
			CompletableFuture<List<HttpResponse<String>>> combinedFutures = toCombinedFuture(requests, client);
			combinedFutures.get().forEach((response) -> {
				assertEquals(200, response.statusCode());
				assertNotNull(response.body());
			});
		}
	}

	private static CompletableFuture<List<HttpResponse<String>>> toCombinedFuture(List<HttpRequest> requests, HttpClient client) {
		List<CompletableFuture<HttpResponse<String>>> completableFutures = requests.stream()
				.map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
				.toList();

		return CompletableFuture
				.allOf(completableFutures.toArray(new CompletableFuture[0]))
				.thenApply(_ -> completableFutures.stream()
						.map(CompletableFuture::join)
						.toList());
	}


}


