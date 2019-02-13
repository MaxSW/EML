package com.xwarner.eml.interpreter.evaluator.operators.bool;

import com.xwarner.eml.interpreter.evaluator.operators.Operator;

public class AndOperator extends Operator {

	public AndOperator() {
		super("&&", 1, true, TYPE_BOOLEAN);
	}

	public boolean evaluateBoolean(boolean a, boolean b) {
		return a && b;
	}

}
