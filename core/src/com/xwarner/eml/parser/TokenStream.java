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

	protected String parseExpression(TokenStream stream, Node node) {
		String src = "";
		int count = 0, sCount = 0;
		while (true) {
			if (stream.done())
				break;
			Token t = stream.peek();

			// System.out.println(t.value);

			if (t.type == Token.NEWLINE)
				return src;
			if (t.value.equals("}"))
				return src;
			if (t.value.equals("{"))
				return src;
			if (t.value.equals(","))
				return src;
			if (t.value.equals(")") && count == 0)
				return src;
			if (t.value.equals(";"))
				return src;
			if (t.value.equals("]") && sCount == 0)
				return src;
			if (t.value.equals("|"))
				return src;

			if (t.type == Token.KEYWORD && !(t.value.equals("true") || t.value.equals("false")))
				return src;

			t = stream.next();
			Node n = null;
			if (t.type == Token.REFERENCE) {
				src += t.src;
				if (!stream.done()) {
					if (stream.peek().value.equals("(")) {
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
				src += t.src;
				n = new NumberNode(Float.parseFloat(t.value));
			} else if (t.type == Token.OPERATOR || t.value.equals("(") || t.value.equals(")")) {
				src += t.src;
				if (t.value.equals("("))
					count++;
				else if (t.value.equals(")"))
					count--;

				if (stream.done())
					return src;

				if (stream.peek().value.equals("=")) {
					n = new OperatorNode(t.value + "=");
					stream.next();
				} else
					n = new OperatorNode(t.value);

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
		return src;
	}

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
				ExpressionNode an = new ExpressionNode();
				src += parseExpression(stream, an);
				n.addChild(an);
				if (stream.done())
					return new TokenDataSet(n, src);
				Token t = stream.next();
				if (t.value.equals(")")) {
					src += ")";
					return new TokenDataSet(n, src);
				}
			}
		}
	}

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

			ExpressionNode exp = new ExpressionNode();
			src += parseExpression(stream, exp);
			node.addChild(exp);

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

	class TokenDataSet {
		public String src;
		public Node node;

		public TokenDataSet(Node node, String src) {
			this.node = node;
			this.src = src;
		}
	}

}
