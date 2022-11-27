package com.xwarner.eml.parser;

import java.util.ArrayList;

import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.functions.InvocationNode;
import com.xwarner.eml.nodes.values.BooleanNode;
import com.xwarner.eml.nodes.values.MatrixNode;
import com.xwarner.eml.nodes.values.MatrixRowNode;
import com.xwarner.eml.nodes.values.NumberNode;
import com.xwarner.eml.nodes.values.OperatorNode;
import com.xwarner.eml.nodes.values.StringNode;
import com.xwarner.eml.nodes.values.VectorNode;
import com.xwarner.eml.nodes.variables.DeclarationNode;
import com.xwarner.eml.parser.tokens.Token;
import com.xwarner.eml.parser.tokens.TokenDataSet;

/**
 * Defines the structure of a token stream and provides a number of utility
 * methods used by all four token streams
 * 
 * @author Max Warner
 *
 */
public class TokenStream {

	protected Token current; // the current token
	protected Token last; // the last token, probably should avoid using this if possible

	/**
	 * Returns the next token in the stream, without moving ahead
	 * 
	 * @return the next token in the stream
	 */
	public Token peek() {
		if (current == null)
			current = readNext();
		return current;
	}

	/**
	 * Returns the next token in the stream, and advances the stream
	 * 
	 * @return the next token in the stream
	 */
	public Token next() {
		Token token = current;
		if (current == null)
			token = readNext();
		else
			current = null;
		last = token;
		return token;
	}

	/**
	 * 
	 * @return has the stream finished
	 */
	public boolean done() {
		return peek() == null;
	}

	/**
	 * Read the next token, implemented by each TokenStream
	 * 
	 * @return the next token
	 */
	protected Token readNext() {
		return null;
	}

	/**
	 * Parses an expression
	 * 
	 * @param stream
	 * @param node
	 * @return TokenDataSet of the expression
	 */
	protected TokenDataSet parseExpression(TokenStream stream) {
		Node node = new ExpressionNode();
		String src = "";
		// the number of unclosed brackets that remain
		int count = 0, sCount = 0;
		while (true) {
			if (stream.done())
				break;
			Token t = stream.peek();

			// things that end the expression

			// any new line
			if (t.type == Token.NEWLINE)
				return new TokenDataSet(node, src);

			// any punctuation that signify the end of an expression
			if ("{},;|".indexOf(t.src) > -1)
				return new TokenDataSet(node, src);

			// any closing brackets that don't close brackets within the expression
			if (t.value.equals(")") && count == 0)
				return new TokenDataSet(node, src);
			if (t.value.equals("]") && sCount == 0)
				return new TokenDataSet(node, src);

			// keywords that aren't booleans
			if (t.type == Token.KEYWORD && !(t.value.equals("true") || t.value.equals("false")))
				return new TokenDataSet(node, src);

			t = stream.next();
			Node n = null;
			if (t.type == Token.REFERENCE) {
				// parse any reference
				src += t.src;
				if (!stream.done()) {
					if (stream.peek().value.equals("(")) {
						// if a function call
						TokenDataSet set = parseInvocation(stream, t);
						n = set.node;
						src += set.src;
					} else {
						n = t.node;
					}
				} else {
					n = t.node;
				}
			} else if (t.type == Token.NUMBER) {
				// a number
				src += t.src;
				n = new NumberNode(Float.parseFloat(t.value));
			} else if (t.type == Token.OPERATOR || t.value.equals("(") || t.value.equals(")")) {
				// an operator or bracket
				src += t.src;
				if (t.value.equals("("))
					count++;
				else if (t.value.equals(")"))
					count--;

				if (stream.done())
					return new TokenDataSet(node, src);

				if (stream.peek().value.equals("=")) {
					n = new OperatorNode(t.value + "=");
					stream.next();
				} else {
					n = new OperatorNode(t.value);
				}

			} else if (t.type == Token.STRING) {
				src += t.src;
				n = new StringNode(t.value);
			} else if (t.type == Token.ASSIGNMENT) {
				src += t.src;
				if (stream.peek().value.equals("=")) {
					n = new OperatorNode("==");
					stream.next();
				} else
					n = new OperatorNode("=");
			} else if (t.type == Token.KEYWORD) {
				src += t.src;
				if (t.value.equals("true"))
					n = new BooleanNode(true);
				else if (t.value.equals("false"))
					n = new BooleanNode(false);
			} else if (t.type == Token.VECTOR) {
				src += t.src;
				n = t.node;
			}

			if (t.value.equals("[")) {
				src += t.src;
				sCount++;
			} else if (t.value.equals("]")) {
				src += t.src;
				sCount--;
			}

			node.addChild(n);

		}
		return new TokenDataSet(node, src);
	}

	/**
	 * Parses an invocation
	 * 
	 * @param stream
	 * @param ref
	 * @return TokenDataSet of the invocation
	 */
	protected TokenDataSet parseInvocation(TokenStream stream, Token ref) {
		Node n = new InvocationNode();
		String src = "";
		src += ref.src;
		n.addChild(ref.node);
		stream.next(); // consume the (
		src += "(";
		if (stream.done())
			throw new Error("inccorect function invocation");

		if (stream.peek().value.equals(")")) {
			src += ")";
			stream.next(); // consume the )
			return new TokenDataSet(n, src);
		} else {
			while (true) {
				TokenDataSet tds = parseExpression(stream);
				src += tds.src;
				n.addChild(tds.node);
				if (stream.done())
					return new TokenDataSet(n, src);
				Token t = stream.next();
				if (t.value.equals(")")) {
					src += ")";
					return new TokenDataSet(n, src);
				}
				// TODO what causes this to be called? seems to happen when loading libraries
				// for example
				System.out.println("stuck");
			}
		}
	}

	/**
	 * Parses a vector or matrix TODO parse arrays here too
	 * 
	 * @param stream
	 * @return TokenDataSet of the vector or matrix
	 */
	public TokenDataSet parseVectorOrMatrix(TokenStream stream) {
		Node node = new VectorNode();
		String src = "";
		Node matrixNode = null;

		if (!stream.next().value.equals("["))
			throw new Error("missing [");
		src += "[";

		while (true) {
			if (stream.done())
				break;

			TokenDataSet tds = parseExpression(stream);
			src += tds.src;
			node.addChild(tds.node);

			if (stream.peek().type == Token.KEYWORD) {
				Token t = stream.next();
				src += t.value;
				node.addChild(new DeclarationNode(t.value));
			}

			if (stream.peek().value.equals(",")) {
				src += ",";
				stream.next();
			} else if (stream.peek().value.equals("]")) {
				src += "]";
				stream.next();
				break;
			} else if (stream.peek().value.equals("|") || stream.peek().value.equals(";")) {
				src += "|";
				// is a matrix / new line
				if (matrixNode == null)
					matrixNode = new MatrixNode();
				MatrixRowNode mrn = new MatrixRowNode();
				mrn.setChildren(node.getChildren());
				node.setChildren(new ArrayList<Node>());
				matrixNode.addChild(mrn);
				stream.next();
			}

		}
		if (matrixNode != null) {
			MatrixRowNode mrn = new MatrixRowNode();
			mrn.setChildren(node.getChildren());
			node.setChildren(new ArrayList<Node>());
			matrixNode.addChild(mrn);
			return new TokenDataSet(matrixNode, src);
		}
		return new TokenDataSet(node, src);
	}

}
