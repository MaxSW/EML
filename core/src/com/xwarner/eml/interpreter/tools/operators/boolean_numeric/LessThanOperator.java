package com.xwarner.eml.interpreter.tools.operators.boolean_numeric;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.tools.operators.Operator;

public class LessThanOperator extends Operator {

	public LessThanOperator() {
		super("<", 1, true, TYPE_BOOLEAN);
	}

	public boolean evaluateNumericBoolean(BigDecimal a, BigDecimal b) {
		return a.compareTo(b) < 0;
	}

}
