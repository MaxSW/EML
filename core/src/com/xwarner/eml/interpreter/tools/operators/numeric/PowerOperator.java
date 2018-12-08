package com.xwarner.eml.interpreter.tools.operators.numeric;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.tools.operators.Operator;

public class PowerOperator extends Operator {

	public PowerOperator() {
		super("^", 4, false, TYPE_NUMERIC);
	}

	public BigDecimal evaluateNumeric(BigDecimal a, BigDecimal b) {
		return a.pow(b.intValue());
	}

}
