package com.xwarner.eml.nodes.variables;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.values.OperatorNode;

public class VariableChangeNode extends Node {

	public String toString() {
		return "variable change";
	}

	public Object invoke2(Bundle bundle) {
		ReferenceNode ref = (ReferenceNode) getChildren().get(0);
		ExpressionNode exp = (ExpressionNode) getChildren().get(1);

		NumericVariable var = (NumericVariable) bundle.context.getVariable(ref, 0, bundle);

		if (exp.getChildren().size() == 1) {
			OperatorNode n = (OperatorNode) exp.getChildren().get(0);
			if (n.operator.equals("++"))
				var.setValue(((BigDecimal) var.getValue(bundle)).add(BigDecimal.ONE));
			else if (n.operator.equals("--"))
				var.setValue(((BigDecimal) var.getValue(bundle)).subtract(BigDecimal.ONE));
		} else {
			ExpressionNode n = new ExpressionNode();
			for (int i = 1; i < exp.getChildren().size(); i++)
				n.addChild(exp.getChildren().get(i));

			BigDecimal val = (BigDecimal) n.invoke2(bundle);
			OperatorNode o = (OperatorNode) exp.getChildren().get(0);
			Operator op = OperatorNode.get(o.operator.substring(0, 1));
			var.setValue(op.evaluateNumeric((BigDecimal) var.getValue(bundle), val));
		}
		return null;
	}

	public String toSaveString() {
		return "20";
	}
}
