package com.xwarner.eml.nodes.functions;

import com.xwarner.eml.interpreter.flags.Flag;
import com.xwarner.eml.nodes.Node;

public class BodyNode extends Node {

	public String toString() {
		return "body";
	}

	public Object pre_invoke() {
		for (Node node : getChildren())
			node.pre_invoke();
		return null;
	}

	public Object invoke() {
		for (Node node : getChildren()) {
			Object o = node.invoke();
			if (o instanceof Flag)
				return o;
		}
		return null;
	}

}
