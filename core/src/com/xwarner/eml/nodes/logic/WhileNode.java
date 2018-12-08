package com.xwarner.eml.nodes.logic;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.flags.BreakFlag;
import com.xwarner.eml.interpreter.flags.ContinueFlag;
import com.xwarner.eml.interpreter.flags.ReturnFlag;
import com.xwarner.eml.nodes.Node;

public class WhileNode extends Node {

	public String toString() {
		return "while";
	}

	public String toSaveString() {
		return "9";
	}

	public Object invoke2(Bundle bundle) {
		ArrayList<Node> children = getChildren();
		while ((boolean) children.get(0).invoke2(bundle)) {
			Object o = children.get(1).invoke2(bundle);
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
