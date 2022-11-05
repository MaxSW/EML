package com.xwarner.eml.nodes.logic;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.flags.Flag;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;

public class IfNode extends Node {

	public String toString() {
		return "if";
	}

	public Object invoke(Bundle bundle) {
		ArrayList<Node> children = getChildren();

		int i = 0;
		int size = children.size();
		if (size % 2 == 1)
			size--;

		while (i < size) {
			Node node = children.get(i);
			if (node instanceof ExpressionNode) {
				if ((boolean) node.invoke(bundle)) {
					Object o = children.get(i + 1).invoke(bundle);
					if (o instanceof Flag)
						return o;
					return null;
				} else {
					i++;
				}
			}
			i++;
		}

		// run the else
		if (children.size() % 2 == 1) {
			Object o = children.get(children.size() - 1).invoke(bundle);
			if (o instanceof Flag)
				return o;

		}

		return null;
	}
}
