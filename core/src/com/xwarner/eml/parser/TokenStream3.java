package com.xwarner.eml.parser;

import com.xwarner.eml.parser.tokens.Token;
import com.xwarner.eml.util.ErrorHandler;

/**
 * Converts the stream of tokens into a new stream of tokens with matrices and
 * vectors having been parsed
 * 
 * TODO parse arrays here too
 * 
 * @author Max Warner
 *
 */
public class TokenStream3 extends TokenStream {

	private TokenStream2 stream;
	public ErrorHandler error;

	public TokenStream3(TokenStream2 stream) {
		this.stream = stream;
		this.error = stream.error;
	}

	protected Token readNext() {
		Token token = stream.peek();
		if (token == null)
			return null;

		// TODO

		if (token.value.equals("[")) {
			Token t = new Token(Token.VECTOR, "", token.line, "");
			parseMatrix(stream).populate(t);
			return t;
		}

		return stream.next();
	}

}
