package com.xwarner.eml.tests;

import com.xwarner.eml.parser.InputStream;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.TokenStream1;
import com.xwarner.eml.parser.TokenStream2;
import com.xwarner.eml.parser.TokenStream3;
import com.xwarner.eml.parser.TokenStream4;
import com.xwarner.eml.parser.Tree;

/**
 * 
 * @author max
 *
 */

public class ParsingTests {

	public static void main(String[] args) {
		// TODO have the tests themselves stored in JSON and just read and run them here
		runTest(1, "var x = 0",
				"{\"a\":\"Tree\",\"z\":{\"a\":\"DeclarationNode\",\"z\":[{\"a\":\"ReferenceNode\",\"flag\":false,\"z\":{\"a\":\"VariableReferenceNode\",\"name\":\"x\"}},{\"a\":\"ExpressionNode\",\"z\":{\"a\":\"NumberNode\",\"value\":0}}],\"type\":\"var\"}}");

	}

	private static void runTest(int id, String code, String compareJSON) {
		Tree genTree = new Parser(
				new TokenStream4(new TokenStream3(new TokenStream2(new TokenStream1(new InputStream(code)))))).parse();

		boolean pass = genTree.toJSON().toString().equalsIgnoreCase(compareJSON);

		if (pass) {
			System.out.println("Test " + id + " passed");
		} else {
			System.err.println("Test " + id + " failed");
		}
	}

}
