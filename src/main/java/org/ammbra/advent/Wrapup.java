package org.ammbra.advent;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.ammbra.advent.surprise.*;
import org.ammbra.advent.surprise.Coupon;
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
		String sender = reqJson.get("sender").toString();
		String receiver = reqJson.get("receiver").toString();
        Celebration celebration = Celebration.valueOf(reqJson.get("celebration").toString());
        Option option = Option.valueOf(reqJson.get("option").toString());
		double itemPrice = 0.0;
		if (!Double.valueOf(reqJson.optDouble("itemPrice")).isNaN()) {
			itemPrice = Double.parseDouble(reqJson.get("itemPrice").toString());
		}

		if (!Double.valueOf(reqJson.optDouble("boxPrice")).isNaN()) {
            double boxPrice = Double.parseDouble(reqJson.get("boxPrice").toString());
        }

		Postcard postcard = new Postcard(sender, receiver, celebration);
		Intention intention = switch (option) {
			case NONE -> new Coupon(0.0, null, Currency.getInstance("USD"));
			case COUPON -> {
				LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
				yield new Coupon(itemPrice, localDate, Currency.getInstance("USD"));
			}
			case EXPERIENCE -> new Experience(itemPrice, Currency.getInstance("EUR"));
			case PRESENT -> new Present(itemPrice, 2.0, Currency.getInstance("RON"));
		};

		Gift gift = new Gift(postcard, intention);

		JSONObject json = switch (gift) {
			case Gift(Postcard p, Coupon c) when (c.price() == 0.0) -> p.greet();
			case Gift(_, Coupon _) -> gift.composeToJSON(Option.COUPON.name().toLowerCase());
			case Gift(_, Experience _) -> gift.composeToJSON(Option.EXPERIENCE.name().toLowerCase());
			case Gift(_, Present _) -> gift.composeToJSON(Option.PRESENT.name().toLowerCase());
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

enum Option {NONE, COUPON, EXPERIENCE, PRESENT}

