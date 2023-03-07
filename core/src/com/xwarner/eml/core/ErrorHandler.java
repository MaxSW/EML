package com.xwarner.eml.core;

import com.xwarner.eml.parser.tokens.Token;

public class ErrorHandler {

	public void error(String message) {
		System.err.println(message);
		System.exit(0);
	}

	public void error(String message, Token token) {
		System.err.println("line " + token.line + ": " + message);
		System.exit(0);
	}

}
