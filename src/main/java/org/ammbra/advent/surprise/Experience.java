package org.ammbra.advent.surprise;

import org.ammbra.advent.request.Choice;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;

import static org.ammbra.advent.Wrapup.VALID_REQUEST;

public record Experience(double price, Currency currency) implements Intention {

	private static final Random random = new Random();

	public Experience {
		Objects.requireNonNull(currency, "currency is required");
		if (price < 0) {
			throw new IllegalArgumentException("Price of an item cannot be negative");
		}

		if (!VALID_REQUEST.isBound()) {
			throw new IllegalStateException("The request state is not bound");
		} else if (!VALID_REQUEST.get()
				.equals(Choice.EXPERIENCE)) {
			throw new IllegalStateException("Request state is " + VALID_REQUEST.get());
		}
	}

	@Override
	public JSONObject asJSON() {

		return JSON_VALIDATE. """
				{
				    "currency": "\{currency}",
				    "cost": "\{price}"
				}
				""" ;
	}


	public static class ExperienceScope extends StructuredTaskScope<Experience> {

		private final Collection<Experience> experiences = new ConcurrentLinkedQueue<>();

		@Override
		protected void handleComplete(Subtask<? extends Experience> subtask) {
			switch (subtask.state()) {
				case UNAVAILABLE -> throw new IllegalStateException("Experience details pending...");
				case SUCCESS -> this.experiences.add(subtask.get());
				case FAILED -> subtask.exception();
			}
		}

		public Experience findOffer() {
			return this.experiences.stream()
					.min(Comparator.comparing(Experience::price))
					.orElseThrow(IllegalStateException::new);
		}
	}

	public static Experience findOffer(Double referencePrice) {

		try (var scope = new Experience.ExperienceScope()) {

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

	public static Experience readOffer1(Double referencePrice) {
		double price = random.nextDouble(0.01, referencePrice);
		try {
			Thread.sleep((int) price + 11);
			return new Experience(price, Currency.getInstance("EUR"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Experience readOffer2(Double referencePrice) {
		double price = random.nextDouble(0.5, referencePrice);
		try {
			Thread.sleep((int) price + 6);
			return new Experience(price, Currency.getInstance("EUR"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Experience readOffer3(Double referencePrice) {
		double price = random.nextDouble(0.3, referencePrice);
		try {
			Thread.sleep((int) price +  4);
			return new Experience(price, Currency.getInstance("EUR"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Experience readOffer4(Double referencePrice) {
		double price = random.nextDouble(0.1, referencePrice);
		try {
			Thread.sleep((int) price);
			return new Experience(price, Currency.getInstance("EUR"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
