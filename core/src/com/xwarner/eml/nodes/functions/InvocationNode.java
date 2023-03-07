package com.xwarner.eml.nodes.functions;

import java.util.ArrayList;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;

public class InvocationNode extends Node {

	public String toString() {
		return "invocation";
	}

	public Object invoke() {
		ReferenceNode ref = (ReferenceNode) getChildren().get(0);
		if (getChildren().size() == 1) {
			return Core.context.runFunction(ref, null, 0);
		} else {
			ArrayList<Object> objects = new ArrayList<Object>();
			for (Node n : getChildren()) {
				if (n instanceof ExpressionNode) {
					ExpressionNode arg = (ExpressionNode) n;
					objects.add(arg.invoke());
				}
			}
			return Core.context.runFunction(ref, objects, 0);
		}
	}

}
