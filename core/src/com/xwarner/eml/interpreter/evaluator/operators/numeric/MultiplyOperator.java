package com.xwarner.eml.interpreter.evaluator.operators.numeric;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;

public class MultiplyOperator extends Operator {

	public MultiplyOperator() {
		super("*", 3, true, TYPE_NUMERIC);
	}

	public BigDecimal evaluateNumeric(BigDecimal a, BigDecimal b) {
		return a.multiply(b);
	}

	public Matrix evaluateMatrix(Matrix a, Matrix b) {
		if (a.w != b.h)
			throw new Error("matrix a must have the same width as matrix b's height");
		Matrix v = new Matrix(a.h, b.w);
		for (int i = 0; i < a.h; i++) {
			for (int j = 0; j < b.w; j++) {
				BigDecimal sum = BigDecimal.ZERO;
				for (int k = 0; k < a.w; k++) {
					sum = sum.add(a.vals[i][k].multiply(b.vals[k][j]));
				}
				v.vals[i][j] = sum;
			}
		}
		return v;
	}

	public Matrix evaluateMatrix(BigDecimal a, Matrix b) {
		Matrix v = new Matrix(b.h, b.w);
		for (int i = 0; i < b.h; i++) {
			for (int j = 0; j < b.w; j++) {
				v.vals[i][j] = a.multiply(b.vals[i][j]);
			}
		}
		return v;
	}

}
