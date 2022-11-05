package com.xwarner.eml.nodes.objects;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.objects.EClass;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.functions.BodyNode;
import com.xwarner.eml.nodes.functions.FunctionArgumentNode;

public class ClassNode extends Node {

	public String name;

	public ClassNode(String name) {
		super();
		this.name = name;
	}

	public ClassNode() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "class - name: " + name;
	}

	public Object pre_invoke(Bundle bundle) {
		EClass cls = new EClass();

		ArrayList<Node> children = getChildren();
		for (Node node : children) {
			if (node instanceof BodyNode)
				cls.setBody((BodyNode) node);
			else if (node instanceof FunctionArgumentNode)
				cls.addArg((FunctionArgumentNode) node);
		}

		bundle.context.setClass(name, cls);
		return null;
	}

}
