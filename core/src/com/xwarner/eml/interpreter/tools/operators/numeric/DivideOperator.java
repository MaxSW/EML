package com.xwarner.eml.interpreter.tools.operators.numeric;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.xwarner.eml.interpreter.tools.operators.Operator;

public class DivideOperator extends Operator {

	public DivideOperator() {
		super("/", 3, true, TYPE_NUMERIC);
	}

	public BigDecimal evaluateNumeric(BigDecimal a, BigDecimal b) {
		return a.divide(b, 15, RoundingMode.HALF_UP);
	}

}
