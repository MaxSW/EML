package com.xwarner.eml.interpreter.context.variables.definitions;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.context.variables.VariableListener;
import com.xwarner.eml.nodes.ExpressionNode;

public class Definition implements VariableListener {

	private ExpressionNode expression;
	private Object cache;
	private boolean change;

	public Definition(ExpressionNode expression) {
		this.expression = expression;
		setup();
	}

	public Object evaluate() {
		if (change || cache == null) {
			cache = expression.invoke();
			change = false;
		}
		return cache;
	}

	public void setup() {
		ArrayList<Variable> vars = expression.getVariables();
		for (Variable var : vars)
			var.addListener(this);
	}

	public void onChange(Object obj) {
		change = true;
	}

	public ExpressionNode getExpression() {
		return expression;
	}

	public void setExpression(ExpressionNode expression) {
		this.expression = expression;
		setup();
	}

	public boolean hasChanged() {
		return change;
	}
}
