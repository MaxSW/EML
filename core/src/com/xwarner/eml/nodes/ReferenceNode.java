package com.xwarner.eml.nodes;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.variables.BooleanVariable;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.context.variables.StringVariable;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;

public class ReferenceNode extends Node {

	public boolean flag = false;

	public String toString() {
		if (flag)
			return "reference (flag)";
		else
			return "reference";
	}

	public Object invoke() {
		Variable var = Core.context.getVariable(this, 0);

		if (flag)
			return var;
		else {
			if (var instanceof NumericVariable) {
				return ((NumericVariable) var).getValue();
			} else if (var instanceof BooleanVariable) {
				return ((BooleanVariable) var).getValue();
			} else if (var instanceof StringVariable) {
				return ((StringVariable) var).getValue();
			} else {
				return var;
			}
		}
	}

	public String getName() {
		Node node = getChildren().get(getChildren().size() - 1);
		if (node instanceof VariableReferenceNode)
			return ((VariableReferenceNode) node).name;

		return "unknown";
	}

}
