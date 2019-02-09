package com.xwarner.eml.util;

import com.xwarner.eml.parser.tokens.Token;

public class ErrorHandler {

	public static void error(String message) {
		System.err.println(message);
		System.exit(0);
	}

	public static void error(String message, Token token) {
		System.err.println("line " + token.line + ": " + message);
		System.exit(0);
	}

}
