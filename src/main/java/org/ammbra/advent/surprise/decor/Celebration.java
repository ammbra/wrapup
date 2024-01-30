package org.ammbra.advent.surprise.decor;

public enum Celebration {
	BIRTHDAY("Happy Birthday!", Color.RED),
	CURRENT_YEAR("Hope you a had a great 2023!", Weight.REGULAR),
	NEW_YEAR("Happy New Year!", Weight.BOLD);

	private final String text;
	private final Font font;

	Celebration(String text, Font font) {
		this.text = text;
		this.font = font;
	}

	public String getText() {
		return text;
	}

	public Font getFont() {
		return font;
	}
}
