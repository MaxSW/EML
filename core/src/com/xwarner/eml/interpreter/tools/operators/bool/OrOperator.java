package com.xwarner.eml.interpreter.tools.operators.bool;

import com.xwarner.eml.interpreter.tools.operators.Operator;

public class OrOperator extends Operator {

	public OrOperator() {
		super("||", 1, true, TYPE_BOOLEAN);
	}

	public boolean evaluateBoolean(boolean a, boolean b) {
		return a || b;
	}

}
