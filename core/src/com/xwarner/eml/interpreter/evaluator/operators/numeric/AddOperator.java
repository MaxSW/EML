package com.xwarner.eml.interpreter.evaluator.operators.numeric;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;

public class AddOperator extends Operator {

	public AddOperator() {
		super("+", 2, true, TYPE_NUMERIC);
	}

	public BigDecimal evaluateNumeric(BigDecimal a, BigDecimal b) {
		return a.add(b);
	}

	public Matrix evaluateMatrix(Matrix a, Matrix b) {
		if (a.w != b.w || a.h != b.h)
			throw new Error("matrices must have the same dimensions");
		Matrix v = new Matrix(a.h, a.w);
		for (int i = 0; i < a.h; i++) {
			for (int j = 0; j < a.w; j++) {
				v.vals[i][j] = a.vals[i][j].add(b.vals[i][j]);
			}
		}
		return v;
	}

	public String evaluateString(String a, String b) {
		return a + b;
	}

}
