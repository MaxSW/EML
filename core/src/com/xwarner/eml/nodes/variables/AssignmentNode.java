package com.xwarner.eml.nodes.variables;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;

public class AssignmentNode extends Node {

	public String toString() {
		return "assignment";
	}

	public Object invoke2(Bundle bundle) {
		ReferenceNode ref = (ReferenceNode) this.getChildren().get(0);
		ExpressionNode exp = (ExpressionNode) this.getChildren().get(1);
		Variable var = bundle.context.getVariable(ref, 0, bundle);

		if (ref.flag) {
			var.setEquation(true);
			var.setDefinition(new Definition(exp, bundle));
		} else {
			var.setValue(exp.invoke2(bundle));
		}
		return null;
	}

	public String toSaveString() {
		return "18";
	}
}
