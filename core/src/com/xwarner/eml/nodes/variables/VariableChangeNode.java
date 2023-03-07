package com.xwarner.eml.nodes.variables;

import java.math.BigDecimal;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.context.variables.StringVariable;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.values.OperatorNode;

public class VariableChangeNode extends Node {

	public String toString() {
		return "variable change";
	}

	public Object invoke() {
		ReferenceNode ref = (ReferenceNode) getChildren().get(0);
		ExpressionNode exp = (ExpressionNode) getChildren().get(1);

		Variable variable = Core.context.getVariable(ref, 0);

		if (variable instanceof NumericVariable) {
			NumericVariable var = (NumericVariable) variable;

			if (exp.getChildren().size() == 1) {
				OperatorNode n = (OperatorNode) exp.getChildren().get(0);
				if (n.operator.equals("++"))
					var.setValue(((BigDecimal) var.getValue()).add(BigDecimal.ONE));
				else if (n.operator.equals("--"))
					var.setValue(((BigDecimal) var.getValue()).subtract(BigDecimal.ONE));
			} else {
				ExpressionNode n = new ExpressionNode();
				for (int i = 1; i < exp.getChildren().size(); i++)
					n.addChild(exp.getChildren().get(i));

				BigDecimal val = (BigDecimal) n.invoke();
				OperatorNode o = (OperatorNode) exp.getChildren().get(0);
				Operator op = OperatorNode.get(o.operator.substring(0, 1));
				var.setValue(op.evaluateNumeric((BigDecimal) var.getValue(), val));
			}

		} else if (variable instanceof StringVariable) {
			StringVariable var = (StringVariable) variable;

			ExpressionNode n = new ExpressionNode();
			for (int i = 1; i < exp.getChildren().size(); i++)
				n.addChild(exp.getChildren().get(i));

			String val = (String) n.invoke();
			OperatorNode o = (OperatorNode) exp.getChildren().get(0);
			Operator op = OperatorNode.get(o.operator.substring(0, 1));
			var.setValue(op.evaluateString((String) var.getValue(), val));
		}

		return null;
	}
}
