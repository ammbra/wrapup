package org.ammbra.advent;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.ammbra.advent.request.Choice;
import org.ammbra.advent.request.RequestConverter;
import org.ammbra.advent.request.RequestData;
import org.ammbra.advent.surprise.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import static org.ammbra.advent.surprise.Coupon.findOffer;


public record Wrapup() implements HttpHandler {
	public static ScopedValue<Choice> VALID_REQUEST = ScopedValue.newInstance();

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

		// Read JSON from the input stream
		JSONObject req = RequestConverter.asJSONObject(reqBody);
		JSONObject response;

		if (!req.isEmpty()) {
			RequestData data = RequestConverter.fromJSON(req);
			Postcard postcard = new Postcard(data.sender(), data.receiver(), data.celebration());
			Intention intention = detectIntention(postcard, data);
			Gift gift = new Gift(postcard, intention);
			response = process(gift, data.choice());
		} else {
			response =  new JSONObject("error", "Empty request");
		}

		exchange.sendResponseHeaders(statusCode, 0);

		try (var stream = exchange.getResponseBody()) {
			stream.write(response.toString().getBytes());
		}
	}

	JSONObject process(Gift gift, Choice choice) {
		return switch (gift) {
			case Gift(Postcard p, Postcard _) -> p.asJSON();
			case Gift(Postcard p, Coupon c) when (c.price() == 0.0) -> p.asJSON();
			case Gift(Postcard p, Experience e) when (e.price() == 0.0) -> p.asJSON();
			case Gift(Postcard p, Present pr) when (pr.itemPrice() == 0.0) -> p.asJSON();
			case Gift(_, Coupon _), Gift(_, Experience _), Gift(_, Present _) -> {
				String option = choice.name().toLowerCase();
				yield gift.merge(option);
			}
		};
	}


	Intention detectIntention( Postcard postcard, RequestData data) {
		return switch (data.choice()) {
			case NONE -> postcard;
			case COUPON -> {
				try {
					yield ScopedValue.where(VALID_REQUEST, Choice.COUPON).call(() -> findOffer(data.itemPrice()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			case EXPERIENCE -> {
				try {
					yield ScopedValue.where(VALID_REQUEST, Choice.EXPERIENCE).call(() -> findOffer(data.itemPrice()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			case PRESENT -> Present.findOffer(data.itemPrice(), data.boxPrice());
		};
	}


}

