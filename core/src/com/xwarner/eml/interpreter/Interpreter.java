package com.xwarner.eml.interpreter;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.Tree;

public class Interpreter {

	public Tree tree;
	public Bundle bundle;

	public Interpreter(Tree tree) {
		this.tree = tree;
		this.bundle = new Bundle();
	}

	public void run() {
		bundle.init();
		for (Node node : tree.getChildren())
			node.pre_invoke(bundle);
		for (Node node : tree.getChildren())
			node.invoke(bundle);
	}

}
