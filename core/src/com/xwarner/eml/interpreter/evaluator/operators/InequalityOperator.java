package com.xwarner.eml.interpreter.evaluator.operators;

import java.math.BigDecimal;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.variables.values.Matrix;

public class InequalityOperator extends Operator {

	public InequalityOperator() {
		super("!=", 1, true, TYPE_BOOLEAN);
	}

	public boolean evaluateBoolean(boolean a, boolean b) {
		return a != b;
	}

	public boolean evaluateNumericBoolean(BigDecimal a, BigDecimal b) {
		return a.compareTo(b) != 0;
	}

	public boolean evaluateStringBoolean(String a, String b) {
		return !a.equals(b);
	}

	public boolean evaluateMatrixBoolean(Matrix a, Matrix b) {
		if (a.w != b.w || a.h != b.h)
			Core.error.error("matrices must have the same dimensions");
		for (int i = 0; i < a.h; i++) {
			for (int j = 0; j < a.w; j++) {
				if (!a.vals[i][j].equals(b.vals[i][j]))
					return true;
			}
		}
		return false;
	}

}
