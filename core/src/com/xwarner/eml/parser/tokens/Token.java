package com.xwarner.eml.parser.tokens;

import com.xwarner.eml.nodes.Node;

public class Token {
	public static final int PUNCTUATION = 1, NUMBER = 2, KEYWORD = 3, NAME = 4, OPERATOR = 5, ASSIGNMENT = 6,
			STRING = 7, NEWLINE = 8;

	public static final int REFERENCE = 11, EXPRESSION = 12, INVOCATION = 13, VECTOR = 14;

	public int type;
	public String value, src;

	public Node node;
	public int line;

	public Token(int type, String value, int line, String src) {
		this.type = type;
		this.value = value;
		this.line = line;
		this.src = src;
	}

	public static String type(int type) {
		if (type == PUNCTUATION)
			return "Punctuation";
		if (type == NUMBER)
			return "Number";
		if (type == KEYWORD)
			return "Keyword";
		if (type == NAME)
			return "Name";
		if (type == OPERATOR)
			return "Operator";
		if (type == ASSIGNMENT)
			return "Assignment";
		if (type == STRING)
			return "String";
		if (type == NEWLINE)
			return "Newline";

		return "Unknown token";
	}

}
