package com.xwarner.eml.nodes.functions;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.flags.Flag;
import com.xwarner.eml.nodes.Node;

public class BodyNode extends Node {

	public String toString() {
		return "body";
	}

	public Object pre_invoke(Bundle bundle) {
		for (Node node : getChildren())
			node.pre_invoke(bundle);
		return null;
	}

	public Object invoke(Bundle bundle) {
		for (Node node : getChildren()) {
			Object o = node.invoke(bundle);
			if (o instanceof Flag)
				return o;
		}
		return null;
	}

}
