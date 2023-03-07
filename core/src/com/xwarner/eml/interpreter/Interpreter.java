package com.xwarner.eml.interpreter;

import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.Tree;

public class Interpreter {

	public Tree tree;

	public Interpreter(Tree tree) {
		this.tree = tree;
	}

	public void run() {
		for (Node node : tree.getChildren())
			node.pre_invoke();
		for (Node node : tree.getChildren())
			node.invoke();
	}

}
