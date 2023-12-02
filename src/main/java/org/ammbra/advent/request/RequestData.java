package org.ammbra.advent.request;

import org.ammbra.advent.surprise.Celebration;


public record RequestData(String sender, String receiver,
						  Celebration celebration, Choice choice,
						  double itemPrice, double boxPrice) {

	private RequestData(Builder builder) {
		this(builder.sender, builder.receiver,
				builder.celebration, builder.choice,
				builder.itemPrice, builder.boxPrice);
	}

	public static class Builder {
		private String sender;
		private String receiver;
		private Celebration celebration;
		private Choice choice;
		private double itemPrice;
		private double boxPrice;

		public Builder sender(String sender) {
			this.sender = sender;
			return this;
		}

		public Builder receiver(String receiver) {
			this.receiver = receiver;
			return this;
		}

		public Builder celebration(Celebration celebration) {
			this.celebration = celebration;
			return this;
		}

		public Builder choice(Choice choice) {
			this.choice = choice;
			return this;
		}

		public Builder itemPrice(double itemPrice) {
			this.itemPrice = itemPrice;
			return this;
		}

		public Builder boxPrice(double boxPrice) {
			this.boxPrice = boxPrice;
			return this;
		}

		public RequestData build() throws IllegalStateException {
			return new RequestData(this);
		}
	}
}
