package com.xwarner.eml.nodes;

import com.xwarner.eml.interpreter.bundle.Bundle;
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

	public Object invoke(Bundle bundle) {
		Variable var = bundle.context.getVariable(this, 0, bundle);

		if (flag)
			return var;
		else {
			if (var instanceof NumericVariable) {
				return ((NumericVariable) var).getValue(bundle);
			} else if (var instanceof BooleanVariable) {
				return ((BooleanVariable) var).getValue(bundle);
			} else if (var instanceof StringVariable) {
				return ((StringVariable) var).getValue(bundle);
			} else {
				return var;
			}
		}
	}

	public String getName(Bundle bundle) {
		Node node = getChildren().get(getChildren().size() - 1);
		if (node instanceof VariableReferenceNode)
			return ((VariableReferenceNode) node).name;

		return "unknown";
	}

}
