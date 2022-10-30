package com.xwarner.eml.optimiser;

import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.Tree;

public class TreeOptimiser {

	public TreeOptimiser() {
		createRules();
	}

	private void createRules() {

	}

	public Tree optimise(Tree input) {
		Tree output = new Tree();
		for (Node node : input.getChildren()) {
			output.addChild(optimiseNode(node));
		}
		return output;
	}

	private Node optimiseNode(Node node) {
		// check each rule
		return node;
	}

}
