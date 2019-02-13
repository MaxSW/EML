package com.xwarner.eml.interpreter.evaluator;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.interpreter.context.variables.values.Vector;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;

public class ExpressionEntry {

	public static final int TYPE_NUM = 1, TYPE_OP = 2, TYPE_BOOL = 3, TYPE_STRING = 4, TYPE_VEC = 5, TYPE_MAT = 6;

	public int type;

	public Operator operator;
	public BigDecimal value;
	public boolean booleanValue;
	public String stringValue;
	public Vector vector;
	public Matrix matrix;

}
