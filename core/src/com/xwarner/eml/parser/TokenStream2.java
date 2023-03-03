package com.xwarner.eml.parser;

import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.values.NumberNode;
import com.xwarner.eml.nodes.values.OperatorNode;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;
import com.xwarner.eml.parser.tokens.Token;
import com.xwarner.eml.util.ErrorHandler;

/**
 * Converts the stream of tokens into a new stream of tokens with variable
 * references having been parsed
 * 
 * @author Max Warner
 *
 */

public class TokenStream2 extends TokenStream {

	private TokenStream1 stream; // the input TokenStream
	public ErrorHandler error; // the error handler

	/**
	 * Creates a new TokenStream using the previous TokenStream as input
	 * 
	 * @param stream
	 */
	public TokenStream2(TokenStream1 stream) {
		this.stream = stream;
		this.error = stream.error;
	}

	/**
	 * Read the next Token
	 */
	protected Token readNext() {
		Token token = stream.next();
		if (token == null)
			return null;
		// a reference must be started by a name or a reference indicator
		if (token.type == Token.NAME || token.value.equals(":")) {
			return parseReference(token);
		}
		return token;
	}

	/**
	 * Parse a reference
	 * 
	 * @param token
	 * @return
	 */
	private Token parseReference(Token token) {
		Token t = new Token(Token.REFERENCE, "", token.line, "");
		ReferenceNode node = new ReferenceNode();
		if (token.type == Token.NAME) {
			node.addChild(new VariableReferenceNode(token.value));
			t.value = token.value;
			t.src += token.value;
		} else {
			Token t2 = stream.next();
			node.addChild(new VariableReferenceNode(t2.value));
			t.value = t2.value;
			t.src += t2.value;
			node.flag = true;
		}
		t.node = node;
		boolean nextChild = false;
		while (true) {
			if (stream.done())
				break;
			token = stream.peek();
			if (token.type == Token.PUNCTUATION) {
				t.src += token.src;
				if (token.value.equals(".") && !nextChild) {
					nextChild = true;
					stream.next();
					t.value += token.value;
				} else if (token.value.equals("[") && !nextChild) {
					stream.next();
					ExpressionNode exp = new ExpressionNode();
					while (true) {
						Token tt = stream.next();
						t.src += tt.src;
						if (tt.value.equals("]"))
							break;
						if (tt.type == Token.NUMBER)
							exp.addChild(new NumberNode(Float.parseFloat(tt.value)));
						else if (tt.type == Token.NAME)
							exp.addChild(parseReference(tt).node);
						else if (tt.type == Token.OPERATOR || tt.value.equals("(") || tt.value.equals(")"))
							exp.addChild(new OperatorNode(tt.value));
						else if (tt.type == Token.PUNCTUATION && tt.value.equals(",")) {
							t.node.addChild(exp);
							exp = new ExpressionNode();
						} else
							break;
					}
					t.node.addChild(exp);
					t.value += token.value;
				} else {
					break;
				}
			} else if (token.type == Token.NAME && nextChild) {
				t.node.addChild(new VariableReferenceNode(stream.next().value));
				nextChild = false;
				t.value += token.value;
			} else {
				break;
			}
		}
		return t;
	}

}
