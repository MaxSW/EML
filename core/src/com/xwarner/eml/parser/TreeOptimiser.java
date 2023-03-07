package com.xwarner.eml.parser;

import java.util.ArrayList;

import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.functions.BodyNode;
import com.xwarner.eml.nodes.variables.DeclarationNode;

public class TreeOptimiser {

	private ArrayList<Class<?>> parentList;
	private ArrayList<Class<?>> directList;

	public TreeOptimiser() {
		parentList = new ArrayList<Class<?>>();
		parentList.add(BodyNode.class);
		parentList.add(DeclarationNode.class);

		directList = new ArrayList<Class<?>>();
		directList.add(ExpressionNode.class);
	}

	public Tree optimise(Tree tree) {
		for (Node node : tree.getChildren()) {
			optimiseNode(node);
		}

		// TODO there may be other things we want to do that are more complex than doing
		// it at the node level

		return tree;
	}

	private Node optimiseNode(Node node) {
		if (parentList.contains(node.getClass())) {
			// Nodes to look into
			for (Node subnode : node.getChildren()) {
				optimiseNode(subnode);
			}
		} else if (directList.contains(node.getClass())) {
			// Nodes to optimise
			node.optimise();
		}
		// Default is to do nothing
		return node;
	}

}
