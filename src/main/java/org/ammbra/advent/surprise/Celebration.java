package org.ammbra.advent.surprise;

public enum Celebration {
	BIRTHDAY("Happy Birthday!", Style.ITALIC),
	CURRENT_YEAR("Hope you a had a great 2023!", Style.REGULAR),
	NEW_YEAR("Happy New Year!", Style.BOLD);

	private final String text;
	private final Style style;

//
//	Celebration(String text) {
//		this.style = Style.REGULAR;
//		this.text = text;
//	}

	Celebration(String text, Style style) {
		this.text = text;
		this.style = style;
	}

	public String getText() {
		return text;
	}

	public Style getStyle() {
		return style;
	}
}
