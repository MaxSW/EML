package com.xwarner.eml.nodes.functions;

import java.util.ArrayList;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.nodes.Node;

public class FunctionNode extends Node {

	public String name;

	public FunctionNode(String name) {
		super();
		this.name = name;
	}

	public FunctionNode() {
	}

	public String toString() {
		return "function - name: " + name;
	}

	public Object pre_invoke() {
		Function func = new Function();

		ArrayList<Node> children = getChildren();
		for (Node node : children) {
			if (node instanceof BodyNode)
				func.setBody((BodyNode) node);
			else if (node instanceof FunctionArgumentNode)
				func.addArg((FunctionArgumentNode) node);
		}

		Core.context.setFunction(name, func);

		return null;
	}

}
