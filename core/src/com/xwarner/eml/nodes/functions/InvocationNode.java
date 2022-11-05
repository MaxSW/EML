package com.xwarner.eml.nodes.functions;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;

public class InvocationNode extends Node {

	public String toString() {
		return "invocation";
	}

	public Object invoke(Bundle bundle) {
		ReferenceNode ref = (ReferenceNode) getChildren().get(0);
		if (getChildren().size() == 1) {
			return bundle.context.runFunction(ref, null, bundle, 0);
		} else {
			ArrayList<Object> objects = new ArrayList<Object>();
			for (Node n : getChildren()) {
				if (n instanceof ExpressionNode) {
					ExpressionNode arg = (ExpressionNode) n;
					objects.add(arg.invoke(bundle));
				}
			}
			return bundle.context.runFunction(ref, objects, bundle, 0);
		}
	}

}
