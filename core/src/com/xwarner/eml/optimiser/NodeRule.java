package com.xwarner.eml.optimiser;

import java.util.ArrayList;

import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.tokens.Token;

/**
 * Simple regex for nodes. Similar to Token rule
 * 
 * @author Max Warner
 *
 */

public class NodeRule {

	private ArrayList<NodeRuleData> rules;

	public NodeRule() {
		rules = new ArrayList<NodeRuleData>();
	}

	public boolean matches(Node node) {
		return false;
	}

	public Node runIfMatches(Node node, NodeRuleResponse response) {
		if (matches(node))
			return response.run(node);
		return null;
	}

	private class NodeRuleData {

		public boolean matches(Token token) {
			return false;
		}

	}

	public interface NodeRuleResponse {
		public Node run(Node node);
	}
}
