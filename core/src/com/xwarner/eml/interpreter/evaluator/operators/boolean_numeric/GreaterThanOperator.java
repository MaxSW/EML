package com.xwarner.eml.interpreter.evaluator.operators.boolean_numeric;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.evaluator.operators.Operator;

public class GreaterThanOperator extends Operator {

	public GreaterThanOperator() {
		super(">", 1, true, TYPE_BOOLEAN);
	}

	public boolean evaluateNumericBoolean(BigDecimal a, BigDecimal b) {
		return a.compareTo(b) > 0;
	}

}
