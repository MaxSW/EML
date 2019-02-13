package com.xwarner.eml.interpreter.evaluator.operators.numeric;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.interpreter.context.variables.values.Vector;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;

public class SubtractOperator extends Operator {

	public SubtractOperator() {
		super("-", 2, true, TYPE_NUMERIC);
	}

	public BigDecimal evaluateNumeric(BigDecimal a, BigDecimal b) {
		return a.subtract(b);
	}

	public Vector evaluateVector(Vector a, Vector b) {
		if (a.size != b.size)
			throw new Error("vectors must be the same size");
		Vector var = new Vector(a.size);
		for (int i = 0; i < a.size; i++) {
			var.vals[i] = a.vals[i].subtract(b.vals[i]);
		}
		return var;
	}

	public Matrix evaluateMatrix(Matrix a, Matrix b) {
		if (a.w != b.w || a.h != b.h)
			throw new Error("matrices must have the same dimensions");
		Matrix v = new Matrix(a.h, a.w);
		for (int i = 0; i < a.h; i++) {
			for (int j = 0; j < a.w; j++) {
				v.vals[i][j] = a.vals[i][j].subtract(b.vals[i][j]);
			}
		}
		return v;
	}

}
