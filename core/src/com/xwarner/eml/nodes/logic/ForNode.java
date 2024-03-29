package com.xwarner.eml.nodes.logic;

import com.xwarner.eml.interpreter.flags.BreakFlag;
import com.xwarner.eml.interpreter.flags.ContinueFlag;
import com.xwarner.eml.interpreter.flags.ReturnFlag;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.functions.BodyNode;
import com.xwarner.eml.nodes.variables.DeclarationNode;
import com.xwarner.eml.nodes.variables.VariableChangeNode;

public class ForNode extends Node {

	public String toString() {
		return "for";
	}

	public Object invoke() {
		DeclarationNode n1 = (DeclarationNode) getChildren().get(0);
		ExpressionNode n2 = (ExpressionNode) getChildren().get(1);
		VariableChangeNode n3 = (VariableChangeNode) getChildren().get(2);
		BodyNode n4 = (BodyNode) getChildren().get(3);

		for (n1.invoke(); (boolean) n2.invoke(); n3.invoke()) {
			Object o = n4.invoke();
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
