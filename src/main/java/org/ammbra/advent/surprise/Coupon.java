package org.ammbra.advent.surprise;

import org.ammbra.advent.request.Choice;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Currency;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;

import static org.ammbra.advent.Wrapup.VALID_REQUEST;

public record Coupon(double price, LocalDate expiringOn, Currency currency)
		implements Intention {

	private static final Random random = new Random();

	public Coupon {
		Objects.requireNonNull(currency, "currency is required");
		if (price < 0) {
			throw new IllegalArgumentException("Price of an item cannot be negative");
		}

		if (!VALID_REQUEST.isBound()) {
			throw new IllegalStateException("The request state is not bound");
		} else if (!VALID_REQUEST.get().equals(Choice.COUPON)) {
			throw new IllegalStateException("Request state is " + VALID_REQUEST.get());
		}
	}

	@Override
	public JSONObject asJSON() {
		return JSON_VALIDATE. """
				{
				    "currency": "\{currency}",
				    "expiresOn" : "\{ expiringOn}",
				    "cost": "\{price}"
				}
				""" ;
	}

	public static class CouponScope extends StructuredTaskScope<Coupon> {

		private final Collection<Coupon> coupons = new ConcurrentLinkedQueue<>();

		@Override
		protected void handleComplete(Subtask<? extends Coupon> subtask) {
			switch (subtask.state()) {
				case UNAVAILABLE -> throw new IllegalStateException("Coupon details pending...");
				case SUCCESS -> this.coupons.add(subtask.get());
				case FAILED -> subtask.exception();
			}
		}

		public Coupon findOffer() {
			return this.coupons.stream().findFirst()
					.orElseThrow(IllegalStateException::new);
		}
	}

	public static Coupon findOffer(Double referencePrice) {

		try (var scope = new CouponScope()) {

			scope.fork(() -> readOffer1(referencePrice));
			scope.fork(() -> readOffer2(referencePrice));
			scope.fork(() -> readOffer3(referencePrice));
			scope.fork(() -> readOffer4(referencePrice));

			scope.join();

			return scope.findOffer();

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Coupon readOffer1(Double referencePrice) {
		LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
		double price = random.nextDouble(1, referencePrice);
		try {
			Thread.sleep((int) price );
			return new Coupon(price, localDate, Currency.getInstance("USD"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Coupon readOffer2(Double referencePrice) {
		LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
		double price = random.nextDouble(0.3, referencePrice);
		try {
			Thread.sleep((int) price + 6);
			return new Coupon(price, localDate, Currency.getInstance("USD"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Coupon readOffer3(Double referencePrice) {
		LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
		double price = random.nextDouble(0.1, referencePrice);
		try {
			Thread.sleep((int) price);
			return new Coupon(price, localDate, Currency.getInstance("USD"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Coupon readOffer4(Double referencePrice) {
		LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
		double price = random.nextDouble(0.2, referencePrice);
		try {
			Thread.sleep((int) price);
			return new Coupon(price, localDate, Currency.getInstance("USD"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
