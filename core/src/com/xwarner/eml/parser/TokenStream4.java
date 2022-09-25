package com.xwarner.eml.parser;

import com.xwarner.eml.parser.tokens.Token;
import com.xwarner.eml.parser.tokens.TokenDataSet;
import com.xwarner.eml.util.ErrorHandler;

/**
 * Converts the stream of tokens into a new stream of tokens with expressions
 * and invocations having been parsed
 * 
 * @author Max Warner
 *
 */

public class TokenStream4 extends TokenStream {

	private TokenStream3 stream;
	public ErrorHandler error;

	private boolean lastAssignment;
	private boolean lastSemiColon;
	private boolean lastReturn;

	public TokenStream4(TokenStream3 stream) {
		this.stream = stream;
		this.error = stream.error;
	}

	protected Token readNext() {
		Token token = stream.peek();
		if (token == null)
			return null;

		if (shouldParseExpression(token)) {
			Token t = new Token(Token.EXPRESSION, "", token.line, "");
			parseExpression(stream).populate(t);
			return t;
		} else if (token.type == Token.REFERENCE) {
			if (!stream.done()) {
				Token tt = stream.next();
				if (stream.peek().value.equals("(")) {
					Token t = new Token(Token.INVOCATION, "", token.line, "");
					TokenDataSet set = parseInvocation(stream, token);
					t.node = set.node;
					t.src = set.src;
					return t;
				} else {
					return tt;
				}
			}
		}

		lastAssignment = token.type == Token.ASSIGNMENT;
		lastReturn = token.value.equals("return");
		lastSemiColon = token.value.equals(";");
		return stream.next();
	}

	/**
	 * Decides whether we should parse an expression, based on the first token
	 * 
	 * @param token
	 * @return
	 */
	private boolean shouldParseExpression(Token token) {
		if (token.type == Token.NUMBER || token.type == Token.OPERATOR
				|| (token.type == Token.REFERENCE && (lastAssignment || lastReturn)) || token.value.equals("(")
				|| token.type == Token.VECTOR)
			return true;
		// get the for loop
		if (token.type == Token.REFERENCE && lastSemiColon)
			return true;
		if (token.value.equals("true") || token.value.equals("false"))
			return true;
		if (token.type == Token.STRING)
			return true;

		return false;
	}

}
