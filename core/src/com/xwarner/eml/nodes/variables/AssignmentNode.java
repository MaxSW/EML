package com.xwarner.eml.nodes.variables;

import com.xwarner.eml.core.Core;
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

	public Object invoke() {
		ReferenceNode ref = (ReferenceNode) this.getChildren().get(0);
		Node n = this.getChildren().get(1);

		if (n instanceof ExpressionNode) {
			Variable var = Core.context.getVariable(ref, 0);
			ExpressionNode exp = (ExpressionNode) n;

			if (ref.flag) {
				var.setEquation(true);
				var.setDefinition(new Definition(exp));
			} else {
				var.setValue(exp.invoke());
			}
		} else if (n instanceof ObjectCreationNode) {
			Core.context.createVariable(ref, ((ObjectCreationNode) n).invoke(), 0);
		}
		return null;
	}

}
