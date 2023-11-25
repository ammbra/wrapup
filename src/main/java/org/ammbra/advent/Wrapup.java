package org.ammbra.advent;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.ammbra.advent.surprise.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.concurrent.Executors;


record Wrapup() implements HttpHandler {

	void main() throws IOException {
		var server = HttpServer.create(
				new InetSocketAddress("", 8081), 0);
		var address = server.getAddress();
		server.createContext("/", new Wrapup());
		server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
		server.start();
		System.out.printf("http://%s:%d%n",address.getHostString(), address.getPort());
	}


	@Override
	public void handle(HttpExchange exchange) throws IOException {
		int statusCode = 200;

		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			statusCode = 400;
		}

		// Get the request body input stream
		InputStream reqBody = exchange.getRequestBody();

		// Read JSON string from the input stream
		String reqData = convertToString(reqBody);
        JSONObject reqJson = new JSONObject(reqData);
		String sender = reqJson.optString("sender");
		String receiver = reqJson.optString("receiver");
        Celebration celebration = Celebration.valueOf(reqJson.optString("celebration"));
        Choice choice = Choice.valueOf(reqJson.optString("option"));
		double itemPrice = reqJson.optDouble("itemPrice");
		double boxPrice = reqJson.optDouble("boxPrice");

		Postcard postcard = new Postcard(sender, receiver, celebration);
		Intention intention = switch (choice) {
			case NONE -> new Coupon(0.0, null, Currency.getInstance("USD"));
			case COUPON -> {
				LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
				yield new Coupon(itemPrice, localDate, Currency.getInstance("USD"));
			}
			case EXPERIENCE -> new Experience(itemPrice, Currency.getInstance("EUR"));
			case PRESENT -> new Present(itemPrice, boxPrice, Currency.getInstance("RON"));
		};

		Gift gift = new Gift(postcard, intention);

		JSONObject json = switch (gift) {
			case Gift(Postcard _, Postcard _) -> {
				String message = "You cannot send two postcards!";
				throw new UnsupportedOperationException(message);
			}
			case Gift(Postcard p, Coupon c)
					when (c.price() == 0.0) -> p.asJSON();
			case Gift(_, Coupon _), Gift(_, Experience _),
					Gift(_, Present _) -> {
				String option = choice.name().toLowerCase();
				yield gift.merge(option);
			}
		};

		exchange.sendResponseHeaders(statusCode, 0);

		try (var stream = exchange.getResponseBody()) {
			stream.write(json.toString().getBytes());
		}

	}

	private String convertToString(InputStream reqBody) throws IOException {
		StringBuilder sb = new StringBuilder();
        try (InputStreamReader sr = new InputStreamReader(reqBody)) {
            char[] buf = new char[1024];
            int len;
            while ((len = sr.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }
        }
		return sb.toString();
	}
}

enum Choice {NONE, COUPON, EXPERIENCE, PRESENT}

