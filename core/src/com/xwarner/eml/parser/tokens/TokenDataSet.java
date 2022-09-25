package com.xwarner.eml.parser.tokens;

import com.xwarner.eml.nodes.Node;

/**
 * Provides some but not all of the data used for creating a token
 * 
 * @author Max Warner
 *
 */
public class TokenDataSet {
	public String src;
	public Node node;

	public TokenDataSet(Node node, String src) {
		this.node = node;
		this.src = src;
	}

	public void populate(Token t) {
		t.node = this.node;
		t.src = this.src;
	}
}
