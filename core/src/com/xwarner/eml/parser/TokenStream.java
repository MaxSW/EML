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
import com.xwarner.eml.parser.tokens.Token;

public class TokenStream {

	protected Token current; // cache
	protected Token last;

	public Token peek() {
		if (current == null)
			current = readNext();
		return current;
	}

	public Token next() {
		Token token = current;
		if (current == null)
			token = readNext();
		else
			current = null;
		last = token;
		return token;
	}

	public boolean done() {
		return peek() == null;
	}

	protected Token readNext() {
		return null;
	}

	/* Common methods */
	protected void parseExpression(TokenStream stream, Node node) {
		int count = 0, sCount = 0;
		while (true) {
			if (stream.done())
				break;
			Token t = stream.peek();

			// System.out.println(t.value);

			if (t.type == Token.NEWLINE)
				return;
			if (t.value.equals("}"))
				return;
			if (t.value.equals("{"))
				return;
			if (t.value.equals(","))
				return;
			if (t.value.equals(")") && count == 0)
				return;
			if (t.value.equals(";"))
				return;
			if (t.value.equals("]") && sCount == 0)
				return;
			if (t.value.equals("|"))
				return;

			if (t.type == Token.KEYWORD && !(t.value.equals("true") || t.value.equals("false")))
				return;

			t = stream.next();
			Node n = null;
			if (t.type == Token.REFERENCE) {
				if (!stream.done()) {
					if (stream.peek().value.equals("("))
						n = parseInvocation(stream, t);
					else
						n = t.node;
				} else {
					n = t.node;
				}
			} else if (t.type == Token.NUMBER) {
				n = new NumberNode(Float.parseFloat(t.value));
			} else if (t.type == Token.OPERATOR || t.value.equals("(") || t.value.equals(")")) {
				if (t.value.equals("("))
					count++;
				else if (t.value.equals(")"))
					count--;

				if (stream.done())
					return;

				if (stream.peek().value.equals("=")) {
					n = new OperatorNode(t.value + "=");
					stream.next();
				} else
					n = new OperatorNode(t.value);

			} else if (t.type == Token.STRING) {
				n = new StringNode(t.value);
			} else if (t.type == Token.ASSIGNMENT) {
				if (stream.peek().value.equals("=")) {
					n = new OperatorNode("==");
					stream.next();
				} else
					n = new OperatorNode("=");
			} else if (t.type == Token.KEYWORD) {
				if (t.value.equals("true"))
					n = new BooleanNode(true);
				else if (t.value.equals("false"))
					n = new BooleanNode(false);
			} else if (t.type == Token.VECTOR) {
				n = t.node;
			}

			if (t.value.equals("["))
				sCount++;
			else if (t.value.equals("]"))
				sCount--;

			node.addChild(n);

		}
	}

	protected Node parseInvocation(TokenStream stream, Token ref) {
		Node n = new InvocationNode();
		n.addChild(ref.node);
		stream.next(); // consume the (
		if (stream.done())
			throw new Error("inccorect function invocation");

		if (stream.peek().value.equals(")")) {
			stream.next(); // consume the )
			return n;
		} else {
			while (true) {
				ExpressionNode an = new ExpressionNode();
				parseExpression(stream, an);
				n.addChild(an);
				if (stream.done())
					return n;
				Token t = stream.next();
				if (t.value.equals(")"))
					return n;
			}
		}
	}

	public Node parseVectorOrMatrix(TokenStream stream) {
		Node node = new VectorNode();
		Node matrixNode = null;

		if (!stream.next().value.equals("["))
			throw new Error("missing [");

		while (true) {
			if (stream.done())
				break;

			ExpressionNode exp = new ExpressionNode();
			parseExpression(stream, exp);
			node.addChild(exp);

			if (stream.peek().value.equals(",")) {
				stream.next();
			} else if (stream.peek().value.equals("]")) {
				stream.next();
				break;
			} else if (stream.peek().value.equals("|") || stream.peek().value.equals(";")) {
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
			return matrixNode;
		}
		return node;
	}

}
