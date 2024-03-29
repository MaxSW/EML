package com.xwarner.eml.nodes.logic;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.flags.BreakFlag;
import com.xwarner.eml.interpreter.flags.ContinueFlag;
import com.xwarner.eml.interpreter.flags.ReturnFlag;
import com.xwarner.eml.nodes.Node;

public class WhileNode extends Node {

	public String toString() {
		return "while";
	}

	public Object invoke() {
		ArrayList<Node> children = getChildren();
		while ((boolean) children.get(0).invoke()) {
			Object o = children.get(1).invoke();
			if (o instanceof ReturnFlag)
				return o;
			if (o instanceof BreakFlag)
				break;
			if (o instanceof ContinueFlag)
				continue;

		}

		return null;
	}
}
