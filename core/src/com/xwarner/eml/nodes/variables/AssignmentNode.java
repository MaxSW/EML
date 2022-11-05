package com.xwarner.eml.nodes.variables;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.objects.ObjectCreationNode;

public class AssignmentNode extends Node {

	public String toString() {
		return "assignment";
	}

	public Object invoke(Bundle bundle) {
		ReferenceNode ref = (ReferenceNode) this.getChildren().get(0);
		Node n = this.getChildren().get(1);

		if (n instanceof ExpressionNode) {
			Variable var = bundle.context.getVariable(ref, 0, bundle);
			ExpressionNode exp = (ExpressionNode) n;

			if (ref.flag) {
				var.setEquation(true);
				var.setDefinition(new Definition(exp, bundle));
			} else {
				var.setValue(exp.invoke(bundle));
			}
		} else if (n instanceof ObjectCreationNode) {
			bundle.context.createVariable(ref, ((ObjectCreationNode) n).invoke(bundle), 0, bundle);
		}
		return null;
	}

}
