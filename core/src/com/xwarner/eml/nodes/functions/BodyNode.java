package com.xwarner.eml.nodes.functions;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.flags.Flag;
import com.xwarner.eml.nodes.Node;

public class BodyNode extends Node {

	public String toString() {
		return "body";
	}

	public Object invoke1(Bundle bundle) {
		for (Node node : getChildren())
			node.invoke1(bundle);
		return null;
	}

	public Object invoke2(Bundle bundle) {
		for (Node node : getChildren()) {
			Object o = node.invoke2(bundle);
			if (o instanceof Flag)
				return o;
		}
		return null;
	}

}
