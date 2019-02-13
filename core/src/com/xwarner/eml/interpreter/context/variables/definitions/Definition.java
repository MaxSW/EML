package com.xwarner.eml.interpreter.context.variables.definitions;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.context.variables.VariableListener;
import com.xwarner.eml.nodes.ExpressionNode;

public class Definition implements VariableListener {

	private ExpressionNode expression;
	private Object cache;
	private boolean change;

	public Definition(ExpressionNode expression, Bundle bundle) {
		this.expression = expression;
		setup(bundle);
	}

	public Object evaluate(Bundle bundle) {
		if (change || cache == null) {
			cache = expression.invoke2(bundle);
			change = false;
		}
		return cache;
	}

	public void setup(Bundle bundle) {
		ArrayList<Variable> vars = expression.getVariables(bundle);
		for (Variable var : vars)
			var.addListener(this);
	}

	public void onChange(Object obj) {
		change = true;
	}

	public ExpressionNode getExpression() {
		return expression;
	}

	public void setExpression(ExpressionNode expression, Bundle bundle) {
		this.expression = expression;
		setup(bundle);
	}

	public boolean hasChanged() {
		return change;
	}
}
