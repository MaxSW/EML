package com.xwarner.eml.nodes.functions;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
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

	public Object invoke1(Bundle bundle) {
		Function func = new Function();

		ArrayList<Node> children = getChildren();
		for (Node node : children) {
			if (node instanceof BodyNode)
				func.setBody((BodyNode) node);
			else if (node instanceof FunctionArgumentNode)
				func.addArg((FunctionArgumentNode) node);
		}

		bundle.context.setFunction(name, func);

		return null;
	}

	public String toSaveString() {
		return "5 " + name;
	}
	
	public void fromSaveString(String[] split, String str) {
		name = split[2];
	}

}
