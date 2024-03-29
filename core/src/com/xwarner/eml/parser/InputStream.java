package com.xwarner.eml.parser;

public class InputStream {

	private String source;
	private int pos = 0;
	// line and col for tracking error locations
	private int line = 1;

	public InputStream(String source) {
		this.source = source;
	}

	public char next() {
		if (pos >= source.length())
			return ' ';
		char c = source.charAt(pos);
		pos++;
		if (c == '\n' || c == '\r') {
			line++;
		}
		return c;
	}

	public char peek() {
		return source.charAt(pos);
	}

	public boolean done() {
		return pos >= source.length();
	}

	public int line() {
		return line;
	}

	public int pos() {
		return pos;
	}

}
