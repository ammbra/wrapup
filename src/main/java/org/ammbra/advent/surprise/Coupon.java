package org.ammbra.advent.surprise;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Currency;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;

import static org.ammbra.advent.Wrapup.VALID_REQUEST;

public record Coupon(double price, LocalDate expiringOn, Currency currency)
		implements Intention {

	private static final Random random = new Random();

	@Override
	public JSONObject asJSON() {
		return JSON. """
				{
				    "currency": "\{currency}",
				    "expiresOn" : "\{ expiringOn}",
				    "cost": "\{price}"
				};
				""" ;
	}

	public Coupon {
		if (!VALID_REQUEST.isBound()) {
			throw new IllegalStateException("The offer state is not bound");
		}
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
			return this.coupons.stream().toList().getFirst();
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
		double price = random.nextDouble(10, referencePrice);
		try {
			Thread.sleep((int) price );
			return new Coupon(price, localDate, Currency.getInstance("USD"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Coupon readOffer2(Double referencePrice) {
		LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
		double price = random.nextDouble(-5, referencePrice);
		try {
			Thread.sleep((int) price + 6);
			return new Coupon(price, localDate, Currency.getInstance("USD"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Coupon readOffer3(Double referencePrice) {
		LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
		double price = random.nextDouble(0, referencePrice);
		try {
			Thread.sleep((int) price);
			return new Coupon(price, localDate, Currency.getInstance("USD"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Coupon readOffer4(Double referencePrice) {
		LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
		double price = random.nextDouble(1, referencePrice);
		try {
			Thread.sleep((int) price);
			return new Coupon(price, localDate, Currency.getInstance("USD"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
