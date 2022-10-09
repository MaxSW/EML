package com.xwarner.eml.parser;

import java.util.ArrayList;

import com.xwarner.eml.parser.tokens.Token;

/**
 * Preloads the full stream of tokens from TokenStream4, so it is easier to look
 * back and forwards for the Parser
 * 
 * @author Max Warner
 *
 */

public class TokenStream5 {

	private ArrayList<Token> tokens;

	private int pos;
	private int length;

	public TokenStream5(TokenStream4 stream) {
		tokens = new ArrayList<Token>();
		while (!stream.done()) {
			tokens.add(stream.next());
		}
		pos = 0;
		length = tokens.size();
	}

	/**
	 * Returns the next token in the stream, without moving ahead
	 * 
	 * @return the next token in the stream
	 */
	public Token peek() {
		return tokens.get(pos);
	}

	/**
	 * Returns the next token in the stream, and advances the stream
	 * 
	 * @return the next token in the stream
	 */
	public Token next() {
		pos++;
		if (pos <= length)
			return tokens.get(pos - 1);
		else
			return null;
	}

	/**
	 * 
	 * @return has the stream finished
	 */
	public boolean done() {
		return pos >= length - 1;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

}
