package com.xwarner.eml.parser;

import java.util.ArrayList;

import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
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
import com.xwarner.eml.parser.tokens.TokenRule;
import com.xwarner.eml.util.ErrorHandler;

/**
 * Converts the final TokenStream into a Tree of Nodes. Most of the `grammar'
 * rules of EML are applied here
 * 
 * TODO much better error handling
 * 
 * TODO some sort of custom token regex to make rules more clearly defined
 * 
 * @author Max Warner
 *
 */
public class Parser extends TokenStream {

	private TokenStream5 stream; // the input stream

	private TokenRule declarationRule, objNewDeclarationRule, assignmentExpressionRule, variableChangeRule,
			assignmentInvocationRule;
	private TokenRule whileRule, ifRule, forRule;

	/**
	 * Creates the parser with the given input TokenStream
	 * 
	 * @param stream
	 */
	public Parser(TokenStream5 stream) {
		this.stream = stream;
	}

	/**
	 * Parse the stream and produce a Tree
	 * 
	 * @return
	 */
	public Tree parse() {
		defineRules();
		Tree tree = new Tree();
		while (!stream.done()) {
			Node n = nextNode();
			if (n != null)
				tree.addChild(n);
		}
		return tree;
	}

	public void defineRules() {
		declarationRule = new TokenRule().either(Token.KEYWORD, "const", "var", "bool", "str", "vec", "mat", "arr")
				.require(Token.REFERENCE).require(Token.ASSIGNMENT).any();
		objNewDeclarationRule = new TokenRule().require(Token.KEYWORD, "obj").require(Token.REFERENCE)
				.require(Token.ASSIGNMENT).require(Token.KEYWORD, "new").require(Token.INVOCATION);
		assignmentExpressionRule = new TokenRule().require(Token.REFERENCE).require(Token.ASSIGNMENT)
				.either(new int[] { Token.EXPRESSION, Token.VECTOR }, new String[] { "", "" });
		variableChangeRule = new TokenRule().require(Token.REFERENCE).require(Token.EXPRESSION);
		assignmentInvocationRule = new TokenRule().require(Token.REFERENCE).require(Token.ASSIGNMENT)
				.require(Token.KEYWORD, "new").require(Token.INVOCATION);

		whileRule = new TokenRule().require(Token.KEYWORD, "while").require(Token.EXPRESSION);
		ifRule = new TokenRule().require(Token.KEYWORD, "if").require(Token.EXPRESSION);
		// TODO specify expressions should only be (
		forRule = new TokenRule().require(Token.KEYWORD, "for").require(Token.EXPRESSION).require(declarationRule)
				.require(Token.PUNCTUATION, ";").require(Token.EXPRESSION).require(Token.PUNCTUATION, ";")
				.require(Token.EXPRESSION).require(Token.PUNCTUATION, ")");
	}

	/**
	 * Return the next Node
	 * 
	 * @return
	 */
	public Node nextNode() {

		/* Variable declaration or assignment */
		if (declarationRule.matches(stream))
			return declarationRule.extract((tokens) -> parseDeclaration(tokens));
		if (objNewDeclarationRule.matches(stream))
			return objNewDeclarationRule.extract((tokens) -> parseNewObjectDeclaration(tokens));
		if (assignmentExpressionRule.matches(stream))
			return assignmentExpressionRule.extract((tokens) -> parseAssignmentExpression(tokens));
		if (variableChangeRule.matches(stream))
			return variableChangeRule.extract((tokens) -> parseVariableChange(tokens));
		if (assignmentInvocationRule.matches(stream))
			return assignmentInvocationRule.extract((tokens) -> parseAssignmentInvocation(tokens));

		/* Logic */
		if (whileRule.matches(stream))
			return whileRule.extract((tokens) -> parseWhile(tokens));
		if (ifRule.matches(stream))
			return ifRule.extract((tokens) -> parseIf(tokens));
		if (forRule.matches(stream))
			return forRule.extract((tokens) -> parseFor(tokens));

		Token t = stream.next();

		if (t == null)
			return null;

		// skip new lines
		if (t.type == Token.NEWLINE)
			return nextNode();

		if (t.type == Token.KEYWORD) {
			if (t.value.equals("obj"))
				return parseObjectCreation();
			else if (t.value.equals("func"))
				return parseFunctionCreation();
			else if (t.value.equals("return"))
				return parseReturn();
			else if (t.value.equals("class"))
				return parseClassCreation();
			else if (t.value.equals("continue")) {
				stream.next();
				return new ContinueNode();
			} else if (t.value.equals("break")) {
				stream.next();
				return new BreakNode();
			}
		} else if (t.type == Token.INVOCATION) {
			return t.node;
		}

		ErrorHandler.error("unknown token \"" + t.value + "\"", t);
		return null;

		// TODO if nothing matches we can do really nice error handling by reporting
		// what worked the closest (in order) and what was unexpected
		// we can detect this by looking at whether the tree has grown over this
		// function call
	}

	// TODO rewrite earlier parsers to distinguish between vectors (no commas) and
	// arrays (commas)
	public Node parseDeclaration(ArrayList<Token> tokens) {
		Node node = new DeclarationNode(tokens.get(0).value);
		node.addChild(tokens.get(1).node);
		node.addChild(tokens.get(3).node);
		return node;
	}

	// TODO write tests once parsed object creation
	public Node parseNewObjectDeclaration(ArrayList<Token> tokens) {
		Node node = new DeclarationNode("obj");
		node.addChild(tokens.get(1).node);
		Node node2 = new ObjectCreationNode();
		node2.addChild(tokens.get(4).node);
		node.addChild(node2);
		return node;
	}

	public Node parseAssignmentExpression(ArrayList<Token> tokens) {
		Node node = new AssignmentNode();
		node.addChild(tokens.get(0).node);
		node.addChild(tokens.get(2).node);
		return node;
	}

	public Node parseVariableChange(ArrayList<Token> tokens) {
		Node node = new VariableChangeNode();
		node.addChild(tokens.get(0).node);
		node.addChild(tokens.get(1).node);
		return node;
	}

	// TODO write tests once parsed object creation
	public Node parseAssignmentInvocation(ArrayList<Token> tokens) {
		Node node = new AssignmentNode();
		Node node2 = new ObjectCreationNode();
		node2.addChild(tokens.get(3).node);
		node.addChild(node2);
		return node;
	}

	// TODO remove opening and closing brackets from expressions in while, if, for
	public Node parseWhile(ArrayList<Token> tokens) {
		WhileNode node = new WhileNode();
		node.addChild(tokens.get(1).node);
		node.addChild(parseBody());
		return node;
	}

	public Node parseIf(ArrayList<Token> tokens) {
		IfNode node = new IfNode();
		node.addChild(tokens.get(1).node);
		node.addChild(parseBody());
		while (true) {
			if (stream.done())
				break;
			if (!stream.peek().value.equals("else"))
				break;
			stream.next(); // consume the else
			if (stream.peek().value.equals("if")) {
				stream.next(); // consume the if
				node.addChild(stream.next().node);
				node.addChild(parseBody());
			} else {
				node.addChild(parseBody());
				return node; // can only have one else statement
			}
		}
		return node;
	}

	public Node parseFor(ArrayList<Token> tokens) {
		ForNode node = new ForNode();
		node.addChild(parseDeclaration(new ArrayList<Token>(tokens.subList(2, 6))));
		node.addChild(tokens.get(7).node);

		// convert the expression node into a standard variable change node
		// TODO is there better way of doing this?
		ExpressionNode exp = (ExpressionNode) tokens.get(9).node;
		VariableChangeNode adj = new VariableChangeNode();
		adj.addChild(exp.getChildren().get(0));
		ExpressionNode exp2 = new ExpressionNode();
		for (int i = 1; i < exp.getChildren().size(); i++) {
			exp2.addChild(exp.getChildren().get(i));
		}
		adj.addChild(exp2);
		node.addChild(adj);
		node.addChild(parseBody());
		return node;
	}

	/** OLD FUNCTIONS **/

	public Node parseObjectCreation() {
		Token token = stream.next();
		if (token.type != Token.REFERENCE)
			ErrorHandler.error("invalid variable name", token);
		if (stream.next().type != Token.ASSIGNMENT)
			ErrorHandler.error("missing = in variable assignment", token);

		Node node = new DeclarationNode("obj");
		node.addChild(token.node);

		// empty object
		if (stream.peek().value.equals("{")) {
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

	/**
	 * Parse a function creation
	 * 
	 * @return
	 */
	public Node parseFunctionCreation() {
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

	/**
	 * Parse a class creation
	 * 
	 * @return
	 */
	public Node parseClassCreation() {
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

	/**
	 * Parse a return statement
	 * 
	 * @return
	 */
	public Node parseReturn() {
		ReturnNode n = new ReturnNode();
		if (stream.peek().type == Token.EXPRESSION)
			n.addChild(stream.next().node);
		return n;
	}

	/**
	 * Parse the body of a function or other construction
	 * 
	 * @return
	 */
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
