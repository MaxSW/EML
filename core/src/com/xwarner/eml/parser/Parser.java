package com.xwarner.eml.parser;

import com.xwarner.eml.Dev;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.functions.BodyNode;
import com.xwarner.eml.nodes.functions.FunctionArgumentNode;
import com.xwarner.eml.nodes.functions.FunctionNode;
import com.xwarner.eml.nodes.logic.BreakNode;
import com.xwarner.eml.nodes.logic.ContinueNode;
import com.xwarner.eml.nodes.logic.ForNode;
import com.xwarner.eml.nodes.logic.IfNode;
import com.xwarner.eml.nodes.logic.ReturnNode;
import com.xwarner.eml.nodes.logic.WhileNode;
import com.xwarner.eml.nodes.objects.ClassNode;
import com.xwarner.eml.nodes.objects.ObjectCreationNode;
import com.xwarner.eml.nodes.variables.AssignmentNode;
import com.xwarner.eml.nodes.variables.DeclarationNode;
import com.xwarner.eml.nodes.variables.VariableChangeNode;
import com.xwarner.eml.parser.tokens.Token;
import com.xwarner.eml.tools.ErrorHandler;

public class Parser extends TokenStream {

	private TokenStream4 stream;

	public Parser(TokenStream4 stream) {
		this.stream = stream;
	}

	public Tree parse() {
		Tree tree = new Tree();
		while (!stream.done()) {
			Node node = nextNode();
			if (node != null)
				tree.addChild(node);
		}
		return tree;
	}

	public Node nextNode() {
		Token t = stream.next();

		if (t == null)
			return null;

		if (t.type == Token.NEWLINE)
			return nextNode();

		if (t.type == Token.KEYWORD) {
			if (t.value.equals("const") || t.value.equals("var") || t.value.equals("bool")
					|| t.value.equals("str"))
				return parseDeclaration(t.value);
			else if (t.value.equals("arr"))
				return parseArrayCreation();
			else if (t.value.equals("obj"))
				return parseObjectCreation();
			else if (t.value.equals("vec"))
				return parseVector();
			else if (t.value.equals("mat"))
				return parseMatrix();
			else if (t.value.equals("func"))
				return parseFunction();
			else if (t.value.equals("if"))
				return parseIf();
			else if (t.value.equals("while"))
				return parseWhile();
			else if (t.value.equals("for"))
				return parseFor();
			else if (t.value.equals("return"))
				return parseReturn();
			else if (t.value.equals("class"))
				return parseClass();
			else if (t.value.equals("continue")) {
				stream.next();
				return new ContinueNode();
			} else if (t.value.equals("break")) {
				stream.next();
				return new BreakNode();
			}
		} else if (t.type == Token.REFERENCE) {
			Token t2 = stream.peek();
			if (t2.type == Token.ASSIGNMENT) {
				return parseAssignment(t);
			} else if (t2.type == Token.EXPRESSION) {
				return parseVariableChange(t);
			}
		} else if (t.type == Token.INVOCATION) {
			return t.node;
		}

		ErrorHandler.error("unknown token \"" + t.value + "\"", t);
		return null;
	}

	public Node parseDeclaration(String type) {
		Token token = stream.next();
		if (token.type != Token.REFERENCE)
			ErrorHandler.error("invalid variable name", token);
		Token token2 = stream.next();
		if (token2.type != Token.ASSIGNMENT)
			ErrorHandler.error("missing = in variable assignment", token2);
		Node node = new DeclarationNode(type);
		node.addChild(token.node);
		Token token3 = stream.next();
		node.addChild(token3.node);
		return node;
	}

	public Node parseObjectCreation() {
		Token token = stream.next();
		if (token.type != Token.REFERENCE)
			ErrorHandler.error("invalid variable name", token);
		if (stream.next().type != Token.ASSIGNMENT)
			ErrorHandler.error("missing = in variable assignment", token);

		Node node = new DeclarationNode("obj");
		node.addChild(token.node);

		if (stream.peek().value.equals("new")) {
			// an instantiation
			stream.next(); // consume the new
			Token token2 = stream.next();
			if (token2.type != Token.INVOCATION)
				ErrorHandler.error("invalid class", token2);

			Node node2 = new ObjectCreationNode();
			node2.addChild(token2.node);
			node.addChild(node2);
		} else if (stream.peek().value.equals("{")) {
			stream.next();
			if (!stream.next().value.equals("}"))
				ErrorHandler.error("missing }", token);
		} else {
			// else a function call
			Token token2 = stream.next();
			if (token2.type != Token.EXPRESSION)
				ErrorHandler.error("invalid object declaration", token);
			node.addChild(token2.node.getChildren().get(0));
		}
		return node;
	}

	public Node parseArrayCreation() {
		if (stream.peek().type != Token.REFERENCE)
			ErrorHandler.error("invalid variable name", stream.peek());
		ReferenceNode ref = (ReferenceNode) stream.next().node;
		if (stream.next().type != Token.ASSIGNMENT)
			ErrorHandler.error("missing = ", stream.peek());
//		System.out.println(stream.next().type);
		if (stream.next().type != Token.EXPRESSION)
			ErrorHandler.error("missing []", stream.peek());
		Node n = new DeclarationNode("arr");
		n.addChild(ref);
		return n;
	}

	public Node parseVector() {
		Node n = new DeclarationNode("vec");
		ReferenceNode ref = (ReferenceNode) stream.next().node;
		n.addChild(ref);
		if (stream.next().type != Token.ASSIGNMENT)
			ErrorHandler.error("missing = ", stream.peek());

		Token t = stream.next();
		if (t.type != Token.EXPRESSION)
			ErrorHandler.error("missing vector", t);
		n.addChild(t.node);

		return n;
	}

	public Node parseMatrix() {
		Node n = new DeclarationNode("mat");
		ReferenceNode ref = (ReferenceNode) stream.next().node;
		n.addChild(ref);
		if (stream.next().type != Token.ASSIGNMENT)
			ErrorHandler.error("missing = ", stream.peek());

		Token t = stream.next();
		if (t.type != Token.EXPRESSION)
			ErrorHandler.error("missing matrix", t);
		n.addChild(t.node);

		return n;
	}

	public Node parseAssignment(Token ref) {
		stream.next(); // consume the =
		Node n = new AssignmentNode();
		n.addChild(ref.node);
		Token t = stream.next();
		if (t.type == Token.EXPRESSION) {
			n.addChild(t.node);
		} else if (t.value.equals("new")) {
			Token t2 = stream.next();
			if (t2.type != Token.INVOCATION)
				ErrorHandler.error("invalid object creation", t2);
			Node n2 = new ObjectCreationNode();
			n2.addChild(t2.node);
			n.addChild(n2);
		} else if (t.type == Token.VECTOR) {
			n.addChild(t.node);
		}
		return n;
	}

	public Node parseVariableChange(Token ref) {
		VariableChangeNode n = new VariableChangeNode();
		n.addChild(ref.node);
		n.addChild(stream.next().node);
		return n;
	}

	public Node parseFunction() {
		FunctionNode node = new FunctionNode(stream.next().value);
		if (stream.next().type != Token.ASSIGNMENT)
			ErrorHandler.error("missing = in function declaration", stream.peek());
		stream.next();
		Token t = stream.peek();
		if (!t.value.equals("{")) {
			while (true) {
				Token t1 = stream.next();
				if (t1.type != Token.KEYWORD)
					ErrorHandler.error("incorrect argument definition 1", t1);
				String type = t1.value;
				Token t2 = stream.next();
				if (t2.type != Token.REFERENCE)
					ErrorHandler.error("incorrect argument definition 2", t2);
				String var = t2.value;
				node.addChild(new FunctionArgumentNode(type, var));
				if (stream.next().value.equals(")"))
					break;
			}
		}

		node.addChild(parseBody());
		return node;
	}

	public Node parseClass() {
		ClassNode node = new ClassNode(stream.next().value);
		if (stream.next().type != Token.ASSIGNMENT)
			ErrorHandler.error("missing = in function declaration", stream.peek());
		stream.next(); // TODO? workaround because the opening ( is being interpreted as an expression
		// if (!stream.next().value.equals("("))
		// throw new Error("misisng ( in function declaration");
		Token t = stream.peek();
		if (!t.value.equals("{")) {
			while (true) {
				Token t1 = stream.next();
				if (t1.type != Token.KEYWORD)
					ErrorHandler.error("incorrect argument definition 1", t1);
				String type = t1.value;
				Token t2 = stream.next();
				if (t2.type != Token.REFERENCE)
					ErrorHandler.error("incorrect argument definition 2", t2);
				String var = t2.value;
				node.addChild(new FunctionArgumentNode(type, var));
				if (stream.next().value.equals(")"))
					break;
			}
		}

		node.addChild(parseBody());
		return node;
	}

	public Node parseIf() {
		IfNode n = new IfNode();
		n.addChild(stream.next().node);
		n.addChild(parseBody());
		while (true) {
			if (stream.done())
				break;
			if (!stream.peek().value.equals("else"))
				break;
			stream.next(); // consume the else
			if (stream.peek().value.equals("if")) {
				stream.next(); // consume the if
				n.addChild(stream.next().node);
				n.addChild(parseBody());
			} else {
				n.addChild(parseBody());
				return n; // can only have one else statement
			}
		}
		return n;
	}

	public Node parseWhile() {
		WhileNode n = new WhileNode();
		n.addChild(stream.next().node);
		n.addChild(parseBody());
		return n;
	}

	public Node parseFor() {
		ForNode n = new ForNode();
		stream.next();
		n.addChild(parseDeclaration(stream.next().value));
		stream.next();
		n.addChild(stream.next().node);
		stream.next();

		// convert the expression node into a standard variable change node
		ExpressionNode exp = (ExpressionNode) stream.next().node;
		VariableChangeNode adj = new VariableChangeNode();
		adj.addChild(exp.getChildren().get(0));
		ExpressionNode exp2 = new ExpressionNode();
		for (int i = 1; i < exp.getChildren().size(); i++) {
			exp2.addChild(exp.getChildren().get(i));
		}
		adj.addChild(exp2);
		n.addChild(adj);
		stream.next();

		n.addChild(parseBody());
		return n;
	}

	public Node parseReturn() {
		ReturnNode n = new ReturnNode();
		if (stream.peek().type == Token.EXPRESSION)
			n.addChild(stream.next().node);
		return n;
	}

	public BodyNode parseBody() {
		BodyNode body = new BodyNode();

		Token t = stream.next();
		if (!t.value.equals("{"))
			ErrorHandler.error("missing { in function definition", t);

		int count = 0;

		while (true) {
			if (stream.done())
				break;
			if (stream.peek().value.equals("}") && count == 0)
				break;
			if (stream.peek().type == Token.NEWLINE) {
				stream.next();
				continue;
			}

			if (stream.peek().value.equals("{"))
				count++;
			else if (stream.peek().value.equals("}"))
				count--;

			Node next = nextNode();
			if (next != null)
				body.addChild(next);
			else
				break;
		}
		stream.next(); // consume the }
		return body;
	}

}
